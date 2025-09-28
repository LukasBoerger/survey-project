-- Eindeutige Position je Survey
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_indexes
    WHERE indexname = 'uq_questions_survey_position'
  ) THEN
    EXECUTE 'CREATE UNIQUE INDEX uq_questions_survey_position ON questions(survey_id, position)';
END IF;
END$$;

-- updated_at-Trigger für surveys (+ optional questions)
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at := now();
RETURN NEW;
END; $$ LANGUAGE plpgsql;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_trigger WHERE tgname = 'trg_surveys_set_updated'
  ) THEN
CREATE TRIGGER trg_surveys_set_updated
    BEFORE UPDATE ON surveys
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
END IF;

  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name='questions' AND column_name='updated_at'
  ) THEN
ALTER TABLE questions ADD COLUMN updated_at timestamptz DEFAULT now();
CREATE TRIGGER trg_questions_set_updated
    BEFORE UPDATE ON questions
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();
END IF;
END$$;
