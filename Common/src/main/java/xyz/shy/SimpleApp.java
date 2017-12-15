package xyz.shy;

import com.typesafe.config.*;

/**
 * Created by Shy on 2017/12/15
 * https://github.com/lightbend/config/blob/master/examples/java/complex-app/src/main/java/ComplexApp.java
 */

public class SimpleApp {
    public static void main(String[] args) {
        // example of how system properties override; note this
        // must be set before the config lib is used
//        System.setProperty("simple-lib.whatever", "This value comes from a system property");

        // Load our own config values from the default location,
        // application.conf
        Config conf = ConfigFactory.load();
        System.out.println("The answer is: " + conf.getString("simple-app.answer"));
        System.out.println("The whatever is: " + conf.getString("simple-lib.whatever"));

        // In this simple app, we're allowing SimpleLibContext() to
        // use the default config in application.conf ; this is exactly
        // the same as passing in ConfigFactory.load() here, so we could
        // also write "new SimpleLibContext(conf)" and it would be the same.
        // (simple-lib is a library in this same examples/ directory).
        // The point is that SimpleLibContext defaults to ConfigFactory.load()
        // but also allows us to pass in our own Config.
//        SimpleLibContext context = new SimpleLibContext();
//        context.printSetting("simple-lib.foo");
//        context.printSetting("simple-lib.hello");
//        context.printSetting("simple-lib.whatever");
    }
}
