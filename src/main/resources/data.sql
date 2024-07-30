INSERT INTO user (birth, created_at, updated_at, social_id, user_id, device_id, email, first_name, last_name, nick_name, authorization_server, gender, role, user_status) VALUES
    ('2018-02-01', '2018-02-01', '2018-12-18', '124123123', '1', 'device_1', 'asdf@naver.com', 'ex', 'ample', 'nick', 'KAKAO', 'MALE', 'ROLE_COMMON', 'ACTIVE');

INSERT INTO usage_time(usage_time_id, user_id, created_at, updated_at, remaining_time) VALUES
    (1, 1, '2018-02-01', '2018-12-18', '9000');

INSERT INTO theme (theme_id, theme_title, user_id, updated_at, created_at) VALUES
    (1, 'Theme Title 1', 1, '2018-02-01', '2018-12-18'),
    (2, 'Theme Title 2', 1, '2018-02-01', '2018-12-18'),
    (3, 'Theme Title 3', 1, '2018-02-01', '2018-12-18'),
    (4, 'Theme Title 4', 1, '2018-02-01', '2018-12-18'),
    (5, 'Theme Title 5', 1, '2018-02-01', '2018-12-18'),
    (6, 'Theme Title 6', 1, '2018-02-01', '2018-12-18'),
    (7, 'Theme Title 7', 1, '2018-02-01', '2018-12-18'),
    (8, 'Theme Title 8', 1, '2018-02-01', '2018-12-18'),
    (9, 'Theme Title 9', 1, '2018-02-01', '2018-12-18'),
    (10, 'Theme Title 10', 1, '2018-02-01', '2018-12-18');

INSERT INTO version (theme_id, major_version, minor_version, created_at) VALUES
    (1, 1, 0, '2018-02-01'),
    (1, 1, 1, '2018-02-01'),
    (1, 1, 2, '2018-02-01'),
    (1, 1, 3, '2018-02-01'),
    (1, 2, 0, '2018-02-01'),
    (1, 2, 1, '2018-02-01'),
    (2, 1, 0, '2018-02-01'),
    (2, 1, 1, '2018-02-01'),
    (2, 2, 0, '2018-02-01'),
    (2, 2, 1, '2018-02-01');


INSERT INTO script (script_id, script_content, theme_id, created_at, d_type, major_version, minor_version, total_expect_time) VALUES
    (1, 'Hello. my 1. name is 1', 1,'2018-02-01', 'INPUT', 1, 0, '00:01:30'),
    (5, 'Hello. my 5. name is 5', 1,'2018-02-01', 'INPUT', 2, 0, '00:01:30'),
    (7, 'Hello. my 7. name is 7', 2,'2018-02-01', 'INPUT', 1, 0, '00:01:30'),
    (10, 'Hello. my 10. name is 10', 2,'2018-02-01', 'INPUT', 2, 0, '00:01:30');

INSERT INTO script (script_id, script_content, theme_id, created_at, d_type, major_version, minor_version, total_real_time) VALUES
    (2, 'Hello. my 2. name is 2', 1,'2018-02-01', 'STT', 1, 1, '00:01:30'),
    (3, 'Hello. my 3. name is 3', 1,'2018-02-01', 'STT', 1, 2, '00:01:30'),
    (4, 'Hello. my 4. name is 4', 1,'2018-02-01', 'STT', 1, 3, '00:01:30'),
    (6, 'Hello. my 6. name is 6', 1,'2018-02-01', 'STT', 2, 1, '00:01:30'),
    (8, 'Hello. my 8. name is 8', 2,'2018-02-01', 'STT', 1, 1, '00:01:30'),
    (9, 'Hello. my 9. name is 9', 2,'2018-02-01', 'STT', 2, 1, '00:01:30');


INSERT INTO sentence (sentence_id, sentence_content, sentence_order, paragraph_id, script_id, created_at, sentence_expect_time) VALUES
    ("1", 'Hello.', 1, 1, 1, '2018-02-01', '00:01:30'),
    ("3", 'my 1.', 2, 1, 1, '2018-02-01', '00:01:30'),
    ("2", 'name is 1', 3, 2, 1, '2018-02-01', '00:01:30'),

    ("4", 'Hello.', 1, 1, 2, '2018-02-01', '00:01:30'),
    ("5", 'my 2.', 2, 1, 2, '2018-02-01', '00:01:30'),
    ("6", 'name is 2', 3, 2, 2, '2018-02-01', '00:01:30'),

    ("7", 'Hello.', 1, 1, 3, '2018-02-01', '00:01:30'),
    ("8", 'my 3.', 2, 1, 3, '2018-02-01', '00:01:30'),
    ("9", 'name is 3', 3, 2, 3, '2018-02-01', '00:01:30'),

    ("10", 'Hello.', 1, 1, 4, '2018-02-01', '00:01:30'),
    ("11", 'my 4.', 2, 1, 4, '2018-02-01', '00:01:30'),
    ("12", 'name is 4', 3, 2, 5, '2018-02-01', '00:01:30'),

    ("13", 'Hello.', 1, 1, 5, '2018-02-01', '00:01:30'),
    ("14", 'my 5.', 2, 1, 5, '2018-02-01', '00:01:30'),
    ("15", 'name is 5', 3, 2, 5, '2018-02-01', '00:01:30'),

    ("16", 'Hello.', 1, 1, 6, '2018-02-01', '00:01:30'),
    ("17", 'my 6.', 2, 1, 6, '2018-02-01', '00:01:30'),
    ("18", 'name is 6', 3, 2, 6, '2018-02-01', '00:01:30'),

    ("19", 'Hello.', 1, 1, 7, '2018-02-01', '00:01:30'),
    ("20", 'my 7.', 2, 1, 7, '2018-02-01', '00:01:30'),
    ("21", 'name is 7', 3, 2, 7, '2018-02-01', '00:01:30'),

    ("22", 'Hello.', 1, 1, 8, '2018-02-01', '00:01:30'),
    ("23", 'my 8.', 2, 1, 8, '2018-02-01', '00:01:30'),
    ("24", 'name is 8', 3, 2, 8, '2018-02-01', '00:01:30'),

    ("25", 'Hello.', 1, 1, 9, '2018-02-01', '00:01:30'),
    ("26", 'my 9.', 2, 1, 9, '2018-02-01', '00:01:30'),
    ("27", 'name is 9', 3, 2, 9, '2018-02-01', '00:01:30'),

    ("28", 'Hello.', 1, 1, 10, '2018-02-01', '00:01:30'),
    ("29", 'my 10.', 2, 1, 10, '2018-02-01', '00:01:30'),
    ("30", 'name is 10', 3, 2, 10, '2018-02-01', '00:01:30');

