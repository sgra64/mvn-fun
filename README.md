# C3: *mvn-jdbc-c3* - CrudRepository

[*CrudRepository*](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html)
is a well-known interface to access database tables that was introduced by the *Spring Framework*.

The interface
[*CrudRepository.java*](https://github.com/spring-projects/spring-data-commons/blob/main/src/main/java/org/springframework/data/repository/CrudRepository.java)
defines basic methods to access data records (rows) in a database table. It is part of the *Spring Data* package.

Operations are grouped by the *CRUD pattern:*

- *Creation* of new data records (rows) in a table with the SQL equivalent of *INSERT*,

- *Read* of existing data records (SQL: *SELECT*),

- *Update* of existing data records (SQL: *UPDATE*) and

- *Delete* of existing data records (SQL: *DELETE*).

One *CrudRepository* provides access to one database table.

An essential part of *CrudRepository* is that it includes a mapping of database records
to objects and vice versa, which is called *object-relational mapping:*

- Objects are mapped to rows for insertion or update in the associated table.
    And rows retrieved from the table are mapped to objects.

- It assumes that a corresponding *Java class* or a
    [*Java Record*](https://medium.com/@mak0024/a-comprehensive-guide-to-java-records-2e8edcbd9c75)
    (as a more modern choice) exists for each table.

- Hence, `CrudRepository<T,ID>` is of a generic type `<T>` that corresponds to the
    associated *Java class* or a *Java Record*.  Type `<ID>` that corresponds to the
    generic type of the ID (identifier), which typically can be *String*, *long* or *int*.



&nbsp;

## 1 Set-up New Branch in *mvn-jdbc-c3*

Set up a new branch in *mvn-jdbc-c3* with the base at the current *mvn-jdbc* branch.

Pull data from the remote branch `https://github.com/sgra64/mvn-fun/tree/mvn-jdbc-c3`:

```sh
# set URL to repository to pull the remote branch
git remote add se2-mvn-repo https://github.com/sgra64/mvn-fun.git

# pull the remote branch 'mvn-jdbc-c3'
git pull se2-mvn-repo mvn-jdbc-c3 \
    --squash --allow-unrelated-histories --strategy-option theirs
```

The *pull* will merge changes into your new local branch *mvn-jdbc-c3*.

Build the project and run. Fragments of the code are working. The *Customer*
table is fully shown. Various attributs are missing in the table for *Vehicles*
and an *Unimplemented method 'findAllById'* message appears at the end.

```
Hello "freerider.reservations.jdbc" example!
Database connection open.
 --> found table: CUSTOMER
+----+---------------------+---------------------+---------------------+
| ID | NAME                | CONTACT             | STATUS              |
+----+---------------------+---------------------+---------------------+
| 1  | Meyer, Eric         | eme@gmail.com       | Active              |
| 2  | Allister, Tony      | +49 030 2304245     | InRegistration      |
| 3  | Ohlstadt, Sandra    | ohlstadt@gmx.de     | Active              |
| 4  | Gronemann, Erica    | maus@bht-berlin.de  | Active              |
| 5  | Samadi, Khaleed     | mocka@gmail.com     | Active              |
| 6  | Medwedev, Igor      | +49 042 30452626    | Active              |
+----+---------------------+---------------------+---------------------+
+----+-----------+-----------+-----+-----------+-----------+-----------+
| ID | MAKE      | MODEL     | SEA | CATEGORY  | POWER     | STATUS    |
+----+-----------+-----------+-----+-----------+-----------+-----------+
| 1  | VW        | Golf      |     |           |           |           |
| 2  | VW        | Polo      |     |           |           |           |
| 3  | BMW       | 320d T    |     |           |           |           |
+----+-----------+-----------+-----+-----------+-----------+-----------+
numRowsDeleted: 2
Exception in thread "main" java.lang.UnsupportedOperationException: Unimplemented method 'findAllById'
	at freerider.reservations.jdbc.repository.CrudRepositoryImpl.findAllById(CrudRepositoryImpl.java:107)
	at freerider.reservations.jdbc.application.Application.run(Application.java:63)
	at freerider.reservations.jdbc.application.Application.main(Application.java:28)
```



&nbsp;

## 2 Understand the Code Drop

Quickly browse the structure of the code that has arrived:

```
src/main/java/freerider/reservations/jdbc/application
src/main/java/freerider/reservations/jdbc/application/Application.java

src/main/java/freerider/reservations/jdbc/model
src/main/java/freerider/reservations/jdbc/model/Customer.java
src/main/java/freerider/reservations/jdbc/model/Vehicle.java

src/main/java/freerider/reservations/jdbc/repository
src/main/java/freerider/reservations/jdbc/repository/CrudRepositoryFactory.java
src/main/java/freerider/reservations/jdbc/repository/CrudRepositoryImpl.java
src/main/java/freerider/reservations/jdbc/repository/DBSchemaBuilder.java
src/main/java/freerider/reservations/jdbc/repository/IdMapper.java
src/main/java/freerider/reservations/jdbc/repository/RowMapper.java

src/main/java/org/springframework
src/main/java/org/springframework/data/repository/CrudRepository.java
```

Draw a *UML Component Diagram* of the major interfaces, objects or instances.

Write 1-2 sentences of what each `.java` file in the code drop means or does.



&nbsp;

## 3 Complete *CrudRepositoryImpl.java*

Part of the reason why output shows missing pieces is an incomplete implementation class
*CrudRepositoryImpl.java*.

Complete the missing methods.



&nbsp;

## 4 Complete the Schema for *Vehicles*

Table *Vehicles* only shows partial attributes. Complete the missing attributes for the
Vehicles table according to the ER-Diagram:

<img src="https://raw.githubusercontent.com/sgra64/docker/markup/DB12-freerider/freerider_ERD.png" width="600"/>

Make sure to locate all places for the change:

- the database schema,

- the data model *Record*,

- the initial creation of records in the database,

- methods in the the RowMapper-class.

The *Vehicle* table should show the completed attributes at the end:

```
+----+-----------+-----------+-----+-----------+-----------+-----------+
| ID | MAKE      | MODEL     | SEA | CATEGORY  | POWER     | STATUS    |
+----+-----------+-----------+-----+-----------+-----------+-----------+
| 1  | VW        | Golf      | 4   | Sedan     | Diesel    | Active    |
| 2  | VW        | Polo      | 4   | Sedan     | Gasoline  | Active    |
| 3  | BMW       | 320d T    | 4   | Sedan     | Diesel    | Active    |
+----+-----------+-----------+-----+-----------+-----------+-----------+
```



&nbsp;

## 5 Add Table *Reservations*

Add the table *Reservations* with attributes according to the ER-Diagram.

Initialize the table with reservations:

```sql
INSERT INTO RESERVATION (ID, CUSTOMER_ID, VEHICLE_ID, RBEGIN, REND, PICKUP, DROPOFF, STATUS) VALUES
    (  1,  1,  2, '2025-06-12 08.00.00', '2025-06-12 20.00.00', 'Berlin Wedding', 'Berlin Wedding', 'Booked'),
    (  2,  6,  1, '2025-06-14 10.00.00', '2025-06-24 10.00.00', 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (  3,  3,  1, '2025-06-03 14.00.00', '2025-06-03 16.30.00', 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (  4,  1,  3, '2025-06-06 09.00.00', '2025-06-07 08.59.59', 'Berlin Wedding', 'Hamburg', 'Inquired'),
    (  5,  4,  1, '2025-06-12 14.00.00', '2025-06-19 13.59.59', 'Potsdam', 'Teltow', 'Inquired')
;
```


<!-- 
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


<img src="https://raw.githubusercontent.com/sgra64/mvn-fun/refs/heads/markup/img/git-log-after-merge.png" width="600"/>
-->
