package org.javadov.catmouse.rest;

import org.javadov.catmouse.model.Game;
import org.javadov.catmouse.model.Player;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asgar on 4/1/17.
 */

@Path("/games")
public class GameService {
    private static Map<Integer, Game> games = new HashMap<>();

    @POST
    @Path("/newgame/{requestorId}/{responderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handle(@PathParam("requestorId") int requestorId,
                           @PathParam("responderId") int responderId) {
        Player requestor = PlayerService.getPlayerById(requestorId);
        Player responder = PlayerService.getPlayerById(responderId);

        Game game = Game.create(requestor, responder);
        games.put(game.getId(), game);

        JsonObject player1 = Json.createObjectBuilder()
                .add("name", game.getPlayer1().getName())
                .add("id", game.getPlayer1().getId())
                .build();
        JsonObject player2 = Json.createObjectBuilder()
                .add("name", game.getPlayer2().getName())
                .add("id", game.getPlayer2().getId())
                .build();

        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        jsonBuilder.add("gameId", game.getId());
        jsonBuilder.add("player1", player1);
        jsonBuilder.add("player2", player2);
        jsonBuilder.add("chaser", game.getPlayer1().isChaser() ? 1 : 2);

        String jsonString = jsonBuilder.build().toString();

        return Response
                .status(Response.Status.CREATED)
                .entity(jsonString)
                .build();
    }

    @POST
    @Path("/{gameId}/{playerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response moveTo(@PathParam("gameId") int gameId,
                           @PathParam("playerId") int playerId,
                           JsonObject json) {
        Game game = games.get(gameId);
        int row = json.getInt("row");
        int col = json.getInt("col");
        Player player = game.takeAction(playerId, row, col);
        if (game.isOver()) {
            return Response.ok()
                    .header("state", "game over")
                    .entity((player.isChaser()) ?
                            "Congrats! You caught your opponent!" :
                            "Oh.. Your opponent caught you :(")
                    .build();
        } else {
            return Response.ok(game).build();
        }
    }
}
