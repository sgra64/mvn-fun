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

1. [*Create Customer Records*](#5-create-customer-records)



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
[*DBSchemaBuilder.java*](https://github.com/sgra64/mvn-fun/blob/mvn-jdbc/src/main/java/freerider/reservations/jdbc/application/DBSchemaBuilder.java)
into the project as a singleton component class:

```java
package freerider.reservations.jdbc.application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


/**
 * Singleton component class that probes whether a schema (tables)
 * is present in a database and creates the schema if not.
 */
public class DBSchemaBuilder {

    /**
     * Name of CUSTOMER table.
     */
    public final static String CUSTOMER_Table = "CUSTOMER";

    /**
     * SQL-Schema to create CUSTOMER table.
     */
    public final static String CUSTOMER_Schema =
        "CREATE TABLE if not exists CUSTOMER (" +
        "  ID INT not null auto_increment, " +
        "  FIRSTNAME VARCHAR(255), " +
        "  LASTNAME VARCHAR(255), " +
        "  PRIMARY KEY ( ID )" +
        ")";

    /**
     * Singleton {@link DBSchemaBuilder} instance.
     */
    private static DBSchemaBuilder dbSchema = null;

    /**
     * List of pairs: schema name and schema definition to create
     * in a database.
     */
    private final List<String> tableSQLinCreationOrder = List.of(
        "CUSTOMER", CUSTOMER_Schema
    );

    /**
     * Private constructor to avoid external instance creation as
     * part of the Singleton pattern.
     */
    private DBSchemaBuilder() { }

    /**
     * Public getter method of {@link DBSchemaBuilder} instance as
     * part of the Singleton pattern.
     * @return {@link DBSchemaBuilder} singleton instance
     */
    public static DBSchemaBuilder getInstance() {
        if(dbSchema==null) {
            dbSchema = new DBSchemaBuilder();
        }
        return dbSchema;
        // return java.util.Optional.ofNullable(dbSchema).orElse(dbSchema = new DBSchema());
    }

    /**
     * Probe that tables of the schema are present and create the ones
     * that are not.
     * @param dbcon open database connection
     * @return list of names of created tables
     */
    public List<String> probeCreateSchema(
        Connection dbcon,
        Supplier<Integer> loadCUSTOMER
    ) {
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
        // invoke CUSTOMER data load callout
        for(String tableCreated : tablesCreated) {
            switch(tableCreated) {
            case "CUSTOMER": if(loadCUSTOMER != null) loadCUSTOMER.get(); break;
            }
        }
        var msg = tablesCreated.size()==0? "opened DB: all tables found" :
            String.format(" --> opened DB: %d tables created", tablesCreated.size());
        System.out.println(msg);
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

1. Which variation was implemented in `DBSchemaBuilder.java` and why?



&nbsp;

## 5 Create *Customer* Records

Supplement changes in class
[*Application.java*](https://github.com/sgra64/mvn-fun/blob/mvn-jdbc/src/main/java/freerider/reservations/jdbc/application/Application.java) :


```java
package freerider.reservations.jdbc.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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

        // try to open database connection
        try(
            final Connection dbcon = DriverManager.getConnection(db_url, db_user, db_password)
        ) {
            System.out.println("Database connection open.");
            
            // load schema into database, if schema does not exist and
            // load data into database, if schema was created
            DBSchemaBuilder.getInstance().probeCreateSchema(dbcon,
                () -> load_CUSTOMER_Data(dbcon)
            );

            // read records from CUSTOMER table
            try(
                Statement stmt = dbcon.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM CUSTOMER")
            ) {
                StringBuilder sb = new StringBuilder();
                String hline = String.format("+----+%s+%s+", "-".repeat(21), "-".repeat(21));
                sb.append(String.format("%s\n", hline));
                sb.append(String.format("| %-2s | %-20s| %-20s|\n", "ID", "FIRSTNAME", "LASTNAME"));
                sb.append(String.format("%s\n", hline));
                // 
                // iterate over ResultSet of returned CUSTOMER records
                while(rs.next()) {
                    long id = rs.getLong("ID");
                    String firstName = rs.getString("FIRSTNAME");
                    String lastName = rs.getString("LASTNAME");
                    // 
                    String line = String.format("| %2d | %-20s| %-20s|\n", id, firstName, lastName);
                    sb.append(line);
                }
                sb.append(String.format("%s\n", hline));
                System.out.println(sb.toString());
            // 
            } catch (SQLException e) {
                System.out.println(String.format("Error reading all records from database CUSTOMER"));
            }
        // 
        } catch (SQLException e) {
            System.out.println(String.format("Error opening database connection(%s, %s, %s): \"%s\"",
                db_url, db_user, db_password, e.getMessage()));
        }
    }

    /**
     * Load CUSTOMER records into corresponding table.
     * @param dbcon open database connection
     * @return number records loaded
     */
    private static int load_CUSTOMER_Data(Connection dbcon) {
        int numRowsInserted = 0;
        // INSERT customers as one transaction, if CUSTOMER table is empty
        try(PreparedStatement ps = dbcon.prepareStatement(
            // 
            "INSERT INTO CUSTOMER(FIRSTNAME, LASTNAME) VALUES " +
                "('Eric', 'Meyer'), ('Tony', 'Allister'), ('Sandra', 'Ohlstadt'), " +
                "('Erica', 'Gronemann'), ('Khaleed', 'Samadi'), ('Igor', 'Medwedev')",
                Statement.RETURN_GENERATED_KEYS
        )) {
            // issue INSERT transaction
            numRowsInserted = ps.executeUpdate();
            // 
            // obtain result set with ID attributes assigned by database
            try(ResultSet rs = ps.getGeneratedKeys()) {
                while(rs.next()) {
                    long id = rs.getLong(1);
                    System.out.println(String.format("inserted customer with id: %d (%d rows)",
                        id, numRowsInserted));
                }
            } catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return numRowsInserted;
    }
}
```

Run the code:

```
Hello "freerider.reservations.jdbc" example!
Database connection open.
inserted customer with id: 1 (6 rows)
inserted customer with id: 2 (6 rows)
inserted customer with id: 3 (6 rows)
inserted customer with id: 4 (6 rows)
inserted customer with id: 5 (6 rows)
inserted customer with id: 6 (6 rows)
 --> opened DB: 1 tables created
+----+---------------------+---------------------+
| ID | FIRSTNAME           | LASTNAME            |
+----+---------------------+---------------------+
|  1 | Eric                | Meyer               |
|  2 | Tony                | Allister            |
|  3 | Sandra              | Ohlstadt            |
|  4 | Erica               | Gronemann           |
|  5 | Khaleed             | Samadi              |
|  6 | Igor                | Medwedev            |
+----+---------------------+---------------------+
```


<!-- 

<img src="https://raw.githubusercontent.com/sgra64/mvn-fun/refs/heads/markup/img/git-log-after-merge.png" width="600"/> -->
