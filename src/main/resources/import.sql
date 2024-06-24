
INSERT INTO customuser(id, name, roles) VALUES (0, 'alice', 'admin'),(1, 'bob', 'user'), (2, 'alice3', 'user');
ALTER SEQUENCE customuser_seq RESTART WITH 3;

INSERT INTO message(id, message, author_id, duedate) VALUES (0, 'hello', 0, '2024-12-12');
ALTER SEQUENCE message_seq RESTART WITH 1;