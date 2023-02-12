--INSERT INTO rollre (id, y, w, s) VALUES (1, '2775', '1bf9', '14d3');

--INSERT INTO voters (id, first_name, last_name, dob, roll_id) VALUES (1, 'Stephane', 'Augier', '2001-06-18', 1);

INSERT INTO voters (id, first_name, last_name, dob) VALUES (1, 'Stephane', 'Augier', '2001-06-18');

--INSERT INTO ballots (id, randint, timestamp, permutation, s, w)
--    VALUES (2, 1960397462, '2023-02-10 18:24:03.764', '1', '6748', '42b0');
--
--INSERT INTO permutationme (id, permutation) VALUES (1, 1);
--
--INSERT INTO encrypted_ballots (ID, BLIND_FACTOR, CYPHER_TEXT, EPHEMERAL_KEY, MASKEDY, PERMUTATION, S, W, Y)
--    VALUES (3, '66d7', 'ptStIrbyXxhrtwq97EyErHmPDRUz4aegOtNsOQRnULZAa3R50/XUlyvQ/D4yzsDx', '2057', '5102', 1, '1bf9', '14d3', '2775');