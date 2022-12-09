CREATE DATABASE programming_problems_platform;

\c programming_problems_platform

-- Create tables

CREATE TABLE users
(
    username varchar(255) PRIMARY KEY,
    first_name varchar(255) NOT NULL,
    last_name varchar(255) NOT NULL,
    email varchar(255) NOT NULL CHECK (email like '%_@__%.__%'),
    points int NOT NULL DEFAULT 0 CHECK (points >= 0)
);

CREATE TABLE problems
(
    id serial PRIMARY KEY,
    description text NOT NULL UNIQUE,
    required_points int NOT NULL DEFAULT 0 CHECK (required_points >= 0),
    reward_points int NOT NULL DEFAULT 1 CHECK (reward_points >= 1),
    time_limit numeric(16, 14) NOT NULL DEFAULT 2 CHECK (time_limit >= 0),
    memory_limit int NOT NULL DEFAULT 1024 CHECK (memory_limit >= 1)
);

CREATE TABLE test_cases
(
    id serial PRIMARY KEY,
    input text NOT NULL,
    expected_output text NOT NULL,
    problem_id int NOT NULL REFERENCES problems(id) ON DELETE CASCADE
);

CREATE TABLE solutions
(
    id serial PRIMARY KEY,
    submitted_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    source_file text NOT NULL,
    problem_id int NOT NULL REFERENCES problems(id) ON DELETE CASCADE,
    submitted_by varchar(255) NOT NULL REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE test_results
(
    id serial PRIMARY KEY,
    is_success boolean NOT NULL,
    time_spent numeric(16, 14) NOT NULL CHECK (time_spent >= 0),
    memory_used int NOT NULL CHECK (memory_used >= 0),
    solution_id int NOT NULL REFERENCES solutions(id) ON DELETE CASCADE,
    test_case_id int NOT NULL REFERENCES test_cases(id) ON DELETE CASCADE,
    UNIQUE (solution_id, test_case_id)
);

-- Create views

CREATE VIEW leaderboard_points AS
SELECT username, points
FROM users
ORDER BY points DESC, username; 

CREATE VIEW problems_popularity AS
SELECT A.id, COUNT(B.id) AS "solution attempts"
FROM problems AS A
LEFT OUTER JOIN solutions AS B ON B.problem_id = A.id
GROUP BY A.id
ORDER BY COUNT(B.id) DESC, A.id;

CREATE MATERIALIZED VIEW leaderboard_solutions_monthly AS
WITH successful_solutions(solution_id) AS
(
    SELECT solution_id
    FROM test_results
    GROUP BY solution_id
    HAVING COUNT(CASE WHEN is_success='false' THEN 1 END) = 0
)
SELECT C.username, COUNT(DISTINCT B.problem_id) AS "correct solutions"
FROM successful_solutions AS A
JOIN solutions AS B ON A.solution_id = B.id
RIGHT OUTER JOIN users AS C ON B.submitted_by = C.username
GROUP BY B.submitted_by, C.username
ORDER BY COUNT(B.submitted_by) DESC, C.username;

-- Create triggers

CREATE FUNCTION enough_points_for_solution_submition() 
   RETURNS TRIGGER 
   LANGUAGE PLPGSQL
AS $$
BEGIN
    IF 
    (
        SELECT points 
        FROM users
        WHERE NEW.submitted_by = username
    )
    < 
    (
        SELECT required_points 
        FROM problems
        WHERE NEW.problem_id = id
    ) THEN
      RAISE EXCEPTION 'User does not have enough points to solve this problem';
    END IF;

    RETURN NEW;
END;
$$;

CREATE TRIGGER require_points_for_solution_submition
BEFORE INSERT ON solutions
FOR EACH ROW EXECUTE PROCEDURE enough_points_for_solution_submition();

CREATE FUNCTION is_test_result_pointing_to_same_problem() 
   RETURNS TRIGGER 
   LANGUAGE PLPGSQL
AS $$
BEGIN
    IF 
    (
        SELECT problem_id
        FROM test_cases
        WHERE NEW.test_case_id = id
    )
    <> 
    (
        SELECT problem_id
        FROM solutions
        WHERE NEW.solution_id = id
    ) THEN
      RAISE EXCEPTION 'Test result is referencing a test case from a different problem';
    END IF;

    RETURN NEW;
END;
$$;

CREATE TRIGGER require_test_result_to_reference_same_problem
BEFORE INSERT ON test_results
FOR EACH ROW EXECUTE PROCEDURE is_test_result_pointing_to_same_problem();

-- Create index

CREATE UNIQUE INDEX description_search
ON problems (description);

-- Populate data

INSERT INTO users VALUES
('benasb', 'Benas', 'Budrys', 'benas.budrys@stud.mif.vu.lt', 42),
('jonce', 'Jonas', 'Jonaitis', 'jonas.jonaitis@gmail.com', DEFAULT),
('vardis123', 'Vardenis', 'Pavardenis', 'varpavar@manomail.org', 15);

INSERT INTO problems VALUES
(DEFAULT, 'Sudekite du naturaliuosius skaicius a ir b. Skaiciai ivedami atskirti tarpu', 15, 3, 3.12345, 556),
(DEFAULT, 'Perrasykite duota zodi atvirksciai', DEFAULT, DEFAULT, DEFAULT, DEFAULT),
(DEFAULT, 'Dar viena idomi uzduotis', DEFAULT, DEFAULT, DEFAULT, DEFAULT);

INSERT INTO test_cases VALUES
(DEFAULT, '5 6', '11', 1),
(DEFAULT, '-13 14', '2', 1),
(DEFAULT, 'alus', 'sula', 2),
(DEFAULT, 'benas', 'saneb', 2),
(DEFAULT, 'alussula', 'alussula', 2),
(DEFAULT, 'labaiIlgasZodis', 'sidoZsaglIiabal', 2),
(DEFAULT, '2 6', '64', 3);

INSERT INTO solutions VALUES
(DEFAULT, DEFAULT, '#include <stdio.h>\nint main() {\n   printf("Hello, World!");\n   return 0;\n}\n', 1, 'benasb'),
(DEFAULT, DEFAULT, 'teisingas kodas', 1, 'benasb'),
(DEFAULT, DEFAULT, 'printf("hi");', 2, 'benasb'),
(DEFAULT, DEFAULT, 'mano irgi teisingas source code', 1, 'vardis123'),
(DEFAULT, DEFAULT, 'teisingas kodas numeris du', 1, 'benasb');

INSERT INTO test_results VALUES
(DEFAULT, 'true', 1.566, 123, 1, 1),
(DEFAULT, 'false', 3.566, 54, 1, 2),
(DEFAULT, 'true', 2.1, 243, 2, 1),
(DEFAULT, 'true', 1.23, 133, 2, 2),
(DEFAULT, 'true', 2, 112, 3, 3),
(DEFAULT, 'true', 1, 87, 3, 4),
(DEFAULT, 'true', 2, 112, 3, 5),
(DEFAULT, 'true', 1, 87, 3, 6),
(DEFAULT, 'true', 1.64, 456, 4, 1),
(DEFAULT, 'true', 1.42, 333, 4, 2),
(DEFAULT, 'true', 1.42, 424, 5, 1),
(DEFAULT, 'true', 0.42, 321, 5, 2);

-- Show initial data
SELECT * FROM users;
SELECT * FROM problems;
SELECT * FROM test_cases;
SELECT * FROM solutions;
SELECT * FROM test_results;

-- Refresh materialized view
REFRESH MATERIALIZED VIEW leaderboard_solutions_monthly;