package freerider.reservations.jdbc.model;

/**
 * Entity type of a {@link Customer} object associated with records stored in
 * a <i>CUSTOMER</i> table in a database.
 */
public record Customer(

    /**
     * Primary key as Customer identity, assigned by the database.
     */
    int id,

    /**
     * Name attribute of a Customer.
     */
    String name,

    /**
     * Contact attribute of a Customer.
     */
    String contact,

    /**
     * Customer status attribute.
     */
    Status status

) {

    /**
     * Livecycle states of a Customer.
     */
    public enum Status {Active, InRegistration, Terminated};

    /**
     * Constructor of object that is not in the database (with illegal negative id).
     * @param name name of a Customer
     * @param contact contact information of a Customer (email, phone number)
     * @param status status attribute of a Customer: Active, InRegistration, Terminated
     */
    public Customer(String name, String contact, Status status) {
        this(-1, name, contact, status);
    }
}