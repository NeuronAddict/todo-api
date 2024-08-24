INSERT INTO LogEntry(id, type, message_id, initiator_id)
VALUES (0, 'add', 0, 0);
ALTER SEQUENCE LogEntry_SEQ RESTART WITH 1;