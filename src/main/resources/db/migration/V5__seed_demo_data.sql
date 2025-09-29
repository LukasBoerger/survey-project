-- Demo-User
INSERT INTO users (id, email, display_name, password_hash)
VALUES (gen_random_uuid(), 'demo@example.com', 'Demo User', '#hash')
    ON CONFLICT (email) DO NOTHING;

-- Demo-Survey
WITH u AS (
    SELECT id FROM users WHERE email='demo@example.com' LIMIT 1
    )
INSERT INTO surveys (id, owner_id, title, public_id)
SELECT gen_random_uuid(), u.id, 'Welcome Survey', 'WELCOME-001' FROM u
    ON CONFLICT (public_id) DO NOTHING;

-- Fragen
WITH s AS (SELECT id FROM surveys WHERE public_id='WELCOME-001')
INSERT INTO questions (id, survey_id, type, prompt, position)
SELECT gen_random_uuid(), s.id, 'EMOJI', 'Wie fühlst du dich nach der Nutzung?', 1 FROM s
UNION ALL
SELECT gen_random_uuid(), s.id, 'STAR',   'Bewerte das Erlebnis (1–5)',        2 FROM s
UNION ALL
SELECT gen_random_uuid(), s.id, 'TEXT',   'Was sollen wir verbessern?',        3 FROM s;

-- EMOJI-Optionen
WITH q AS (
    SELECT q.id FROM questions q
                         JOIN surveys s ON s.id=q.survey_id
    WHERE s.public_id='WELCOME-001' AND q.type='EMOJI'
)
INSERT INTO question_options (id, question_id, ord, label, value)
SELECT gen_random_uuid(), q.id, 1, '😀', 'happy' FROM q
UNION ALL SELECT gen_random_uuid(), q.id, 2, '😐', 'neutral' FROM q
UNION ALL SELECT gen_random_uuid(), q.id, 3, '😞', 'sad' FROM q;
