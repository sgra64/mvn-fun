package mvn.jdbc.application;

import java.util.*;
import org.apache.logging.log4j.*;

/**
 * Class with static {@code main()}-method that starts the program.
 * <p>
 * Creates instance of itself and of class {@link ApplicationContext},
 * where it invokes {@code startup(args, log)} method. During startup,
 * the {@code log4j2} logging system is initialized and file
 * {@code 'application.properties'} located on the {@code CLASSPATH}
 * is loaded into {@link Properties}.
 * <p>
 * Class then invokes method {@code run(ApplicationContext context)}
 * on instances registered in {@link ApplicationContext}.
 * 
 * Run the program:
 * - mvn exec:java -q
 * - java -cp @$cpfile mvn.jdbc.application.Application
 */
public class Application {

    /**
     * Interface of an instance where method {@code run(ApplicationContext context)}
     * can be invoked.
     */
    public interface Runnable {

        /**
         * Method invoked with {@link ApplicationContext} containing {@code args[]}
         * and {@link Properties} obtained from {@code 'application.properties'}.
         * @param context {@link ApplicationContext} passed to {@link Runnable} instance
         * @return chainable self-reference
         */
        Runnable run(ApplicationContext context);
    }

    /**
     * Container holding objects shared accross the application
     */
    private final ApplicationContext context;

    /**
     * {@link Logger} 'app-logger' instance
     */
    private final Logger log;

    /**
     * Private constructor.
     */
    private Application() {
        this.context = new ApplicationContext(this);
        this.log = context.log();
        this.log.trace("Application instance created");
    }

    /**
     * Static {@code main()} - method as entry point for the Java VM.
     * @param args arguments passed from the command line
     */
    public static void main(String[] args) {
        var application = new Application();
        application.run(args);
    }

    /**
     * Method invoked on {@link Application} instance to perform
     * {@link ApplicationContext} {@code startup()}, print a
     * {@code greeting()} message and invoke
     * {@code run(ApplicationContext context)} on instances registered
     * in {@link ApplicationContext}.
     * @param args arguments passed from the command line
     * @return true if {@link ApplicationContext} {@code startup()} succeeded
     */
    private boolean run(String[] args) {
        if(context.startup(args, log)) {
            log.trace("Application.context startup() passed");
            // 
            greeting();
            // 
            // load runnables found in 'application.run' property
            for(String rs : Optional.ofNullable((String)context.properties().get("application.run"))
                .orElseGet(() -> { log.info("no property 'application.run' found in 'application.properties'"); return ""; })
                .split("[,;]")
            ) {
                Runnable runnable = null;
                switch(rs.trim()) {
                    case "CustomerRunner": runnable = new CustomerRunner(); break;
                    case "DatabaseRunner": runnable = new DatabaseRunner(); break;
                }
                if(runnable != null) {
                    log.info(String.format("executing runnable: '%s.class'", runnable.getClass().getSimpleName()));
                    runnable.run(context);
                } else {
                    log.info(String.format("no runnable class found in 'application.run' for '%s'", rs));
                }
            }
        // 
        } else {
            log.error("Application.context startup() failed");
        }
        return true;
    }

    /**
     * Print greeting if property "application.greeting" is not set to "false".
     */
    private void greeting() {
        if( ! context.matchProperty("application.greeting", "false")) {
            String module = Optional.ofNullable(Application.class.getModule().getName()).map(m -> " (modular)").orElse("");
            String name = Optional.ofNullable((String)context.properties().get("application.name")).orElse("program");
            String msg = String.format("%s%s", name, module);
            log.trace("print greeting message: " + msg);
            System.out.println(msg);
        }
    }
}
