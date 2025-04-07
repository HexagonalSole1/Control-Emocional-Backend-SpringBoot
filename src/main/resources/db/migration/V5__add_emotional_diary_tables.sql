-- src/main/resources/db/migration/V5__add_emotional_diary_tables.sql



-- Insertar emociones predefinidas
INSERT INTO emotions (name, description, color, icon, created_at, updated_at) VALUES
                                                                                  ('Feliz', 'Sentimiento de alegría y satisfacción', '#FFD700', 'emoji-smile', NOW(), NOW()),
                                                                                  ('Triste', 'Sentimiento de pena o dolor emocional', '#1E90FF', 'emoji-frown', NOW(), NOW()),
                                                                                  ('Enojado', 'Sentimiento de molestia o irritación', '#FF4500', 'emoji-angry', NOW(), NOW()),
                                                                                  ('Asustado', 'Sentimiento de miedo o preocupación', '#9932CC', 'emoji-fearful', NOW(), NOW()),
                                                                                  ('Cansado', 'Sensación de fatiga física o mental', '#808080', 'emoji-tired', NOW(), NOW()),
                                                                                  ('Sorprendido', 'Sensación de asombro o desconcierto', '#FFA500', 'emoji-surprised', NOW(), NOW()),
                                                                                  ('Tranquilo', 'Sensación de calma y paz interior', '#90EE90', 'emoji-neutral', NOW(), NOW()),
                                                                                  ('Ansioso', 'Sentimiento de inquietud o nerviosismo', '#FF69B4', 'emoji-dizzy', NOW(), NOW());