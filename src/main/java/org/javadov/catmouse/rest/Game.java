package org.javadov.catmouse.rest;

import org.javadov.catmouse.model.Player;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.javadov.catmouse.CatMouseGame.logger;

/**
 * Created by asgar on 3/24/17.
 */

@Path("/")
public class Game {
    private static final List<Player> players = new ArrayList<>();

//    @GET
//    public void homepage() {}

    @Path("/newgame")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    public Response newGame(@Context Request request, MultivaluedMap<String, String> formData) throws IOException {
        logger.info(formData.entrySet().toString());
        logger.info(request.toString());

        Player player = Player.createPlayer("");
        players.add(player);

        Response response = Response.ok(player, MediaType.APPLICATION_JSON_TYPE).build();
        logger.info("RESPONSE: " + response.toString());
        return response;
    }
}
