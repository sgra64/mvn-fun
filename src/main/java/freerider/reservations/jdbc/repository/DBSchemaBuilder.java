package freerider.reservations.jdbc.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


/**
 * Singleton component class that probes whether a schema (tables)
 * is present in a database and creates the schema if not.
 */
class DBSchemaBuilder {

    static final String CUSTOMER = "CUSTOMER";
    static final String VEHICLE = "VEHICLE";
    static final String RESERVATION = "RESERVATION";

    /**
     * Map of table names and corresponding SQL-Schemata.
     */
    final Map<String, String> schemaMap = Map.of(
        CUSTOMER,
            "CREATE TABLE if not exists " + CUSTOMER + " (" +
            "  ID INT not null auto_increment, " +
            // "  FIRSTNAME VARCHAR(60), " +
            // "  LASTNAME VARCHAR(60), " +
            "  NAME VARCHAR(60) default null, " +
            "  CONTACT VARCHAR(60) default null, " +
            "  STATUS ENUM('Active', 'InRegistration', 'Terminated') default null, " +
            "  " +
            "  PRIMARY KEY ( ID )" +
            ")",
        // 
        VEHICLE,
            "CREATE TABLE if not exists " + VEHICLE + " (" +
            "  ID INT not null auto_increment, " +
            "  MAKE VARCHAR(60) default null, " +
            "  MODEL VARCHAR(60) default null, " +
            "  SEATS INT DEFAULT '4', " +
            "  CATEGORY ENUM('Sedan', 'SUV', 'Convertible', 'Van', 'Bike') default null, " +
            "  POWER ENUM('Gasoline', 'Diesel', 'Electric', 'Hybrid', 'Hydrogen') default null, " +
            "  STATUS ENUM('Active', 'Serviced', 'Terminated') default null, " +
            "  " +
            "  PRIMARY KEY ( ID )" +
            ")",
        // 
        RESERVATION,
            "CREATE TABLE if not exists " + RESERVATION + " (" +
            "  ID INT not null auto_increment, " +
            "  CUSTOMER_ID INT not null, " +
            "  VEHICLE_ID INT not null, " +
            "  RBEGIN DATETIME default null, " +
            "  REND DATETIME default null, " +
            "  PICKUP VARCHAR(48) default null, " +
            "  DROPOFF VARCHAR(48) default null, " +
            "  STATUS ENUM('Inquired', 'InquiryConfirmed', 'Booked', 'Cancelled') default null, " +
            "  " +
            "  PRIMARY KEY ( ID ), " +
            "  FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER(ID), " +
            "  FOREIGN KEY (VEHICLE_ID) REFERENCES VEHICLE(ID)" +
            ")"
    );

    /**
     * Singleton {@link DBSchemaBuilder} instance.
     */
    private static DBSchemaBuilder dbSchema = null;


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
    static DBSchemaBuilder getInstance() {
        if(dbSchema==null) {
            dbSchema = new DBSchemaBuilder();
        }
        return dbSchema;
    }

    /**
     * Probe that tables of the schema are present and create the ones
     * that are not.
     * @param dbcon open database connection
     * @return list of names of created tables
     */
    List<String> probeCreateSchema(Connection dbcon, RowMapper dataMapper,
        String tableName, Consumer<RowMapper> initializr)
    {
        List<String> tablesFound = new ArrayList<>();
        List<String> tablesCreated = new ArrayList<>();
        try(
            Statement stmt = dbcon.createStatement();
        ) {
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            while(rs.next()){
                var table = rs.getString(1);
                // System.out.println(String.format(" --> found table: %s", table));
                tablesFound.add(table);
            }
            if( ! tablesFound.contains(tableName)) {
                var schema = schemaMap.get(tableName);
                if(schema != null) {
                    try {
                        stmt.executeUpdate(schema);
                        tablesCreated.add(tableName);
                        if(initializr != null) {
                            initializr.accept(dataMapper);
                        }
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        tablesFound.clear();
        return tablesCreated;
    }
}