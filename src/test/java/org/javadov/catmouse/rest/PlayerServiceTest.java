package org.javadov.catmouse.rest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.StringReader;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.javadov.catmouse.rest.CatMouseTestsHelper.getLogger;
import static org.junit.Assert.assertEquals;

/**
 * Created by asgar on 3/29/17.
 */
public class PlayerServiceTest {
    private Logger logger;

    @Before
    public void setUp() throws Exception {
        logger = getLogger();
    }

    @Test
    public void createPlayer() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(UriBuilder.fromUri("http://localhost:2005")).path("players");

        final String[] PLAYER_NAMES = {"Asgar", "Javadov", "Other"};
        for(String PLAYER_NAME: PLAYER_NAMES) {
            Response response = target
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(PLAYER_NAME, MediaType.TEXT_PLAIN_TYPE));
            String jsonString = response.readEntity(String.class);
            JsonObject json = Json.createReader(new StringReader(jsonString)).readObject();
            System.out.println(json);
            System.out.println(json.getInt("id"));
        }
//        Player player = (Player) response.getEntity();
//        assertEquals(PLAYER_NAME, player.getName());
    }

    @Test
    public void getActivePlayers() throws Exception {

    }

    @Test
    @Ignore
    public void testStaticHtmlFiles() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(UriBuilder.fromUri("http://localhost:2005"));
        Response response = webTarget.request().get();
        System.out.println(response.getStatus());
        assertEquals(200, response.getStatus());
    }

}