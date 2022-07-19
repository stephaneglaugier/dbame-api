INSERT INTO roll (id, y, w, s) VALUES (1, '11889d7a651c8851', '0d3f3ffd7289dfe9', '06e208788a24cba3');
INSERT INTO roll (id, y, w, s) VALUES (2, '87b8a04653a20030', '0a632db7bff7b2fc', '11dd4680207303ea');
INSERT INTO roll (id, y, w, s) VALUES (3, '35b8d906f0d0f068', '19a6ec2f78fd884a', '124a533c8208e538');
INSERT INTO roll (id, y, w, s) VALUES (4, '00c10099630d3c51', '1a53315fb071ed90', '176fd08f534d70bd');
INSERT INTO roll (id, y, w, s) VALUES (5, '6ce846bc810d2f50', '0d032b2021d16f0a', '12a73722b3edad9c');

INSERT INTO voters (id, first_name, last_name, dob, roll_id) VALUES (1, 'Stephane', 'Augier', '2001-06-18', 1);
INSERT INTO voters (id, first_name, last_name, dob, roll_id) VALUES (2, 'Stephane', 'Augier', '2001-06-18', 2);
INSERT INTO voters (id, first_name, last_name, dob, roll_id) VALUES (3, 'Stephane', 'Augier', '2001-06-18', 3);
INSERT INTO voters (id, first_name, last_name, dob, roll_id) VALUES (4, 'Stephane', 'Augier', '2001-06-18', 4);
INSERT INTO voters (id, first_name, last_name, dob, roll_id) VALUES (5, 'Stephane', 'Augier', '2001-06-18', 5);

INSERT INTO signed_ballot (id, randint, timestamp, permutation, s, w)
    VALUES (1, 517592347, '2022-07-19 16:15:05.675', '2', '1049af9459cc7f9b', '07e00dcad850913e');
INSERT INTO signed_ballot (id, randint, timestamp, permutation, s, w)
    VALUES (2, 2033490642, '2022-07-19 16:15:05.675', '1', '1343223965b2b49f', '15595bda8577de35');
INSERT INTO signed_ballot (id, randint, timestamp, permutation, s, w)
    VALUES (3, 17768119, '2022-07-19 16:15:05.676', '4', '148ef8160f0bc2be', '0086fe636a7a0aaf');
INSERT INTO signed_ballot (id, randint, timestamp, permutation, s, w)
    VALUES (4, 773967591, '2022-07-19 16:15:05.676', '5', '035f09b45d526f1d', '16365fc1ac17f624');
INSERT INTO signed_ballot (id, randint, timestamp, permutation, s, w)
    VALUES (5, 1060549677, '2022-07-19 16:15:05.676', '3', '133e44b79d345f58', '17ae241a4f5715fd');