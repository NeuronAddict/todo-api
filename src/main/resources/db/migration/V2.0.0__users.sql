INSERT INTO CustomUser(id, name, hashedpassword, roles)
VALUES (0, 'alice', '$2y$05$ZAUrpjDL2h6VJgT2sQm2H.C5K0l7T8bqTonEKE9HsfBUK1CCsz9hS', 'admin'),
       (1, 'bob', '$2y$05$ZAUrpjDL2h6VJgT2sQm2H.C5K0l7T8bqTonEKE9HsfBUK1CCsz9hS', 'user'),
       (2, 'alice3', '$2y$05$ZAUrpjDL2h6VJgT2sQm2H.C5K0l7T8bqTonEKE9HsfBUK1CCsz9hS', 'user');
ALTER SEQUENCE CustomUser_SEQ RESTART WITH 3;
