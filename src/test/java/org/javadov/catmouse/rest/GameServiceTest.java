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
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ajavadov on 4/2/2017.
 */
public class GameServiceTest {
    final String BASE_URL = "http://localhost:2005/games";

    @Test
    public void testJson() {
        JsonObject json = Json.createObjectBuilder()
                .add("id", 1)
                .add("name", "Asgar")
                .build();
        String jsonStr = json.toString();
        System.out.println(json);
    }

    @Test
    public void handle() throws Exception {
        //CatMouseTestsHelper.addDummyPlayers();
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(UriBuilder.fromUri(BASE_URL).build()).path("newgame/1/2");
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(201, response.getStatus());
        String jsonString = response.readEntity(String.class);
        JsonObject json = Json.createReader(new StringReader(jsonString)).readObject();
        int gameId = json.getInt("gameId");
        assertTrue(gameId != 0);
        int chaser = json.getInt("chaser");
        assertTrue(chaser == 1 || chaser == 2);
    }

    @Test
    public void moveTo() throws Exception {
        JsonObject json = Json.createReader(new StringReader("{\"row\":1,\"col\":3}")).readObject();
        int row = json.getInt("row");
        int col = json.getInt("col");
        assertEquals(1, row);
        assertEquals(3, col);
    }

}