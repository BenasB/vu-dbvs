-- Required points trigger
INSERT INTO solutions VALUES
(DEFAULT, DEFAULT, 'labai teisingas kodas', 1, 'jonce');

-- Required test result reference to same problem
INSERT INTO test_results VALUES
(DEFAULT, 'true', 2, 2, 1, 3);

-- Unique test case id and solution id pair in test result
INSERT INTO test_results VALUES
(DEFAULT, 'true', 2, 2, 1, 3);