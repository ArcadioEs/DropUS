# DropUS

Application created to help students organize their work, by helping in communication, knowledge transfers and so on.

**How to use**
* Make sure *Apache Maven* is installed
* Fill database connection: ***DropUS**/src/main/resources/application.properties*
    * By default H2 database connection is provided, so nothing has to be changed. The thing     to remember is, that after every server startup, data will be destroyed. Good for test       purposes and fast enough, since no database schema needs to be provided.
    * While using H2 Database logging is case sensitive, so even though user can register with lowercase, username will be automatically capitalised, therefore while logging, capitalised login needs to be provided
* In *application.properties* provide absolute path for files storage somewhere on disk. Provided folder does not have to exist, application will create it
* Provide maximum size of file as well
* Launch terminal
* Go to root directory of a project
* Execute command "*mvn clean install spring-boot:run*"
* Go to *localhost:8080* in your Internet browser
