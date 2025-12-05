package mvn.jdbc.application;

import java.io.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.*;

import mvn.jdbc.datamodel.Customer;

/**
 * Container holding objects shared accross the application.
 * Access to objects is obtained through fluent getter methods.
 * <p>
 * During startup, {@link Properties} are loaded from file
 * {@code 'application.properties'} found on the {@code CLASSPATH}
 * and the {@code log4j2} logging system is initialized.
 */
@Getter
@Accessors(fluent=true, chain=true)
public class ApplicationContext {
    // 
    private static boolean started = false;

    /**
     * Application property file name {@code "application.properties"}
     * expected to find on the {@code CLASSPATH}.
     */
    private final String propertyFile = "application.properties";

    /**
     * {@link Properties} found in {@code "application.properties"} file.
     */
    private final Properties properties = new Properties();

    /**
     * Arguments passed from command line or from {@code "application.args"}
     * property.
     */
    private String[] args = new String[] { };

    /**
     * Application logger named {@code "app-logger"}.
     */
    private final Logger log = LogManager.getLogger("app-logger");

    /**
     * {@link java.time.LocalDateTime} formatter for date-only.
     */
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * {@link java.time.LocalDateTime} formatter for date and time.
     */
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * {@link java.time.LocalDateTime} formatter for date and time (with milliseconds).
     */
    private final DateTimeFormatter dtfsec = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * {@link TableFormatter.TableFormatterBuilder} for the {@link Customer} table.
     */
    private final TableFormatter.TableFormatterBuilder
        // 
        customerTableFormatterBuilder = TableFormatter.builder()
            .columns("| ID | NAME | FIRSTNAME | CONTACT |")
            // .columns("| ID | NAME | FIRSTNAME | CONTACT | STATUS | STATUS_CHANGE |")
            .widths(6, 16, 16, 24, 16, 21)
            .alignments("R")
            // 
            .rowMapper(Customer.class, c ->
                new String[] {Long.toString(c.id()), c.name(), c.firstName(), c.contact(),
                    c.status().toString(), c.statusChange().format(dtf)});

    /**
     * None-public constructor invoked by {@link Application} instance that
     * prevents other {@link ApplicationContext} instances being created.
     * @param application
     */
    ApplicationContext(Application application) {
        Optional.ofNullable(application).orElseThrow(() ->
            new IllegalArgumentException("Not permitted to create ApplicationContext instance"));
    }

    /**
     * Startup reading file 'application.properties' into {@link Properties},
     * perform logging test and print a greeting message.
     * <p>
     * If no arguments are passed from the command line (CL), {@code args} are
     * taken from the 'application.args' property.
     * @param clargs arguments passed from the command line
     * @return true if startup succeeded
     */
    boolean startup(String[] clargs, Logger log) {
        if( ! started) {
            // initialize 'args' from the comman line and remove last 'arg' if
            // added by VSCodeRunner (VSCodeRunner adds active file to args[])
            this.args = Arrays.asList(clargs).stream()
                // remove 'arg' if path starts with "c:\path" or "/path"
                .filter(arg -> ! (arg.length() > 30 && (arg.startsWith("/") || arg.charAt(1)==':')))
                .toArray(String[]::new);
            // 
            // attempt to read propertyFile from location on the CLASSPATH
            try (InputStream is = Optional.ofNullable(
                this.getClass().getClassLoader().getResourceAsStream(propertyFile)).orElseThrow(() ->
                    new IOException(String.format("could not find property file: '%s'", propertyFile)));
            ) {
                properties.load(is);     // load 'application properties' file
                log.info(String.format("found '%s' with %d properties", propertyFile, properties.size()));
                // 
                // if no 'args' were passed from the command line, fetch from 'application.args' property
                if(this.args.length==0) {
                    this.args = Optional.ofNullable(properties.getProperty("application.args"))
                        .map(pargs -> {
                            List<String> argl = Arrays.asList(pargs.split("[\\s,;]+"));
                            int size = argl.size();
                            return size==0? this.args : argl.toArray(new String[size]);
                        }).orElse(this.args);
                }
                return started = true;
            // 
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            log.error("attempt to re-start ApplicationContext");
        }
        return false;
    }

    /**
     * Match value of a property found in 'application.properties'.
     * @param property property to match
     * @param value property value to match, {@code "*"} matches any value
     * @return true if property value matches
     */
    public boolean matchProperty(String property, String value) {
        return Optional.ofNullable(properties.getProperty(property))
                .map(v -> value.equals("*") || v.toLowerCase().equals(value)).orElse(false);
    }
}
