<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
<!-- D1 (SE-2)
-->
# Branch: *mvn-jdbc*

<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
The assignment demonstates the basic use of database in *Java* using the
[*JDBC (Java Database Connectivity)*](https://en.wikipedia.org/wiki/Java_Database_Connectivity)
interface.

The assignment is carried out in project
[*mvn-fun*](https://github.com/sgra64/mvn-fun)
from the first assignment.

Besides *JDBC*, other basic mechanisms are introduced that are often used in
conjunction with databases:

- Code-Injection with *Lombok*.

- The *Builder Pattern* (with *Lombok*).

- *Logging* with *log4j*.

- Work with embedded in-memory and persistent Database.

- Creating a *Database Schema* with the corresponding *Java Data Model*
    for the car reservation system.


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

---

Steps:

1. [Create branch *mvn-jdbc*](#1-create-branch-jdbc)

1. [Branch Structure and *Project Build*](#2-branch-structure-and-project-build)

1. [Add new Dependencies](#3-add-new-dependencies)

1. [Code-Injection with *Lombok*](#4-code-injection-with-lombok)

1. [*Builder Pattern* with *Lombok*](#5-builder-pattern-with-lombok)

1. [*Logging* with *log4j*](#6-logging-with-log4j)

1. [Embedded in-memory Database *H2*](#7-embedded-in-memory-database-h2)

1. [Persistent Database](#8-persistent-database)

1. [Entity: *"Customer"*](#9-entity-customer)

1. [Database *Query* and *RowMapper*](#10-database-query-and-rowmapper)

1. [Full *FreeRider* Database Schema](#11-full-freerider-database-schema)


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 1. Create branch *mvn-jdbc*

Create a new branch *"mvn-jdbc"* in project
[*mvn-fun*](https://github.com/sgra64/mvn-fun).

Create the branch off the commit at which git-modules had been committed:

```sh
git log --oneline
```
```
d6c9bc5 (origin/main, main) Merge branch 'main' of gitlab.bht-berlin.de:sgraupner/mvn-fun_111
8fa5d61 Initial commit
a63a254 add .git-modules        <-- base of new branch 'mvn-jdbc'
a984e53 add pom.xml, src
cd4acc8 add .gitignore
d77bc6f (tag: root) root commit (empty)
```

Switch to the new branch and check it has been branched correctly:

```sh
git log --oneline
```

*HEAD* now points at the new branch *mvn-jdbc*:

```
a63a254 (HEAD -> mvn-jdbc) add .git-modules     <-- base of new branch 'mvn-jdbc'
a984e53 add pom.xml, src
cd4acc8 add .gitignore
d77bc6f (tag: root) root commit (empty)
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 2. Branch Structure and *Project Build*

In [pom.xml](pom.xml), adjust *GAV*-coordinates and add to properties:

```xml
  <groupId>mvn.jdbc</groupId>
  <artifactId>mvn-jdbc</artifactId>
  <version>1.0-SNAPSHOT</version>

  <properties>
    ...
    <exec.mainClass>mvn.jdbc.application.Application</exec.mainClass>
  </properties>
```

Adjust the `src` directory hierarchy accordingly:

```sh
find src
```
```
src
src/main
src/main/java
src/main/java/mvn
src/main/java/mvn/jdbc
src/main/java/mvn/jdbc/application
src/main/java/mvn/jdbc/application/Application.java
src/test
src/test/java
src/test/java/mvn
src/test/java/mvn/jdbc
src/test/java/mvn/jdbc/application
src/test/java/mvn/jdbc/application/ApplicationTest.java
```

Perform a *"clean project build"* (`-q` runs maven quietly):

```sh
mvn clean compile exec:java -q
```
```
Hello World!
```

Source the project:

```sh
source .env/env.sh
```

This makes additional commands available:

```sh
- mkcp          # create "make" classpath file in '.vscode/cp.txt'

- run           # run application (faster than with: mvn exec:java)

- javadoc       # compile javadoc to 'target/reports/apidocs/index.html'

- delombok      # de-lombok src to 'target/generated-sources/delombok'
- delombok_2    # (needed for lombok later)
```

Run the application:

```sh
run             # run application (faster than with: mvn exec:java)
```
```
Hello world!
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 3. Add new Dependencies

The *jdbc-project* will need new dependencies. Add the following dependencies:

- [*h2-2.4.240*](https://mvnrepository.com/artifact/com.h2database/h2) --
    the embedded database [*H2*](https://www.h2database.com/html/main.html) for Java
    (embedded: the database is implemented in Java and can therefore simply be
     added as a Java dependency).

- [*lombok-1.18.42*](https://mvnrepository.com/artifact/org.projectlombok/lombok) --
    a popular *code-injection* library of project
    [*lombok*](https://projectlombok.org/features) (code injection generates
    *"boilerplate"* code such as constructors, getters and setters for classes).

- [*log4j-core-2.25.2*](https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core)
    and
    [*log4j-api-2.25.2*](https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-api) --
    dependencies of the popular logging framework
    [*log4j*](https://logging.apache.org/log4j/2.x) for Java.


Perform a *"clean project build"*:

```sh
mvn clean compile exec:java -q
```
```
Hello World!
```

Compile and run tests:

```sh
mvn test-compile test
```
```
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

Package and run the *.jar* artifact:

```sh
mvn package

ls -la target

java -jar target/mvn-jdbc-1.0-SNAPSHOT.jar
```
```
--> target/mvn-jdbc-1.0-SNAPSHOT.jar
Hello World!
```

If everything works, commit the changes to the new branch *mvn-jdbc* with
message: `"mvn-jdbc setup"` and show the commit log:

```sh
git commit --oneline
```
```
3bbf119 (HEAD -> mvn-jdbc) mvn-jdbc setup
a63a254 add .git-modules
a984e53 add pom.xml, src
cd4acc8 add .gitignore
d77bc6f (tag: root) root commit (empty)
```

Show the difference that commit made to the previous commit:

```sh
git diff HEAD~1..HEAD --name-status     # show difference between two commits
```

The *"diff"* shows modifications (`M`) in file *pom.xml* and two renamed
files for the adjucted project structure:

```
M     pom.xml
R071  src/main/java/com/mycompany/application/App.java      src/main/java/mvn/jdbc/application/Application.java
R083  src/test/java/com/mycompany/application/AppTest.java  src/test/java/mvn/jdbc/application/ApplicationTest.java
```

Show the differences made in file *pom.xml*:

```sh
git diff HEAD~1..HEAD -- pom.xml        # show difference in 'pom.xml' between two commits
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 4. Code-Injection with *Lombok*

Project [*lombok*](https://projectlombok.org/features) has developed a popular
*code-injection* library to generate and inject *"boilerplate"* code (code
that carries no functionality and is still necessary). Examples are
constructors, getters and setters for classes.

Create class *Customer.java* using
[*lombok annotations*](https://projectlombok.org/features):

```java
package mvn.jdbc.application;

import java.time.LocalDateTime;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * Class of a {@link Customer} entity corresponding to table {@code CUSTOMER}
 * in the database schema:
 * <pre>
 * CREATE TABLE if not exists CUSTOMER (
 *   -- ID INT GENERATED BY DEFAULT AS IDENTITY,  -- MySQL does not support IDENTITY
 *   ID BIGINT AUTO_INCREMENT not null,           -- use AUTO_INCREMENT instead
 *   NAME VARCHAR(60) not null,
 *   FIRSTNAME VARCHAR(60) default null,
 *   CONTACT VARCHAR(60) default null,
 *   STATUS enum('Active', 'InRegistration', 'Terminated') default null,
 *   STATUS_CHANGE TIMESTAMP default null,
 *   PRIMARY KEY (ID)
 * );
 * </pre>
*/
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Accessors(fluent=true, chain=true)
public class Customer {
    private final long id;
    private String name;
    private String firstName;
    private String contact;
    private Status status;
    private LocalDateTime statusChange;

    public enum Status {
        Active, InRegistration, Terminated, Null
        /** Null: default state in the database */
    };
}
```

Although all attributes are private, there are no getter and setter methods
and there is no constructor.

Instead, *lombok* annotations `@Getter`, `@Setter` and `@AllArgsConstructor`
instruct *lombok* to generate those and inject into the class before the
compiler runs.

Annotation `@ToString` generates a *toString()* method that produces
printable output for objects of the class.

Annotation `@Accessors` specifies that the *"fluent"* style of getters and
setters should be used, which is ommitting the *"get"* and the *"set"*
prefixes. Instead of *"getName()"* the method is called just: *"name()"*.
Instead of *"setName(String name)"* the method is called:
*"name(String name)"*.

Accessor *"chain"=true* creates setter methods that can be *"chained"* by
dots.

Add to *main()* in
[*Application.java*](src/main/java/mvn/jdbc/app/Application.java):

```java
package mvn.jdbc.application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import mvn.jdbc.application.Customer.Status;

/**
 * Run the program:
 * - mvn exec:java -q
 */
public class Application {

    public static void main(String[] args) {
        System.out.println("Hello, Customer!");
        // 
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        // 
        Customer c1 = new Customer(100L, "Eric", "Meyer", "eme22@gmail.com", Status.Active, LocalDateTime.parse("2024-06-04 12:35", dtf));
        Customer c2 = new Customer(101L, "Sommer", "Tina", "+49 030 22458 29425", Status.Active, LocalDateTime.parse("2025-10-07 10:28", dtf));
        Customer c3 = new Customer(103L);
        // 
        c3.name("Brinkmann")        // chained setter methods
            .firstName("Tobias")
            .contact("+49 030 662465724")
            .status(Status.Active)
            .statusChange(LocalDateTime.parse("2024-12-28 18:00", dtf));
        // 
        List.of(c1, c2, c3)
            .stream()
            .filter(c -> c.id() > 1)
            // create readable output by the injected toString() method
            .forEach(System.out::println);
    }
}
```

Run the code. Lombok's `@ToString` annotation has added a method to
produce readable output for objects:

```sh
mvn clean compile exec:java -q
```
```
Hello Customer!
Customer(id=100, name=Eric, firstName=Meyer, contact=eme22@gmail.com, status=Active, statusChange=2024-06-04T12:35)
Customer(id=101, name=Sommer, firstName=Tina, contact=+49 030 22458 29425, status=Active, statusChange=2025-10-07T10:28)
Customer(id=103, name=Brinkmann, firstName=Tobias, contact=+49 030 662465724, status=Active, statusChange=2024-12-28T18:00)
```

Commit with message: `"mvn-jdbc lombok demo"` and show the commit log:

```sh
git commit --oneline
```
```
06482c2 (HEAD -> mvn-jdbc) mvn-jdbc lombok demo
3bbf119 mvn-jdbc setup
a63a254 add .git-modules
a984e53 add pom.xml, src
cd4acc8 add .gitignore
d77bc6f (tag: root) root commit (empty)
```

In order to inspect what *lombok* has generated, code can be *"de-lomboked"*:

```sh
# "de-lombok" code from 'src/main' to 'target/src-delomboked'
# 
delombok
```

Inspect what lombok has generated for *Customer.java*. The *"de-lomboked"*
code was created under path *target/src-delomboked*:

```sh
# open path in VSCode or print
cat target/generated-sources/delombok/mvn/jdbc/application/Customer.java
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 5. *Builder Pattern* with *Lombok*

The [*Builder Pattern*](https://refactoring.guru/design-patterns/builder)
is a creational design pattern that constructs complex objects step by step.

In order to *build-the-database*, a new class *Database.java* is created that
implements the *Builder Pattern* using *lombok*:

```java
package mvn.jdbc.application;

import lombok.*;

@Builder
@ToString
public class Database {
    private String db_url;
    private String db_user;
    private String db_password;

}
```

The *Builder*-pattern has:

1. a static class-method: *builder()* that returns a new instance of a
    hidden builder class: *DataBaseBuilder* generated by *lombok*.
    
2. This builder class *DataBaseBuilder* has chainable setter methods
    for attributes. The sequence of their invocation represents the
    build process.

3. Class *DataBaseBuilder* also has a method: *build()* that ends the
    build process and returns the object of the built class, here an
    object of class *Database*.

The code fragment demonstrates the builder pattern for class *Database*:

```java
public static void main(String[] args) {
    System.out.println("Hello Database!");

    Database db = Database.builder()
        .db_url("jdbc:h2:mem:freerider")
        .db_user("sa")
        .db_password("")
        // 
        .build();

    // show database attributes using generated toString() method
    System.out.println(db);
}
```

Run the code:

```sh
mvn clean compile exec:java -q
```

The code shows an new object of class *Database* has been built with
initialized attributes:

```
Database(db_url=jdbc:h2:mem:freerider, db_user=sa, db_password=)
```

Commit with message: `"mvn-jdbc database builder demo"` and show the commit log:

```sh
git commit --oneline
```
```
06482c2 (HEAD -> mvn-jdbc) mvn-jdbc lombok demo
3bbf119 mvn-jdbc setup
a63a254 add .git-modules
a984e53 add pom.xml, src
cd4acc8 add .gitignore
d77bc6f (tag: root) root commit (empty)
```

De-lombok and inspect the generated class *Database.java* with the static
method *builder()* and the internal class *DatabaseBuilder* with method
*build()*.


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 6. *Logging* with *log4j*

*"Logging"* is the mechanism in software systeme to record activities
or events for later inspection.

[*log4j*](https://logging.apache.org/log4j/2.x/),
[*Wiki*](https://en.wikipedia.org/wiki/Log4j) is a popular loggin framework
for Java by
[*Ceki Gülcü*](https://github.com/ceki) who also created the logging extensions
*slf4j* and *logback*.

Logging consists of steps:

1. `Collection` of log-messages in the code using *Logger* objects with log-methods.

1. `Layout` or `Formatting` to output log-messages in a common format.

1. `Distributuion` through *Appenders* representing destinations of log-messages,
    which can be log-files, log-databases or simply the console.

Only the collection of log-messages should be in the code using *Logger* objects.

*Formatting* and *Distributuion* of log-messages should not be part of the code
and is delegated to `Log-Configuration` files.

Place the following content into *log4j's* log-configuration file:
`src/main/resources/log4j2.properties`:

```sh
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
# 'log4j2.properties' defines the configuration of the logging system
# used in the program, see:
# https://logging.apache.org/log4j/2.x/manual/config-intro.html
# 
# This 'log4j2.properties' file defines:
# - loggers used in the program, e.g. the 'db-logger'
# - filtering (by levels) and distribution of log messages to
#   appenders (here: 'db-console-appender' and 'db-file-appender')
# - appenders
# - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

# configuration of the 'db-logger'-logger: OFF, FATAL, ERROR, WARN,
# INFO, DEBUG, TRACE, ALL
# 
logger.db-logger.name = db-logger
# 
# distribution to appenders according to log levels:
# OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL
logger.db-logger = ALL, db-console-appender, db-file-appender
# 
# configure to not inherit parent logger properties
logger.db-logger.additivity = false

# Console appender used by 'db-logger'
appender.db-con.name = db-console-appender
appender.db-con.type = Console
appender.db-con.layout.type = PatternLayout
appender.db-con.layout.pattern = %m%n
# Filter passes messages with level INFO or higher (INFO, WARN, ERROR, FATAL)
appender.db-con.filter.threshold.type = ThresholdFilter
appender.db-con.filter.threshold.level = INFO

# Rolling file appender used by 'app-logger' in 'logs' directory
appender.db-file.name = db-file-appender
appender.db-file.append = false
appender.db-file.type = RollingRandomAccessFile
appender.db-file.fileName = logs/db-log.log
appender.db-file.filePattern = logs/db-log.log.%i
appender.db-file.immediateFlush = true
appender.db-file.layout.type = PatternLayout
appender.db-file.layout.pattern=[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n
appender.db-file.policies.type = Policies
# Filter passes all messages
appender.db-file.filter.threshold.type = ThresholdFilter
appender.db-file.filter.threshold.level = ALL
```

The fragment describes a configuration of a Logger named: *"db.database"*
with two appenders: *"db-console-appender"* and *"db-file-appender"*,
which will write log-messages to a *"rolling"* file: `logs/database.log`.

Production-grade code should `never` contain `System.out.print()` statements
because console-output may not be available on a production server or in a
*Docker* container.

Therefore, loggers are used that can be reconfigured avoiding console output
when the software is deployed into a production environment.

Log-messages are classified into *log levels* when messages are collected.
Several schemes for log-levels are in use - *log4j* uses methods:

1. `fatal(String msg)` -- messages collected before fatal program crashes for later investigation.

1. `error(String msg)` -- to report none-fatal errors during program execution.

1. `warn(String msg)` -- to report warnings.

1. `info(String msg)` -- to report general information.

1. `debug(String msg)` -- log-messages collected for debugging.

1. `trace(String msg)` -- least-priority log-messages that follow (*"trace"*) program execution.

The following example shows the oise


```java
/**
 * Create new Logger instance with name "db.database"
 */
private static final Logger log = LogManager.getLogger("db.database");

public static void main(String[] args) {
    // 
    // Never use System.out.println() in product code!
    // System.out.println("Hello Database!");

    // use logger instead:
    log.info("Hello Database!");

    log.fatal("log-message at level: fatal");
    log.error("log-message at level: error");
    log.warn("log-message at level: warning");
    log.debug("log-message at level: debug");
    log.trace("log-message at level: trace");

    Database db = Database.builder()
        .db_url("jdbc:h2:mem:freerider")
        .db_user("sa")
        .db_password("")
        // 
        .build();

    if(db==null) {
        log.error("error building database, db==null");
    } else {
        // show database attributes using generated toString() method
        // System.out.println(db.toString());
        log.info(String.format("Database built with attributes: %s", db.toString()));
    }
}
```

Run the code:

```sh
mvn clean compile exec:java -q
```

Log messages appear at the console:

```
Hello Database!
log-message at level: fatal
log-message at level: error
log-message at level: warning
log-message at level: debug
log-message at level: trace
Database built with attributes: Database(db_url=jdbc:h2:mem:freerider, db_user=sa, db_password=)
```

Besides the comnsole-logs, *log4j* has created a log file in a new directory
*"logs"* in the project directory:

```sh
ls -la logs     # show log-file in new directory 'logs'
```
```
total 8
drwxr-xr-x 1 svgr2 Kein   0 Nov 20 23:14 .
drwxr-xr-x 1 svgr2 Kein   0 Nov 20 23:14 ..
-rw-r--r-- 1 svgr2 Kein 857 Nov 20 23:14 database.log   <-- new log-file
```

Show the content of the new log-file:

```sh
cat logs/database.log   # show log-file in new directory 'logs'
```

Log lines are longer providing more detail with a classification `[label]`,
a timestamp and the method in which the log-message was collected:

```
[INFO ] 2025-11-20 23:14:29.950 [mvn.jdbc.application.Application.main()] database - Hello Database!
[FATAL] 2025-11-20 23:14:29.953 [mvn.jdbc.application.Application.main()] database - log-message at level: fatal
[ERROR] 2025-11-20 23:14:29.953 [mvn.jdbc.application.Application.main()] database - log-message at level: error
[WARN ] 2025-11-20 23:14:29.953 [mvn.jdbc.application.Application.main()] database - log-message at level: warning
[DEBUG] 2025-11-20 23:14:29.953 [mvn.jdbc.application.Application.main()] database - log-message at level: debug
[TRACE] 2025-11-20 23:14:29.954 [mvn.jdbc.application.Application.main()] database - log-message at level: trace
[INFO ] 2025-11-20 23:14:29.962 [mvn.jdbc.application.Application.main()] database - Database built with attributes: Database(db_url=jdbc:h2:mem:freerider, db_user=sa, db_password=)
```

Commit with message: `"mvn-jdbc log4j demo"` and show the commit log:

```sh
git commit --oneline
```
```
dd8132e (HEAD -> mvn-jdbc) mvn-jdbc log4j demo
5cc77b5 mvn-jdbc database builder demo
f8c2d2d mvn-jdbc lombok demo
40669ab mvn-jdbc setup
a63a254 add .git-modules
a984e53 add pom.xml, src
cd4acc8 add .gitignore
d77bc6f (tag: root) root commit (empty)
```


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 7. Embedded in-memory Database *H2*

*Client-Server* databases such as:
[*MySQL*](https://www.mysql.com),
[*PostgreSQL*](https://www.postgresql.org) or
[*Oracle*](https://en.wikipedia.org/wiki/Oracle_Database)
run as a separate database-*Server* process (or *Service*) independently of the
application process and are accessed over a TCP/IP connection over the Internet
from the database-*Client* process (the application or applications).

An *Embedded* database runs as part of the same application process. No network
is required. Examples of *embedded* databases are for Java:
[*H2*](https://www.h2database.com/html/main.html) and
[*Derby*](https://en.wikipedia.org/wiki/Apache_Derby).
[*SQLite*](https://sqlite.org)
is a popular database implemented in *C* an can be embedded into applications
that can link C-Libraries.

Databases embeddable in *Java* can simply be added as *.jar* files to the
application and obtained from the
[*maven repository*](https://mvnrepository.com), e.g.
[*H2 (maven repository)*](https://mvnrepository.com/artifact/com.h2database/h2) or
[*Derby (maven repository)*](https://mvnrepository.com/artifact/org.apache.derby).

Two dependencies are required to work with databases:

- the general
    [*JDBC interface*](https://en.wikipedia.org/wiki/Java_Database_Connectivity),
    which is already part of Java run-time libraries in the `java.sql` package.

- a *JDBC-Driver* to connect to a specific database, in case of *H2* this is
    [*h2/2.4.240*](https://mvnrepository.com/artifact/com.h2database/h2/2.4.240)

Steps:

1. Add the *JDBC-Driver* for the *H2* database to *pom.xml*.

1. Checkout code from
    [*https://github.com/sgra64/mvn-fun/tree/mvn-jdbc-h2*](https://github.com/sgra64/mvn-fun/tree/mvn-jdbc-h2)
    (fetch remote branch and checkout into local branch).

1. Run the example:
    ```
    +------+----------------+----------------+------------------------+
    |   ID | NAME           | FIRSTNAME      | CONTACT                |
    +------+----------------+----------------+------------------------+
    |  100 | Eric           | Meyer          | eme22@gmail.com        |
    |  101 | Sommer         | Tina           | +49 030 22458 29425    |
    |  102 | Schulze        | Tim            | +49 171 2358124        |
    |  103 | Brinkmann      | Tobias         | +49 030 662465724      |
    +------+----------------+----------------+------------------------+
    ```

1. Understand the data-model in class
    [*Customer.java*](src/main/java/mvn/jdbc/application/Customer.java).

1. Understand the
    [*TableFormatter.java*](src/main/java/mvn/jdbc/application/TableFormatter.java)
    builder.

1. Understand the
    [*Database.java*](src/main/java/mvn/jdbc/application/Database.java)
    builder.

1. Reconfigure to use the *H2*-database and the full *Customer* data set:
    ```
    query: 'SELECT * FROM CUSTOMER'
    +------+----------------+----------------+------------------------+----------------+---------------------+
    |   ID | NAME           | FIRSTNAME      | CONTACT                | STATUS         | STATUS_CHANGE       |
    +------+----------------+----------------+------------------------+----------------+---------------------+
    |    1 | Meyer          | Eric           | eme22@gmail.com        | Active         | 2024-06-04 12:35    |
    |    2 | Tina           | Sommer         | +49 030 22458 29425    | Active         | 2025-10-07 10:28    |
    |    3 | Tim            | Schulze        | +49 171 2358124        | Active         | 2024-12-28 18:00    |
    |    4 | Allister       | Tony           | +49 030 24253134       | Active         | 2023-02-10 18:00    |
    |    5 | Ohlstadt       | Sandra         | ohlst@gmail.com        | Active         | 2023-08-17 18:00    |
    |    6 | Gronemann      | Erica          | gron@gmx.de            | InRegistration | 2022-02-26 07:02    |
    |    7 | Samadi         | Khaleed        | -                      | Active         | 2020-09-24 18:00    |
    |    8 | Medwedev       | Igor           | gopnik@bht-berlin.de   | InRegistration | 2025-11-28 23:26    |
    +------+----------------+----------------+------------------------+----------------+---------------------+
    (8 rows) fetched
    ```

1. Inspect logs.


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 8. Persistent Database

Reconfigure to use the *H2* database as a *persistent database* (not as volatile
in-memory database).

<!-- 
Allow loading the database configuration from a configuration file
`application.properties` avoiding code changes when the database connection
is changed (and the need to re-compile and re-deploy the application).
 -->
 
*H2* represents persistent state in the project directory in a new folder:
`.database` with a file: `h2.mv.db`.

Re-run the program and observe in the log-file that schema and initial data
are no longer loaded at program start when the database is found.

<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 9. Entity: *"Customer"*

Create a schema for Entity: *"Customer"* with sample customer data.

(to follow).


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 10. Database *Query* and *RowMapper*

Query the database and understand what a *RowMapper* is.

(to follow).


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->

&nbsp;

## 11. Full *FreeRider* Database Schema

Create the full schema and datamodel for the Car-Reservation system and load
sample data.

Run queries, e.g. "Collect all reservations of a customer" or "What is the
status of reservation with id: 256"?

(to follow).


<!-- 
<img src="https://raw.githubusercontent.com/sgra64/se1-play/refs/heads/markup/img/junit-run-2.png" width="360"/>
 -->
