package org.javadov.catmouse.rest;

import static org.javadov.catmouse.CatMouseGame.logger;
import org.javadov.catmouse.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by asgar on 3/29/17.
 */
@Path("/")
public class PlayerService {
    private static final List<Player> players = new ArrayList<>();
    private static int counter = 0;

    @Context UriInfo uriInfo;

    @Path("/newplayer")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Player createPlayer(String name) {
        Player newPlayer = Player.createPlayer(name, uriInfo.getRequestUri());
        players.add(newPlayer);
        logger.info("CREATE PLAYER REQUEST: " + name);
        logger.info(players.toString());
        logger.info(uriInfo.toString());
        return newPlayer;
    }

    @Path("/players")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivePlayers() {
        logger.info(++counter + " : request received");
        return Response.ok(players, MediaType.APPLICATION_JSON_TYPE).build();
    }

    static Player getPlayerById(int id) {
        Optional<Player> player = players.stream().filter(p -> p.getId() == id).findFirst();
        return player.orElse(null);
    }
}
