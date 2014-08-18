-- kill the entire database(!)
DROP ALL OBJECTS;

-- create entities
CREATE TABLE IF NOT EXISTS User (
  id INT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) UNIQUE NOT NULL,
  name VARCHAR(255),
  password VARCHAR(255) NOT NULL,
);

CREATE TABLE IF NOT EXISTS Tab (
  id INT PRIMARY KEY AUTO_INCREMENT,
  key VARCHAR(16) UNIQUE NOT NULL,
  name VARCHAR(255) NOT NULL,
);

CREATE INDEX IdxKey ON Tab(key);

CREATE TABLE IF NOT EXISTS Tab_User (
  tabId INT NOT NULL REFERENCES Tab(id) ON DELETE CASCADE ON UPDATE CASCADE,
  userId INT REFERENCES User(id) ON DELETE SET NULL ON UPDATE CASCADE,
  localName VARCHAR(255),
);

CREATE TABLE IF NOT EXISTS Transaction (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  tabId INT NOT NULL REFERENCES Tab(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  date DATE NOT NULL,
  timestamp TIMESTAMP NOT NULL,
  description VARCHAR(255) NOT NULL,
  type VARCHAR(30) NOT NULL CHECK (type IN ('SHARED', 'DIRECT'))
);

CREATE TABLE IF NOT EXISTS Contribution (
	transactionId BIGINT NOT NULL REFERENCES Transaction(id) ON DELETE CASCADE ON UPDATE CASCADE,
	participant VARCHAR(255) NOT NULL,
	amount DECIMAL(20,2) NOT NULL,
);


-- create test data
INSERT INTO User VALUES (NULL, 'tilman@example.com', 'Tilman', 'pw'), (NULL, 'martin@example.com', 'Martin', 'pw'), (NULL, 'user3@example.com', 'User 3', 'pw'), (NULL, 'user4@example.com', 'User 4', 'pw');
INSERT INTO Tab VALUES (NULL, 'c5nq1ayy1g72pbwq', 'Badminton'), (NULL, 'p66a5hd45s861ya5', 'Skatgruppe');
INSERT INTO Tab_User VALUES (1, 1, NULL), (1, 2, 'xmartin'), (1, 3, NULL), (1, 4, 'Userrrr4'), (2, 1, NULL), (2, 2, NULL), (2, 3, NULL);
INSERT INTO Transaction VALUES
 (NULL, 1, '2014-06-16', '2014-06-16 14:15:08', 'Schlägerset', 'SHARED'),
 (NULL, 1, '2014-06-18', '2014-06-19 11:02:05', 'Badminton-Netz', 'SHARED'),
 (NULL, 2, '2014-06-10', '2014-06-10 08:46:05', 'Skatkarten', 'SHARED'),
 (NULL, 2, '2014-06-10', '2014-06-10 08:49:18', 'Mischautomat', 'SHARED'),
 (NULL, 1, '2014-06-20', '2014-06-20 19:04:07', 'Eintritt Badminton-Halle', 'DIRECT');
INSERT INTO Contribution VALUES
 (1, 'Tilman', -119.95),
 (1, 'Onkel Jürgen', -10.00),
 (2, 'Martin', -169.95),
 (3, 'Martin', -8.80),
 (4, 'Tilman', -20.00),
 (4, 'User 3', -49.95),
 (5, 'Tilman', -8.00),
 (5, 'Martin', -10.00);
 
 
-- clean database file and close database
SHUTDOWN COMPACT;