package org.javadov.catmouse.rest;

import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.*;

/**
 * Created by asgar on 3/29/17.
 */

public class CatMouseTestsHelper {
    private static final String LOG_TEST_FILE = "/logging.properties";
    private static Logger logger = Logger.getLogger("Cat-Mouse-Game-Test");

    static {
        try (InputStream is = CatMouseTestsHelper.class.getResourceAsStream(LOG_TEST_FILE)){
            LogManager.getLogManager().readConfiguration(is);
            logger.addHandler(new FileHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    @Test
    @Ignore
    public void generatePlayer() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(UriBuilder.fromUri("http://localhost:2005")).path("players");

        final String[] PLAYER_NAMES = {"Comp", "iPhone"};
        for(String PLAYER_NAME: PLAYER_NAMES) {
            target.request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(PLAYER_NAME, MediaType.TEXT_PLAIN_TYPE));
        }
    }

    public static void addDummyPlayers() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(UriBuilder.fromUri("http://localhost:2005")).path("newplayer");

        final String[] PLAYER_NAMES = {"Comp", "iPhone"};
        for(String PLAYER_NAME: PLAYER_NAMES) {
            target.request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(PLAYER_NAME, MediaType.TEXT_PLAIN_TYPE));
        }
    }
    @Test
    public void testLoggerFormatter() throws IOException {
        logger.info("Info message");
        logger.finest("finest message");
        logger.log(Level.WARNING, "warning message");
        logger.log(Level.FINE, "fine message");
    }
}
