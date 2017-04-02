package org.javadov.catmouse.rest;

import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import static org.junit.Assert.*;

/**
 * Created by ajavadov on 4/2/2017.
 */
public class GameServiceTest {
    final String BASE_URL = "http://localhost:2005/games";

    @Test
    public void handle() throws Exception {
        CatMouseTestsHelper.addDummyPlayers();
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(UriBuilder.fromUri(BASE_URL).build()).path("newgame/1/2");
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(null);
        assertEquals(201, response.getStatus());
        Object obj = response.getEntity();
        JsonObject json = (JsonObject) response.getEntity();
        assertTrue(json.getInt("gameId") != 0);
        int chaser = json.getInt("chaser");
        assertTrue(chaser == 1 || chaser == 2);
    }

    @Test
    public void moveTo() throws Exception {
    }

}