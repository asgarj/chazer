package org.javadov.catmouse.rest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by asgar on 3/29/17.
 */
public class CatMouseTestsHelper {
    public static final String FILENAME = "game.log";
    private static Logger logger = Logger.getLogger("Cat-Mouse-GameDeprecated");
    static {
        try {
            logger.addHandler(new FileHandler(FILENAME, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void addDummyPlayers() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(UriBuilder.fromUri("http://localhost:2005")).path("newplayer");

        final String[] PLAYER_NAMES = {"Asgar", "Yegana", "Other"};
        for(String PLAYER_NAME: PLAYER_NAMES) {
            target.request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(PLAYER_NAME, MediaType.TEXT_PLAIN_TYPE));
        }
    }
}
