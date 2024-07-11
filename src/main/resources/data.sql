INSERT INTO user (user_id, device_id, created_at, updated_at) VALUES
                                                                  (1, 'device_1', '2018-02-01', '2018-12-18');

INSERT INTO theme (theme_id, user_id, created_at, updated_at, theme_title) VALUES
                                                                                     (1, 1, '2022-11-08', '2021-02-24', 'Package Title 1'),
                                                                                     (2, 1, '2023-03-03', null, 'Package Title 2');
INSERT INTO version (major_version, minor_version, theme_id, created_at) VALUES
                                                                               (1, 0, 1, '2021-01-08'),
                                                                               (1, 1, 1, '2021-01-11'),
                                                                               (1, 2, 1, '2021-01-11'),
                                                                               (2, 0, 1, '2021-02-03'),
                                                                               (3, 0, 1, '2021-02-24');

INSERT INTO script (script_id, script_content, created_at, d_type, major_version, minor_version, total_expect_time) VALUES
                                                                                                     (1, 'Script Content 1', '2022-05-22', 1, 1, 0, '00:01:30'),
                                                                                                     (2, 'Script Content 2', '2022-05-22', 1, 2, 0, '00:01:30'),
                                                                                                     (3, 'Script Voice Content 1.1', '2022-05-22', 1, 1, 1, '00:01:30'),
                                                                                                     (4, 'Script Voice Content 1.2', '2022-05-22', 1, 1, 2, '00:01:30');

INSERT INTO sentence (sentence_id, sentence_content, sentence_order, paragraph_id, script_id, created_at, sentence_expect_time) VALUES
                                                                                                              (1, 'Sentence Content 1', 4, 1, 1, '2022-05-07','00:01:40'),
                                                                                                              (2, 'Sentence Content 2', 8, 1, 1, '2023-11-25','00:01:39'),
                                                                                                              (3, 'Sentence Content 3', 10, 2, 1, '2020-05-09','00:01:38'),
                                                                                                              (4, 'Sentence Content 4', 9, 2, 1, '2020-12-06','00:01:37'),
                                                                                                              (5, 'Sentence Content 5', 6, 3, 1, '2021-06-21','00:01:36'),
                                                                                                              (6, 'Sentence Content 6', 1, 3, 1, '2018-12-27','00:01:35'),
                                                                                                              (7, 'Sentence Content 7', 5, 4, 1, '2020-04-25','00:01:34'),
                                                                                                              (8, 'Sentence Content 8', 2, 4, 1, '2020-04-08','00:01:33'),
                                                                                                              (9, 'Sentence Content 9', 3, 0, 1, '2018-04-22','00:01:32'),
                                                                                                              (10, 'Sentence Content 10', 7, 0, 1, '2019-01-14','00:01:31'),
                                                                                                              (11, 'Sentence Content 11', 1, 1, 1, '2019-01-14','00:01:31');
