-- UUID-Generator (Postgres 13+)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- OPTIONS für EMOJI (oder zukünftige Single-Choice)
CREATE TABLE IF NOT EXISTS question_options (
                                                id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    question_id uuid NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
    ord         int  NOT NULL,
    label       text NOT NULL,   -- z.B. 😀 😐 😞 oder "Happy"
    value       text,            -- optionaler technischer Wert
    UNIQUE (question_id, ord)
    );
CREATE INDEX IF NOT EXISTS idx_qopts_question ON question_options(question_id);

-- Für "option gehört zur Frage": Ziel für einen zusammengesetzten FK
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_indexes WHERE indexname = 'uq_qopts_qid_id'
  ) THEN
    -- unique auf (question_id, id) als Ziel für den FK unten
    EXECUTE 'CREATE UNIQUE INDEX uq_qopts_qid_id ON question_options(question_id, id)';
END IF;
END$$;

-- RESPONSES (eine abgegebene Umfrage)
CREATE TABLE IF NOT EXISTS responses (
                                         id           uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    survey_id    uuid NOT NULL REFERENCES surveys(id) ON DELETE RESTRICT,
    submitted_at timestamptz NOT NULL DEFAULT now(),
    ip_hash      text,
    user_agent   text,
    locale       text
    );
CREATE INDEX IF NOT EXISTS idx_responses_survey_time ON responses(survey_id, submitted_at DESC);

-- ANSWERS (Antworten je Frage)
CREATE TABLE IF NOT EXISTS answers (
                                       id             uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    response_id    uuid NOT NULL REFERENCES responses(id) ON DELETE CASCADE,
    question_id    uuid NOT NULL REFERENCES questions(id) ON DELETE RESTRICT,
    option_id      uuid     REFERENCES question_options(id) ON DELETE RESTRICT,
    text_answer    text,
    numeric_answer numeric(10,2)
    );

-- pro Response & Frage nur eine Antwort
CREATE UNIQUE INDEX IF NOT EXISTS uq_answers_unique_per_question
    ON answers(response_id, question_id);

CREATE INDEX IF NOT EXISTS idx_answers_response ON answers(response_id);
CREATE INDEX IF NOT EXISTS idx_answers_question ON answers(question_id);
CREATE INDEX IF NOT EXISTS idx_answers_option   ON answers(option_id);

-- Zusätzliche Referentielle Absicherung:
-- Falls option_id gesetzt ist, muss es zur gleichen question_id gehören.
ALTER TABLE answers
DROP CONSTRAINT IF EXISTS fk_answers_qid_opt,       -- idempotent
  ADD CONSTRAINT fk_answers_qid_opt
    FOREIGN KEY (question_id, option_id)
    REFERENCES question_options (question_id, id)
    ON UPDATE RESTRICT ON DELETE RESTRICT;

-- ********** Typvalidierung per Trigger (statt CHECK mit Subquery) **********
CREATE OR REPLACE FUNCTION enforce_answer_type()
RETURNS trigger AS $$
DECLARE
qtype text;
BEGIN
SELECT type INTO qtype FROM questions WHERE id = NEW.question_id;

IF qtype = 'TEXT' THEN
    IF NEW.text_answer IS NULL OR NEW.numeric_answer IS NOT NULL OR NEW.option_id IS NOT NULL THEN
      RAISE EXCEPTION 'Invalid TEXT answer for question %', NEW.question_id;
END IF;

  ELSIF qtype = 'STAR' THEN
    IF NEW.numeric_answer IS NULL OR NEW.text_answer IS NOT NULL OR NEW.option_id IS NOT NULL THEN
      RAISE EXCEPTION 'Invalid STAR answer for question %', NEW.question_id;
END IF;

  ELSIF qtype = 'EMOJI' THEN
    IF NEW.option_id IS NULL OR NEW.text_answer IS NOT NULL OR NEW.numeric_answer IS NOT NULL THEN
      RAISE EXCEPTION 'Invalid EMOJI answer for question %', NEW.question_id;
END IF;

ELSE
    RAISE EXCEPTION 'Unknown question type % for question %', qtype, NEW.question_id;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_answers_enforce_type ON answers;
CREATE TRIGGER trg_answers_enforce_type
    BEFORE INSERT OR UPDATE ON answers
                         FOR EACH ROW EXECUTE FUNCTION enforce_answer_type();
