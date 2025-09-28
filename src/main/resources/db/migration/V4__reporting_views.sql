-- Counts für EMOJI-Optionen
CREATE OR REPLACE VIEW v_emoji_option_counts AS
SELECT
    s.id   AS survey_id,
    q.id   AS question_id,
    o.id   AS option_id,
    o.label,
    COUNT(a.id) AS votes
FROM surveys s
         JOIN questions q ON q.survey_id = s.id AND q.type = 'EMOJI'
         JOIN question_options o ON o.question_id = q.id
         LEFT JOIN answers a ON a.option_id = o.id
GROUP BY s.id, q.id, o.id, o.label;

-- STAR (Durchschnitt / Anzahl)
CREATE OR REPLACE VIEW v_star_stats AS
SELECT
    s.id   AS survey_id,
    q.id   AS question_id,
    AVG(a.numeric_answer)::numeric(10,2) AS avg_rating,
    COUNT(a.id) AS votes
FROM surveys s
         JOIN questions q ON q.survey_id = s.id AND q.type = 'STAR'
         LEFT JOIN answers a ON a.question_id = q.id
GROUP BY s.id, q.id;

-- TEXT (nur Anzahl; Inhalte separat ziehen)
CREATE OR REPLACE VIEW v_text_counts AS
SELECT
    s.id   AS survey_id,
    q.id   AS question_id,
    COUNT(a.id) AS answers
FROM surveys s
         JOIN questions q ON q.survey_id = s.id AND q.type = 'TEXT'
         LEFT JOIN answers a ON a.question_id = q.id
GROUP BY s.id, q.id;
