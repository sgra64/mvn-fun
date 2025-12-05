package mvn.jdbc.application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import mvn.jdbc.application.Application.Runnable;
import mvn.jdbc.datamodel.Customer;

/**
 * Demo that creates {@link Customer} objects and outputs as table.
 * <pre>
 * +------+----------------+----------------+------------------------+
 * |   ID | NAME           | FIRSTNAME      | CONTACT                |
 * +------+----------------+----------------+------------------------+
 * |  100 | Eric           | Meyer          | eme22@gmail.com        |
 * |  101 | Sommer         | Tina           | +49 030 22458 29425    |
 * |  102 | Schulze        | Tim            | +49 171 2358124        |
 * |  103 | Brinkmann      | Tobias         | +49 030 662465724      |
 * +------+----------------+----------------+------------------------+
 * </pre>
 */
public class CustomerRunner implements Application.Runnable {

    @Override
    public Runnable run(ApplicationContext context) {
        // 
        DateTimeFormatter dtf = context.dtf();
        var tableBuilder = context.customerTableFormatterBuilder();
        // 
        Customer c1 = new Customer(100L, "Eric", "Meyer", "eme22@gmail.com", Customer.Status.Active, LocalDateTime.parse("2024-06-04 12:35", dtf));
        Customer c2 = new Customer(101L, "Sommer", "Tina", "+49 030 22458 29425", Customer.Status.Active, LocalDateTime.parse("2025-10-07 10:28", dtf));
        Customer c3 = new Customer(102L, "Schulze", "Tim", "+49 171 2358124", Customer.Status.Active, LocalDateTime.parse("2024-12-28 18:00", dtf));
        // 
        Customer c4 = new Customer(103L)
            .name("Brinkmann")        // initialize with chained setter methods
            .firstName("Tobias")
            .contact("+49 030 662465724")
            .status(Customer.Status.InRegistration)
            .statusChange(LocalDateTime.parse("2025-11-28 12:18", dtf));
        // 
        // output to 'Customer' table
        tableBuilder.build()
            .header()
                .row(c1)
                .row(c2)
                .row(c3)
                .row(c4)
            .footer()
            .print(System.out);
        // 
        return this;
    }
}
