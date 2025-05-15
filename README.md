# C2: *mvn-jdbc* - Working with Databases

[*Java Database Connectivity (JDBC)*](https://www.tutorialspoint.com/jdbc/index.htm)
is a *Java API* to access tabular data stored in
[*Relational Databases*](https://en.wikipedia.org/wiki/Relational_database).

We use a light-weight,
[*embedded relational database*](https://en.wikipedia.org/wiki/Embedded_database)
named
[*H2*](https://www.h2database.com/html/main.html)
in this assignment.
*Embedded* means that the database does not operate as a separate, independent
[*database server*](https://phoenixnap.com/kb/what-is-a-database-server),
but is part (*embedded*) of the application.

*H2* is implemented in Java and hence can be packaged as *.jar* files that are
distributed through the central
[*Maven Repository/H2*](https://mvnrepository.com/artifact/com.h2database/h2).
Alternative embedded databases for Jave are: *Derby*, *HSQLDB* or *DB4O*.


Challenges:

1. [*Set-up Project as Branch in mvn-fun*](#1-set-up-project-as-branch-in-mvn-fun)

1. [*Create pom.xml*](#2-create-pomxml)

1. [*Build & Run the Project*](#3-build--run-the-project)

1. [*Understand Customer Record and CUSTOMER table*](#4-understand-customer-record-and-customer-table)

1. [*Create a CustomerRepository*](#5-create-a-customerrepository)

1. [*Refactor and Complete the CustomerRepository*](#6-refactor-and-complete-the-customerrepository)

1. [*Refactor into Generic Interface CrudRepository<T,ID>*](#7-refactor-into-generic-interface-crudrepositorytid)



&nbsp;

## 1 Set-up Project as Branch in *mvn-fun*

Recall the [*mvn-fun*](https://github.com/sgra64/mvn-fun)
project and create a new branch: `mvn-jdbc` off the empty root commit.

The project directory of the new branch should be empty (based on the empty commit).

Add [*.gitignore*](https://github.com/sgra64/mvn-fun/blob/mvn-jdbc/.gitignore) and
commit with message: `add .gitignore`.

Create a `src` structure with a file `Application.java` that has a *main()* - function:

```sh
# create path
mkdir -p src/main/java/freerider/reservations/jdbc/application

# create empty file: Application.java
touch src/main/java/freerider/reservations/jdbc/application/Application.java

# validate the structure
find src
```
```
src
src/main
src/main/java
src/main/java/freerider
src/main/java/freerider/reservations
src/main/java/freerider/reservations/jdbc
src/main/java/freerider/reservations/jdbc/application
src/main/java/freerider/reservations/jdbc/application/Application.java
              ^^^^^^^^^^^^^^^^^^^^^^|^^^^
              group-id               artifact-id
```



&nbsp;

## 2 Create *pom.xml*

Create a file *pom.xml* and adjust:

- `<groupid>`,

- `<artifactid>` and

- `<version>0.1.0-SNAPSHOT</version>`

for the created stucture in `src`.

Fill in the *GAV-coordinates:*

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>

  <groupId>freerider.reservations</groupId>
  <artifactId>jdbc</artifactId>
  <version>0.1.0-SNAPSHOT</version>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <exec.mainClass>
        freerider.reservations.jdbc.application.Application
    </exec.mainClass>
  </properties>

  <dependencies>
    <!-- no dependencies yet -->
  </dependencies>

  <build>
    <pluginManagement>
      <plugins>
        <plugin>
          <artifactId>maven-jar-plugin</artifactId>
          <version>3.4.2</version>
          <configuration>
            <archive>
              <manifest>
                  <addClasspath>true</addClasspath>
                  <classpathPrefix>lib/</classpathPrefix>
                  <mainClass>${exec.mainClass}</mainClass>
              </manifest>
            </archive>
          </configuration>
        </plugin>
      </plugins>
    </pluginManagement>
  </build>
</project>
```

&nbsp;

Fill in code for `Application.java` that appempts to open a connection to the embedded
*H2 database* using connection URL: `"jdbc:h2:mem:freerider"` with login user: `"sa"`
(for *system administrator*) and open password: `""`:

```java
package freerider.reservations.jdbc.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Main Application class.
 */
public class Application {

    /**
     * Program execution starts here.
     * @param args arguments passed from the command line
     */
    public static void main(String[] args) {
        System.out.println(String.format("Hello \"%s\" example!",
            Application.class.getName().replace(".application.Application", "")));

        /*
         * Provide database connection information.
         */
        String db_url = "jdbc:h2:mem:freerider";
        // String db_url = "jdbc:h2:file:./.database/freerider.h2";
        String db_user = "sa";
        String db_password = "";

        try(
            // try to open database connection
            Connection dbcon = DriverManager.getConnection(db_url, db_user, db_password)
        ) {
            System.out.println("Database connection opened");

        } catch (SQLException e) {
            System.out.println(String.format("error opening database connection(%s, %s, %s): \"%s\"",
                db_url, db_user, db_password, e.getMessage()));
        }
    }
}
```



&nbsp;

## 3 *Build* & *Run* the Project

Build the project:

```sh
mvn compile                     # compile code

find src target                 # show compiled files
```
```
src
src/main
src/main/java
src/main/java/freerider
src/main/java/freerider/reservations
src/main/java/freerider/reservations/jdbc
src/main/java/freerider/reservations/jdbc/application
src/main/java/freerider/reservations/jdbc/application/Application.java
target
target/classes
target/classes/freerider
target/classes/freerider/reservations
target/classes/freerider/reservations/jdbc
target/classes/freerider/reservations/jdbc/application
target/classes/freerider/reservations/jdbc/application/Application.class
```

In order to run the code, *CLASSPATH* must be set.
"*Source*" the project using the [.env.sh](.env.sh) script:

```sh
source .env.sh                          # source the project
```

Output shows that a *.classpath-file* has been created and the *CLASSPATH*
variable has been set:

```
project environment has been set up:
 - created: .classpath-file
 - set CLASSPATH: "target/classes;"
```

```sh
cat .classpath-file                     # show content of '.classpath-file'
echo $CLASSPATH                         # show CLASSPATH variable
```

Running the program will throw an exception telling that no suitable database
driver was found for the connection: "*jdbc:h2:mem:freerider*".

```sh
# run the program
java -cp "$CLASSPATH" freerider.reservations.jdbc.application.Application
```

```
error opening database connection(jdbc:h2:mem:freerider, sa, ):
"No suitable driver found for jdbc:h2:mem:freerider"
```

Add the driver for *H2* as *dependency* to *pom.xml*. You find the driver
in the central *Maven Repository* for the
[*H2 database*](https://mvnrepository.com/artifact/com.h2database/h2).

Re-build the project with the new dependency:

1. Re-source the project sind dependencies have been updated in `pom.xml`:
    ```sh
    source .env.sh                      # re-source the project
    ```

    Output shows *CLASSPATH* with the new dependency added:

    ```
    project environment has been set up:
     - created: .classpath-file
     - set CLASSPATH: "target/classes;C:\Users\svgr2\.m2\repository\com\h2database\h2\2.3.232\h2-2.3.232.jar
    ```

1. Re-build the project: `mvn clean compile package`


Running the program should now succeed. Try various possibilities:

```sh
# run the program with CLASSPATH passed through the Shell process
java freerider.reservations.jdbc.application.Application

# run the program with @-options file
java -cp @.classpath-file freerider.reservations.jdbc.application.Application

# run program with maven, -q suppresses maven messages
mvn exec:java -q
```
<!-- 
```sh
# package compiled code to '.jar' in 'target'
mvn package

# run the packaged jar file
java -jar target/jdbc-0.1.0-SNAPSHOT.jar
```
 -->
Output of the program for all executions:

```
Hello "freerider.reservations.jdbc" example!
Database connection open.
```


&nbsp;

If the program is working, commit the working state of the project:

```sh
git add pom.xml
git add src/main/java/freerider/reservations/jdbc/application/Application.java

git commit -m "add pom.xml, Application.java"
```

Show commit log of branch *mvn-jdbc :*

```sh
bdb99a8 (HEAD -> mvn-jdbc) add pom.xml, Application.java
34c43bd add .gitignore
a57a0d4 (tag: root) root commit (empty)
```



&nbsp;

## 4 Understand *Customer Record* and *CUSTOMER table*

Understand the concept of a
[*Java Record*](https://www.baeldung.com/java-record-keyword).

Repeat and understand the concept of a database
[*Entity*](https://en.wikipedia.org/wiki/Entity%E2%80%93relationship_model#Components).


Create a file `Customer.java` with the essential definitions for
entity type *Customer :*

```java
package freerider.reservations.jdbc.application;

/**
 * Entity type of a {@link Customer} object associated with records stored in
 * a <i>CUSTOMER</i> table in a database.
 */
public record Customer(

    /**
     * Primary key as customer identity, assigned by the database
     * when a record is created in the database.
     */
    long id,

    /**
     * First name attribute of a customer.
     */
    String firstName,

    /**
     * Last name attribute of a customer.
     */
    String lastName

) {

    /**
     * Class-level method to return the name of associated database table.
     * @return name of the database table for customer records.
     */
    public static String tableName() { return "CUSTOMER"; }

    /**
     * Class-level method to return the SQL to create the table.
     */
    public static String schema() { return
        "CREATE TABLE CUSTOMER (" +
        "  ID INTEGER NOT NULL AUTO_INCREMENT, " +
        "  FIRSTNAME VARCHAR(255), " +
        "  LASTNAME VARCHAR(255), " +
        "  PRIMARY KEY ( ID )" +
        ")";
    }

    /**
     * Constructor of an object unbound in the database (illegal negative id).
     * @param firstName first name attribute of a customer
     * @param lastName last name attribute of a customer
     */
    public Customer(String firstName, String lastName) {
        this(-1L, firstName, lastName);
    }
}
```

&nbsp;

Answer questions:

1. Why is *Java Record* preferred for entity types over a Java class?

1. What are the essential properties of a *Java Record*?

1. What is a *primary key?*

1. What is a *foreign key?*


&nbsp;

Create or install class
[*DBSchemaCreator.java*](https://github.com/sgra64/mvn-fun/blob/mvn-jdbc/src/main/java/freerider/reservations/jdbc/application/DBSchemaCreator.java)
into the project as a singleton component class:

```java
package freerider.reservations.jdbc.application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * Singleton component class that probes whether a schema (tables)
 * is present in a database and creates the schema if not.
 */
public class DBSchemaCreator {

    /**
     * Singleton {@link DBSchemaCreator} instance.
     */
    private static DBSchemaCreator dbSchema = null;

    /**
     * List of pairs: schema name and schema definition to create
     * in a database.
     */
    private final List<String> tableSQLinCreationOrder = List.of(
        Customer.tableName(), Customer.schema()
    );

    /**
     * Private constructor to avoid external instance creation as
     * part of the Singleton pattern.
     */
    private DBSchemaCreator() { }

    /**
     * Public getter method of {@link DBSchemaCreator} instance as
     * part of the Singleton pattern.
     * @return {@link DBSchemaCreator} singleton instance
     */
    public static DBSchemaCreator getInstance() {
        if(dbSchema==null) {
            dbSchema = new DBSchemaCreator();
        }
        return dbSchema;
    }

    /**
     * Probe that tables of the schema are present and create the ones
     * that are not.
     * @param dbcon open database connection
     * @return list of names of created tables
     */
    public List<String> probeCreateSchema(Connection dbcon) {
        List<String> tablesFound = new ArrayList<>();
        List<String> tablesCreated = new ArrayList<>();
        try(
            Statement stmt = dbcon.createStatement();
        ) {
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            while(rs.next()){
                var table = rs.getString(1);
                System.out.println(String.format(" --> found table: %s", table));
                tablesFound.add(table);
            }
            for(int i=0; i < tableSQLinCreationOrder.size()-1; i+=2) {
                var tableName = tableSQLinCreationOrder.get(i);
                if( ! tablesFound.contains(tableName)) {
                    var sql = tableSQLinCreationOrder.get(i+1);
                    try {
                        stmt.executeUpdate(sql);
                        tablesCreated.add(tableName);
                    // 
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        tablesFound.clear();
        // 
        return tablesCreated;
    }
}
```

&nbsp;

Answer questions:

1. What is a *Singleton*?

1. What are the implementation properties of a *Singleton*?

1. What is the difference between a *lazy* and a *strict Singleton*?

1. Which variation was implemented in `DBSchemaCreator.java` and why?



&nbsp;

## 5 Create a *CustomerRepository*

A [*Repository*](https://docs.spring.io/spring-data/data-commons/docs/current/api/org/springframework/data/repository/Repository.html)
(specifically the
[*CrudRepository*](https://docs.spring.io/spring-data/data-commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
)
is an abstraction introduced by the *Spring Boot* framework to wrap
a database table with a well-defined interface of methods to:

- *(C)reate,*

- *(R)ead,*

- *(U)pdate* and

- *(D)elete*

records in a database.

New class `CustomerRepository.java` implements the following methods:

```java
package freerider.reservations.jdbc.application;

public class CustomerRepository {

    /**
     * Public getter method of {@link CustomerRepository} instance as
     * part of the Singleton pattern.
     * @param dbcon open database connection
     * @return {@link CustomerRepository} singleton instance
     */
    public static CustomerRepository getInstance(Connection dbcon) { ... }

    
    /**
     * Method to save (insert) a new {@link Customer} object in the associated
     * database table with a new {@code id} assigned by the database.
     * @param firstName first name attribute of a customer
     * @param lastName last name attribute of a customer
     * @return {@link Customer} object with {@code id} assigned by
     *          the database or empty if object was not inserted
     */
    Optional<Customer> save(String firstName, String lastName) { ... }

    
    /**
     * Method to retrieve an {@link Customer} object from the associated
     * database table by a provided {@code id}.
     * @param id {@code id} of the object to retrieve from the database
     * @return retrieved object if {@code id} was found or empty result
     */
    public Optional<Customer> findById(long id) { ... }

    
    /**
     * Method to retrieve all {@link Customer} objects from the associated
     * database table.
     * @return all objects retreived from the associated database table
     */
    public Iterable<Customer> findAll() { ... }

}
```


&nbsp;

Install class
[*CustomerRepository.java*](https://github.com/sgra64/mvn-fun/blob/mvn-jdbc/src/main/java/freerider/reservations/jdbc/application/CustomerRepository.java)
in the project.


Supplement changes in class
[*Application.java*](https://github.com/sgra64/mvn-fun/blob/mvn-jdbc/src/main/java/freerider/reservations/jdbc/application/Application.java) :


```java
package freerider.reservations.jdbc.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.StreamSupport;


/**
 * Main Application class.
 */
public class Application {

    /**
     * Program execution starts here.
     * @param args arguments passed from the command line
     */
    public static void main(String[] args) {
        System.out.println(String.format("Hello \"%s\" example!",
            Application.class.getName().replace(".application.Application", "")));

        /*
         * Provide database connection information.
         */
        String db_url = "jdbc:h2:mem:freerider";
        // String db_url = "jdbc:h2:file:./.database/freerider.h2";
        String db_user = "sa";
        String db_password = "";

        try(
            // try to open database connection
            Connection dbcon = DriverManager.getConnection(db_url, db_user, db_password)
        ) {
            var dbSchemaCreator = DBSchemaCreator.getInstance();
            var tablesCreated = dbSchemaCreator.probeCreateSchema(dbcon);
            var msg = tablesCreated.size()==0? "opened DB: all tables found" :
                String.format(" --> opened DB: %d tables created", tablesCreated.size());
            System.out.println(msg);
            // 
            var customerRepository = CustomerRepository.getInstance(dbcon);
            if(tablesCreated.size() > 0) {
                // insert records if table CUSTOMER was newly created
                if(tablesCreated.contains(Customer.tableName())) {
                    customerRepository.save("Eric", "Meyer");
                    customerRepository.save("Tony", "Allister");
                    customerRepository.save("Sandra", "Ohlstadt");
                    customerRepository.save("Erica", "Gronemann");
                    customerRepository.save("Khaleed", "Samadi");
                    customerRepository.save("Igor", "Medwedev");
                }
            }

            var customers = customerRepository.findAll();
            StreamSupport.stream(customers.spliterator(), false)
                // print only Customer objects with even id
                // .filter(customer -> customer.id() % 2 == 0)
                // 
                // format and report customers
                .map(customer -> String.format(" --> %s", customer))
                .forEach(System.out::println);

        } catch (SQLException e) {
            System.out.println(String.format("error opening database connection(%s, %s, %s): \"%s\"",
                db_url, db_user, db_password, e.getMessage()));
        }
    }
}
```

Run the code:

```
Hello "freerider.reservations.jdbc" example!
 --> opened DB: 1 tables created
inserted customer "Eric Meyer"  with id: 1 (1 rows)
inserted customer "Tony Allister"       with id: 2 (1 rows)
inserted customer "Sandra Ohlstadt"     with id: 3 (1 rows)
inserted customer "Erica Gronemann"     with id: 4 (1 rows)
inserted customer "Khaleed Samadi"      with id: 5 (1 rows)
inserted customer "Igor Medwedev"       with id: 6 (1 rows)
 --> Customer[id=1, firstName=Eric, lastName=Meyer]
 --> Customer[id=2, firstName=Tony, lastName=Allister]
 --> Customer[id=3, firstName=Sandra, lastName=Ohlstadt]
 --> Customer[id=4, firstName=Erica, lastName=Gronemann]
 --> Customer[id=5, firstName=Khaleed, lastName=Samadi]
 --> Customer[id=6, firstName=Igor, lastName=Medwedev]
```



&nbsp;

## 6 Refactor and Complete the *CustomerRepository*

Refactor *CustomerRepository.java* into an interface and an implementation class
*CustomerRepositoryImpl.java*.

Complete the interface with methods of the
[*CrudRepository*](https://docs.spring.io/spring-data/data-commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
interface and implement methods.



&nbsp;

## 7 Refactor into Generic Interface *CrudRepository<T,ID>*

Refactor interface *CustomerRepository.java* into a generic interface
*CrudRepository<T,ID>* like *Spring Boot's*
[*CrudRepository*](https://docs.spring.io/spring-data/data-commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)

Implement class *CustomerRepositoryImpl.java* as:

```java
class CustomerRepositoryImpl implements CrudRepository<Customer,Long> {
    ...
}
```


<!-- 

<img src="https://raw.githubusercontent.com/sgra64/mvn-fun/refs/heads/markup/img/git-log-after-merge.png" width="600"/> -->

