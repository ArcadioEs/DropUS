# DropUS

Application created to help students organize their work, by helping in communication, knowledge transfers and so on.

**How to use**
* Make sure *Apache Maven* is installed
* Fill database connection: ***DropUS**/src/main/resources/application.properties*
    * By default H2 database connection is provided, so nothing has to be changed. The thing     to remember is, that after every server startup, data will be destroyed. Good for test       purposes and fast enough, since no database schema needs to be provided.
* Launch terminal
* Go to root directory of a project
* Execute command "*mvn clean install*"
* Go to ***DropUS**/target* directory
* Start server by executing command "*java -jar DropUS-1.0-SNAPSHOT.jar*"
* Go to *localhost:8080* in your Internet browser
* This is temporary solution, but to initialize admin user, please go to *localhost:8080/init* and log with credentials: admin/nimda
