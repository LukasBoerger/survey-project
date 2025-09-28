-- UUID-Generator (Postgres 13+)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- OPTIONS für EMOJI (oder zukünftige Single-Choice)
CREATE TABLE IF NOT EXISTS question_options (
                                                id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    question_id uuid NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
    ord         int  NOT NULL,
    label       text NOT NULL,           -- z.B. 😀 😐 😞 oder "Happy"
    value       text,                    -- optionaler technischer Wert
    UNIQUE (question_id, ord)
    );
CREATE INDEX IF NOT EXISTS idx_qopts_question ON question_options(question_id);

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
    numeric_answer numeric(10,2),

    -- Typkonsistenz: pro Antwort genau EIN Feld passend zum Fragetyp
    -- TEXT   -> text_answer
    -- STAR   -> numeric_answer (z.B. 1..5)
    -- EMOJI  -> option_id
    CONSTRAINT answers_type_guard CHECK (
    (
                                            option_id IS NULL
                                            AND numeric_answer IS NULL
                                            AND text_answer IS NOT NULL
                                            AND EXISTS (SELECT 1 FROM questions q WHERE q.id = question_id AND q.type = 'TEXT')
    )
    OR
(
    option_id IS NULL
    AND text_answer IS NULL
    AND numeric_answer IS NOT NULL
    AND EXISTS (SELECT 1 FROM questions q WHERE q.id = question_id AND q.type = 'STAR')
    )
    OR
(
    text_answer IS NULL
    AND numeric_answer IS NULL
    AND option_id IS NOT NULL
    AND EXISTS (SELECT 1 FROM questions q WHERE q.id = question_id AND q.type = 'EMOJI')
    )
    )
    );

-- pro Response & Frage nur eine Antwort
CREATE UNIQUE INDEX IF NOT EXISTS uq_answers_unique_per_question
    ON answers(response_id, question_id);

CREATE INDEX IF NOT EXISTS idx_answers_response ON answers(response_id);
CREATE INDEX IF NOT EXISTS idx_answers_question ON answers(question_id);
CREATE INDEX IF NOT EXISTS idx_answers_option ON answers(option_id);
