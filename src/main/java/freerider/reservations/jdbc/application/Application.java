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
            // 
            // // load schema into database, if schema does not exist and
            // // load data into database, if schema was created
            // DBSchemaBuilder.getInstance().probeCreateSchema(dbcon,
            //     () -> load_CUSTOMER_Data(dbcon)
            // );

            // // read records from CUSTOMER table
            // try(
            //     Statement stmt = dbcon.createStatement();
            //     ResultSet rs = stmt.executeQuery("SELECT * FROM CUSTOMER")
            // ) {
            //     StringBuilder sb = new StringBuilder();
            //     String hline = String.format("+----+%s+%s+", "-".repeat(21), "-".repeat(21));
            //     sb.append(String.format("%s\n", hline));
            //     sb.append(String.format("| %-2s | %-20s| %-20s|\n", "ID", "FIRSTNAME", "LASTNAME"));
            //     sb.append(String.format("%s\n", hline));
            //     // 
            //     // iterate over ResultSet of returned CUSTOMER records
            //     while(rs.next()) {
            //         long id = rs.getLong("ID");
            //         String firstName = rs.getString("FIRSTNAME");
            //         String lastName = rs.getString("LASTNAME");
            //         // 
            //         String line = String.format("| %2d | %-20s| %-20s|\n", id, firstName, lastName);
            //         sb.append(line);
            //     }
            //     sb.append(String.format("%s\n", hline));
            //     System.out.println(sb.toString());
            // // 
            // } catch (SQLException e) {
            //     System.out.println(String.format("Error reading all records from database CUSTOMER"));
            // }
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