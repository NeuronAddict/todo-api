INSERT INTO customuser(id, name, hashedpassword, roles)
VALUES (0, 'alice', '$2y$05$ZAUrpjDL2h6VJgT2sQm2H.C5K0l7T8bqTonEKE9HsfBUK1CCsz9hS', 'admin'),
       (1, 'bob', '$2y$05$ZAUrpjDL2h6VJgT2sQm2H.C5K0l7T8bqTonEKE9HsfBUK1CCsz9hS', 'user'),
       (2, 'alice3', '$2y$05$ZAUrpjDL2h6VJgT2sQm2H.C5K0l7T8bqTonEKE9HsfBUK1CCsz9hS', 'user');
ALTER SEQUENCE customuser_seq RESTART WITH 3;

INSERT INTO message(id, message, author_id, duedate)
VALUES (0, 'hello', 0, '2024-12-12');
ALTER SEQUENCE message_seq RESTART WITH 1;

INSERT INTO logentry(id, type, message_id, initiator_id)
VALUES (0, 'add', 0, 0);
ALTER SEQUENCE logentry_seq RESTART WITH 1;
