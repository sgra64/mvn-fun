package freerider.reservations.jdbc.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import freerider.reservations.jdbc.model.Customer;
import freerider.reservations.jdbc.model.Vehicle;


class RowMapper {

    /**
     * Singleton {@link RowMapper} instance.
     */
    private static RowMapper rowMapper = null;

    private final Connection dbcon;


    /**
     * Private constructor to avoid external instance creation as
     * part of the Singleton pattern.
     */
    private RowMapper(Connection dbcon) {
        this.dbcon = dbcon;
    }

    /**
     * Public getter method of {@link DBSchemaBuilder} instance as
     * part of the Singleton pattern.
     * @return {@link DBSchemaBuilder} singleton instance
     */
    public static RowMapper getInstance(Connection dbcon) {
        if(rowMapper==null) {
            rowMapper = new RowMapper(dbcon);
        }
        return rowMapper;
    }

    Optional<?> create(ResultSet rs, String tableName) {
        if(rs==null)
            throw new IllegalArgumentException("argument ResultSet rs is null");
        if(tableName==null)
            throw new IllegalArgumentException("argument tableName is null");
        // 
        switch(tableName) {
        case DBSchemaBuilder.CUSTOMER: return createCustomer(rs);
        case DBSchemaBuilder.VEHICLE: return createVehicle(rs);
        }
        return Optional.empty();
    }

    Optional<?> save(Object entity, String tableName) {
        if(entity==null)
            throw new IllegalArgumentException("argument entity is null");
        if(tableName==null)
            throw new IllegalArgumentException("argument tableName is null");
        // 
        switch(tableName) {
        case DBSchemaBuilder.CUSTOMER: if(entity instanceof Customer) saveCustomer((Customer)entity); break;
        case DBSchemaBuilder.VEHICLE: if(entity instanceof Vehicle) saveVehicle((Vehicle)entity); break;
        }
        return Optional.empty();
    }


    private Optional<Customer> createCustomer(ResultSet rs) {
        if(rs != null) {
            try {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                String contact = rs.getString("CONTACT");
                Customer.Status status = Customer.Status.valueOf(rs.getString("STATUS"));
                return Optional.of(new Customer(id, name, contact, status));
                //
            } catch (SQLException e) { }
        }
        return Optional.empty();
    }

    private Optional<Customer> saveCustomer(Customer entity) {
        if(entity==null)
            throw new IllegalArgumentException("argument entity is null");
        //
        var id = entity.id();
        if(id <= 0L) {
            // no valid id in entity -> INSERT  in database
            return insertCustomer(entity.name(), entity.contact(), entity.status());
        } else {
            // UPDATE in database
        }
        return Optional.empty();
    }

    Optional<Customer> insertCustomer(String name, String contact, Customer.Status status) {
        // 
        try(PreparedStatement ps = dbcon.prepareStatement(
            String.format("INSERT INTO %s(NAME, CONTACT, STATUS) VALUES(?, ?, ?)", DBSchemaBuilder.CUSTOMER),
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, name);
            ps.setString(2, contact);
            ps.setString(3, status.name());
            ps.executeUpdate();
            // int numRowsAffected = ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    int id = rs.getInt(1);
                    // System.out.println(String.format("inserted customer \"%s %s\"\twith id: %d (%d rows)",
                    //     firstName, lastName, id, numRowsAffected));
                    var customer = new Customer(id, name, contact, status);
                    return Optional.of(customer);
                }
            } catch(SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }


    private Optional<Vehicle> createVehicle(ResultSet rs) {
        if(rs != null) {
            try {
                int id = rs.getInt("ID");
                String make = rs.getString("MAKE");
                String model = rs.getString("MODEL");
                return Optional.of(new Vehicle(id, make, model));
                //
            } catch (SQLException e) { }
        }
        return Optional.empty();
    }

    private Optional<Vehicle> saveVehicle(Vehicle entity) {
        if(entity==null)
            throw new IllegalArgumentException("argument entity is null");
        //
        var id = entity.id();
        if(id <= 0L) {
            // no valid id in entity -> INSERT in database
            return insertVehicle(entity.make(), entity.model());
        } else {
            // UPDATE in database
        }
        return Optional.empty();
    }

    Optional<Vehicle> insertVehicle(String make, String model) {
        // 
        try(PreparedStatement ps = dbcon.prepareStatement(
            String.format("INSERT INTO %s(MAKE, MODEL) VALUES(?, ?)", DBSchemaBuilder.VEHICLE),
                Statement.RETURN_GENERATED_KEYS
        )) {
            int i=1;
            ps.setString(i++, make);
            ps.setString(i++, model);
            // 
            ps.executeUpdate();
            // int numRowsAffected = ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()) {
                    int id = rs.getInt(1);
                    // System.out.println(String.format("inserted vehicle \"%s %s\"\twith id: %d (%d rows)",
                    //     make, model, id, numRowsAffected));
                    var vehicle = new Vehicle(id, make, model);
                    return Optional.of(vehicle);
                }
            } catch(SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }
}