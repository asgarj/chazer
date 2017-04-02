package org.javadov.catmouse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.IOException;
import java.net.URL;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by asgar on 3/23/17.
 */
public final class CatMouseGame {
    public static final Logger logger = Logger.getLogger("Chazzer-Game");
    public static final String FILENAME = "game.log";

    private static final String SERVER_ADDRESS = "http://localhost";
    private static final int SERVER_PORT = 2005;

//    public CatMouseGame() {
//        packages("org.javadov.catmouse");
//    }

    public static void main(String[] args) throws IOException {
//        URI baseUri = UriBuilder.fromUri(SERVER_ADDRESS).port(SERVER_PORT).build();
//        ResourceConfig config = new ResourceConfig(ActionProcessor.class);
//        Server server = JettyHttpContainerFactory.createServer(baseUri, config);
        try {
            logger.addHandler(new FileHandler(FILENAME, true));
        } catch (IOException e) {
            e.printStackTrace();
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
