package freerider.reservations.jdbc.model;

/**
 * Entity type of a {@link Vehicle} object associated with records stored in
 * a <i>VEHICLE</i> table in a database.
 */
public record Vehicle(

    /**
     * Primary key as Vehicle identity, assigned by the database.
     */
    int id,

    /**
     * Make attribute of a Vehicle or brand, e.g. "VW".
     */
    String make,

    /**
     * Model attribute of a Vehicle, e.g. "Golf"
     */
    String model

) {

}