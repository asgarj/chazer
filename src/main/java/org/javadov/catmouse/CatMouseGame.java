package org.javadov.catmouse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.javadov.catmouse.model.Player;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.*;

/**
 * Created by asgar on 3/23/17.
 */
public final class CatMouseGame {
    public static final String LOGGER_CONFIG = "/logging.properties";
    public static final String APP_CONFIG = "/config.properties";
    public static final Logger logger = Logger.getLogger("Chazzer-Game");

    private static final String SERVER_ADDRESS = "http://localhost";
    private static int SERVER_PORT;

//    public CatMouseGame() {
//        packages("org.javadov.catmouse");
//    }

    public static void main(String[] args) throws IOException {
//        URI baseUri = UriBuilder.fromUri(SERVER_ADDRESS).port(SERVER_PORT).build();
//        ResourceConfig config = new ResourceConfig(ActionProcessor.class);
//        Server server = JettyHttpContainerFactory.createServer(baseUri, config);
        try (InputStream is = CatMouseGame.class.getResourceAsStream(LOGGER_CONFIG)){
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.setUseParentHandlers(false);
        logger.addHandler(new FileHandler());
        
        try (InputStream is = CatMouseGame.class.getResourceAsStream(APP_CONFIG)) {
            Properties props = new Properties();
            props.load(is);
            SERVER_PORT = Integer.valueOf(props.getProperty("PORT"));
            Player.ROWS = Integer.valueOf(props.getProperty("ROWS"));
            Player.COLUMNS = Integer.valueOf(props.getProperty("COLUMNS"));
            logger.log(Level.INFO, String.format("Game board size is [%d]x[%d]", Player.ROWS, Player.COLUMNS));
        }

        ResourceConfig config = new ResourceConfig();
        config.packages("org.javadov.catmouse.rest");

        String baseDir = "/META-INF/resources";
        URL baseUrl = CatMouseGame.class.getResource(baseDir);
        String basePath = baseUrl.toExternalForm();

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        resourceHandler.setResourceBase(basePath);


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server server = new Server(SERVER_PORT);
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});
        server.setHandler(handlers);

        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.packages",
                "org.javadov.catmouse.rest");

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            System.out.println("EXCEPTION OCCURRED!");
            System.out.println(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
        } finally {
            server.destroy();
        }
    }
}
