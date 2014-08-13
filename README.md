grouptabs-backend
=================

* `git clone https://github.com/grouptabs/backend.git`
* `mvn package`
* `java -jar target\grouptabs-backend-0.0.1-SNAPSHOT.jar server config.yaml`
	* a list of the available REST endpoints will be shown
* access endpoints, e.g. `http://localhost:8080/tabs`
* do not forget to run `mvn eclipse:eclipse` before importing the project into your workbench
