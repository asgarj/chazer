package org.javadov.catmouse.rest;

import javafx.scene.media.MediaPlayer;
import org.javadov.catmouse.model.Player;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by asgar on 3/29/17.
 */
@Path("/players")
public class PlayerService {
    private static final List<Player> players = new ArrayList<>();

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPlayer(String name) {
        Player newPlayer = Player.createPlayer(name);
        players.add(newPlayer);
        NewCookie cookie = new NewCookie("playerId", "" + newPlayer.getId(), "/", null, null, 8*60*60, false);
        return Response.ok(newPlayer, MediaType.APPLICATION_JSON_TYPE)
                .cookie(cookie)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActivePlayers() {
        return Response.ok(players, MediaType.APPLICATION_JSON_TYPE).build();
    }

    @DELETE
    @Path("/{playerId}")
    public Response deletePlayer(@PathParam("playerId") int playerId) {
        Player player = PlayerService.getPlayerById(playerId);
        PlayerService.dispose(player);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    static Player getPlayerById(int id) {
        Optional<Player> player = players.stream().filter(p -> p.getId() == id).findFirst();
        return player.orElse(null);
    }

    static void dispose(Player player) {
        players.remove(player);
        player = null;
    }
}
