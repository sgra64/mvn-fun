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