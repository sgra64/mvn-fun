package freerider.reservations.jdbc.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import freerider.reservations.jdbc.model.Customer;
import freerider.reservations.jdbc.model.Vehicle;


class IdMapper<ID> {

    private static IdMapper<Customer> customerIdMapper = null;
    private static IdMapper<Vehicle> vehicleIdMapper = null;

    private final String tableName;


    private IdMapper(String tableName) {
        this.tableName = tableName;
    }

    static IdMapper<?> getInstance(String tableName) throws IllegalAccessError {
        if(tableName==null)
            throw new IllegalArgumentException("tableName is null");
        // 
        switch(tableName) {
        case DBSchemaBuilder.CUSTOMER:
            return Optional.ofNullable(customerIdMapper).orElse(customerIdMapper = new IdMapper<Customer>(tableName));
        
        case DBSchemaBuilder.VEHICLE:
            return Optional.ofNullable(vehicleIdMapper).orElse(vehicleIdMapper = new IdMapper<Vehicle>(tableName));
        }
        // 
        throw new IllegalAccessError(String.format("tableName: %s is unsupported", tableName));
    }

    ID getId(Object entity) throws IllegalArgumentException, IllegalAccessError {
        if(entity==null)
            throw new IllegalArgumentException("entity is null");
        // 
        switch(tableName) {

        case DBSchemaBuilder.CUSTOMER:
            if(entity instanceof Customer) {
                return castId(((Customer)entity).id());
            }
            break;

        case DBSchemaBuilder.VEHICLE:
            if(entity instanceof Vehicle) {
                return castId(((Vehicle)entity).id());
            }
            break;
        }
        throw new IllegalAccessError("entity is of unsupported type: " + entity.getClass().getSimpleName());
    }

    void setStatement(int i, PreparedStatement ps, ID id) throws SQLException {
        if(idIsIntOrLong()) {
            ps.setInt(i, toInt(id));
        } else {
            ps.setString(i, toString(id));
        } 
    }

    boolean idIsIntOrLong() { return true; }

    boolean idIsString() { return false; }

    String toString(ID id) {
        if(id==null)
            throw new IllegalArgumentException("id is null");
        // 
        return idIsIntOrLong()? String.format("%d", id) : idIsString()? (String)id : "";
    }

    int toInt(ID id) {
        if(id==null)
            throw new IllegalArgumentException("id is null");
        // 
        return idIsIntOrLong()? Math.toIntExact((int)id) : 0;
    }

    @SuppressWarnings("unchecked")
    private ID castId(Object id) { return (ID)id; }
}