package mvn.jdbc.application;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import org.apache.logging.log4j.*;

import mvn.jdbc.datamodel.Customer;
import mvn.jdbc.datamodel.Vehicle;
import mvn.jdbc.datamodel.Reservation;

/**
 * Demo of minimalist support for Object-Relational Mapping (ORM).
 */
public class ORM {

    private static ORM instance = null;

    private final Database db;

    private final Map<Class<?>, ResultSetMapper<?>> resultSetMappers = new HashMap<>();

    private static final Logger log = LogManager.getLogger("db-logger");

    private ORM(Database db) {
        this.db = db;
        this.resultSetMappers.put(Customer.class, customerResultSetMapper);
        this.resultSetMappers.put(Vehicle.class, vehicleResultSetMapper);
        this.resultSetMappers.put(Reservation.class, reservationResultSetMapper);
    }

    /**
     * Public {@link ORM} instance getter.
     * @param db reference to underlying {@link Database}
     * @return {@link ORM} instance
     */
    public static ORM getInstance(Database db) {
        return Optional.ofNullable(instance).orElse(instance = new ORM(db));
    }

    /**
     * {@link java.util.function.Consumer} that can throw {@link SQLException}
     */
    @FunctionalInterface
    public interface PreparedStatementPreparer {
        void accept(PreparedStatement ps) throws SQLException;
    }

    /**
     * {@link java.util.function.Function} that can throw {@link SQLException}
     */
    @FunctionalInterface
    public interface ResultSetMapper<T> {
        Optional<T> apply(ResultSet rs) throws SQLException;
    }

    /**
     * {@link ResultSetMapper}{@code <T>>} for type {@link Customer}
     */
    private final ResultSetMapper<Customer> customerResultSetMapper =
        rs -> {
            long id = rs.getLong("ID");
            String name = rs.getString("NAME");
            String firstName = rs.getString("FIRSTNAME");
            String contact = rs.getString("CONTACT");
            Customer.Status status = Customer.Status.valueOf(rs.getString("STATUS"));
            LocalDateTime statusChange = rs.getTimestamp("STATUS_CHANGE").toLocalDateTime();
            // 
            Customer customer = new Customer(id, name, firstName, contact, status, statusChange);
            return Optional.of(customer);
        };

    /**
     * {@link ResultSetMapper}{@code <T>>} for type {@link Vehicle}
     */
    private final ResultSetMapper<Vehicle> vehicleResultSetMapper =
        rs -> {
            long id = rs.getLong("ID");
            String make = rs.getString("MAKE");
            String model = rs.getString("MODEL");
            int seats = rs.getInt("SEATS");
            Vehicle.Category category = Vehicle.Category.valueOf(rs.getString("CATEGORY"));
            Vehicle.Power power = Vehicle.Power.valueOf(rs.getString("POWER"));
            Vehicle.Status status = Vehicle.Status.valueOf(rs.getString("STATUS"));
            // 
            Vehicle vehicle = new Vehicle(id, make, model, seats, category, power, status);
            return Optional.of(vehicle);
        };

    /**
     * {@link ResultSetMapper}{@code <T>>} for type {@link Reservation}
     */
    private final ResultSetMapper<Reservation> reservationResultSetMapper =
        rs -> {
            long id = rs.getLong("ID");
            long customer_id = rs.getLong("CUSTOMER_ID");
            long vehicle_id = rs.getLong("VEHICLE_ID");
            LocalDateTime timeBegin = rs.getTimestamp("TIME_BEGIN").toLocalDateTime();
            LocalDateTime timeEnd = rs.getTimestamp("TIME_END").toLocalDateTime();
            String pickup = rs.getString("PICKUP");
            String dropoff = rs.getString("DROPOFF");
            Reservation.Status status = Reservation.Status.valueOf(rs.getString("STATUS"));
            // 
            Reservation reservation = new Reservation(id, customer_id, vehicle_id, timeBegin, timeEnd, pickup, dropoff, status);
            return Optional.of(reservation);
        };

    /**
     * Prepare and run query in database and return result as {@link Stream}
     * of objects of type {@code <T>>} mapped by a {@link ResultSetMapper}{@code <T>>}.
     * 
     * @param <T> generic type of objects mapped from the database
     * @param clazz class of objects to map
     * @param sql SQL query to execute
     * @param preparer function invoked to fill {@link PreparedStatement}
     * @return {@link Stream} of objects of type {@code <T>>}
     */
    public <T> Stream<T> query(Class<T> clazz, String sql, PreparedStatementPreparer... preparer) {
        List<T> result = new ArrayList<>();
        // 
        findResultSetMapper(clazz).ifPresent(rowMapper -> {
            var con = db.connect();
            if(con.isPresent()) {
                try(PreparedStatement ps = con.get().prepareStatement(sql)) {
                    if(preparer.length > 0) {
                        preparer[0].accept(ps);
                    }
                    ResultSet rs = ps.executeQuery();
                    while(rs.next()) {
                        rowMapper.apply(rs).ifPresent(t -> result.add(t));
                    }
                } catch (SQLException e) {
                    log.error(String.format("Error reading all records from database CUSTOMER, %s", e.getMessage()));
                }
            }
        });
        return result.stream();
    }

    @SuppressWarnings("unchecked")
    private <T> Optional<ResultSetMapper<T>> findResultSetMapper(Class<T> clazz) {
        for(var key : resultSetMappers.keySet()) {
            if(clazz.isAssignableFrom(key))
                return Optional.of((ResultSetMapper<T>)resultSetMappers.get(key));
        }
        return Optional.empty();
    }
}
