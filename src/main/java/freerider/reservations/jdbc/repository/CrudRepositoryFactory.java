package freerider.reservations.jdbc.repository;

import java.sql.Connection;

import org.springframework.data.repository.CrudRepository;

import freerider.reservations.jdbc.model.Customer;
import freerider.reservations.jdbc.model.Vehicle;


public class CrudRepositoryFactory {

    private static CrudRepositoryFactory crudRepositoryFactory = null;

    private final Connection dbcon;

    private CrudRepository<Customer, Integer> customerRepository = null;

    private CrudRepository<Vehicle, Integer> vehicleRepository = null;


    private CrudRepositoryFactory(Connection dbcon) {
        this.dbcon = dbcon;
    }

    public static CrudRepositoryFactory getInstance(Connection dbcon) {
        if(crudRepositoryFactory==null) {
            crudRepositoryFactory = new CrudRepositoryFactory(dbcon);
        }
        return crudRepositoryFactory;
    }

    public static CrudRepositoryFactory getInstance() {
        if(crudRepositoryFactory==null)
            throw new IllegalAccessError(
                "calling getInstance() without prior passing database connection through getInstance(dbcon)"
            );
        // 
        return crudRepositoryFactory;
    }

    public CrudRepository<Customer, Integer> getCustomerRepository() {
        if(customerRepository==null) {
            createRepositories();
        }
        return customerRepository;
    }

    public CrudRepository<Vehicle, Integer> getVehicleRepository() {
        if(vehicleRepository==null) {
            createRepositories();
        }
        return vehicleRepository;
    }


    private void createRepositories() {
        // 
        // load schema into database, if schema does not exist and
        // load data into database, if schema was created
        var rowMapper = RowMapper.getInstance(dbcon);
        var schemaBuilder = DBSchemaBuilder.getInstance();

        schemaBuilder.probeCreateSchema(dbcon, rowMapper, DBSchemaBuilder.CUSTOMER, dm -> {
            dm.insertCustomer("Meyer, Eric", "eme@gmail.com", Customer.Status.Active);
            dm.insertCustomer("Allister, Tony", "+49 030 2304245", Customer.Status.InRegistration);
            dm.insertCustomer("Ohlstadt, Sandra", "ohlstadt@gmx.de", Customer.Status.Active);
            dm.insertCustomer("Gronemann, Erica", "maus@bht-berlin.de", Customer.Status.Active);
            dm.insertCustomer("Samadi, Khaleed", "mocka@gmail.com", Customer.Status.Active);
            dm.insertCustomer("Medwedev, Igor", "+49 042 30452626", Customer.Status.Active);
        });

        schemaBuilder.probeCreateSchema(dbcon, rowMapper, DBSchemaBuilder.VEHICLE, dm -> {
            dm.insertVehicle("VW", "Golf");
            dm.insertVehicle("VW", "Polo");
            dm.insertVehicle("BMW", "320d T");
        });

        customerRepository = new CrudRepositoryImpl<Customer, Integer>(dbcon, "CUSTOMER");
        
        vehicleRepository = new CrudRepositoryImpl<Vehicle, Integer>(dbcon, "VEHICLE");
    }
}