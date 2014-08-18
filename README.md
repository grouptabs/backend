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
access tab with {tab key}  
(a random 16-character string is stored alongside the tab ID as key for external access)

PUT  
change tab properties (tab name)

POST  
not allowed

DELETE  
delete tab for user


### /tabs/{tab ID}/transactions[?since=YYYY-MM-DD]
&rarr; better use paging parameters "from" and "to"

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

### Run Create Script
`java -cp src/main/resources/h2-1.3.176.jar org.h2.tools.RunScript -url jdbc:h2:grouptabs -script recreate-database.sql`
