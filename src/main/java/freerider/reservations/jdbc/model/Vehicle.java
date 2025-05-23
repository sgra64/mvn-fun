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

    /**
     * Constructor of object that is not in the database (with illegal negative id).
     * @param make brand of a Vehicle, e.g. "VW"
     * @param model model of a Vehicle, e.g. "Golf"
     */
    public Vehicle(String make, String model) {
        this(-1, make, model);
    }
}