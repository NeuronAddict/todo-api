INSERT INTO CustomUser(id, name, hashedpassword, roles, email, telephone)
VALUES (0, 'alice', '$2y$05$ZAUrpjDL2h6VJgT2sQm2H.C5K0l7T8bqTonEKE9HsfBUK1CCsz9hS', 'admin', 'alice@neuronaddict.org',
        '0606060606'),
       (1, 'bob', '$2y$05$ZAUrpjDL2h6VJgT2sQm2H.C5K0l7T8bqTonEKE9HsfBUK1CCsz9hS', 'user', 'bob@neuronaddict.org',
        '0707070707'),
       (2, 'alice3', '$2y$05$ZAUrpjDL2h6VJgT2sQm2H.C5K0l7T8bqTonEKE9HsfBUK1CCsz9hS', 'user', 'alice3@neuronaddict.org',
        '0808080808');
ALTER SEQUENCE CustomUser_SEQ RESTART WITH 3;
