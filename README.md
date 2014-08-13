grouptabs-backend
=================

build
-----

* `git clone https://github.com/grouptabs/backend.git`
* `mvn package`
* `java -jar target\grouptabs-backend-0.0.1-SNAPSHOT.jar server config.yaml`
	* a list of the available REST endpoints will be shown
* access endpoints, e.g. `http://localhost:8080/tabs`
* run `mvn eclipse:eclipse` before importing the project into your eclipse workbench


target interface
----------------
### /tabs

GET  
list of all accessible tabs for the user  
empty list while not authenticated (better HTTP error code? --} check)

PUT  
not allowed

POST  
create new tab and return tab ID

DELETE  
not allowed (better allow DELETE if tab has no transactions?)


### /tabs/{tab ID}

GET  
access tab with {tab ID}  
~~{tab ID} is int or long (to be decided) encoded as ascii string (like doodle links)~~  
&rarr; a random 16-character string is stored alongside the tab ID as key for external access

PUT  
change tab properties (tab name)

POST  
not allowed

DELETE  
delete tab for user


### /tabs/{tab ID}/transactions[?since=YYYY-MM-DD]
&rarr; better user paging parameters "from" and "to"

GET  
get all transactions in tab

PUT  
not allowed

POST  
create new transaction in tab

DELETE  
not allowed


### /tabs/{tab ID}/transactions/{transaction ID}

GET  
get single transaction with {transaction ID}

PUT  
modify transaction with {transaction ID}

POST  
not allowed

DELETE  
delete transaction with {transaction ID}


### /users

GET  
get all users

PUT  
not allowed

POST  
create new user and return ID

DELETE  
not allowed


### /users/{user ID}

GET  
get user data for user with {user ID}

PUT  
modify user with {user ID}

POST  
not allowed

DELETE  
delete user with {user ID}


Database
--------
### Start H2 Database and SQL Client
`java -cp src/main/resources/h2-1.3.176.jar org.h2.tools.Server`

JDBC URL: `jdbc:h2:grouptabs`

(leave everything else on default: Embedded mode, Driver org.h2.Driver, User Name sa, no password)

### Create Script
```sql
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

CREATE TABLE IF NOT EXISTS Tab_User (
  tabId INT NOT NULL REFERENCES Tab(id) ON UPDATE CASCADE,
  userId INT NOT NULL REFERENCES User(id) ON UPDATE CASCADE,
  localName VARCHAR(255),
);

CREATE TABLE IF NOT EXISTS Transaction (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  tabId INT NOT NULL REFERENCES Tab(id) ON UPDATE CASCADE,
  date DATE NOT NULL,
  timestamp TIMESTAMP NOT NULL,
  description VARCHAR(255) NOT NULL,
  type VARCHAR(30) NOT NULL CHECK (type IN ('SHARED', 'DIRECT'))
);

CREATE TABLE IF NOT EXISTS Participant (
	transactionId BIGINT NOT NULL REFERENCES Transaction(id) ON UPDATE CASCADE,
	userId INT NOT NULL REFERENCES User(id) ON UPDATE CASCADE,
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
INSERT INTO Participant VALUES
 (1, 1, -119.95),
 (2, 2, -169.95),
 (3, 2, -8.80),
 (4, 1, -20.00),
 (4, 3, -49.95),
 (5, 1, -8.00),
 (5, 2, -10.00);
 
 
 -- clean database file and close database
 -- SHUTDOWN COMPACT;
```

