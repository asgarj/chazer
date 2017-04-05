package org.javadov.catmouse.rest;

import org.javadov.catmouse.model.Game;
import org.javadov.catmouse.model.Message;
import org.javadov.catmouse.model.Player;
import static org.javadov.catmouse.CatMouseGame.logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.StringReader;
import static java.lang.String.format;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by asgar on 4/1/17.
 */

@Path("/games")
public class GameService {
    private static Map<Integer, Game> games = new HashMap<>();
    private static Map<Integer, Game> msgToGame = new HashMap<>();

    @GET
    @Path("/new/{messageId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Game handle(@PathParam("messageId") int messageId) {
        Message message = MessageService.getMessageById(messageId);
        Game game = Game.create(message);
        games.put(game.getId(), game);
        msgToGame.put(message.getMessageId(), game);
        logger.info(String.format("Created new game for players %d - %d with id:%d",
                game.getPlayer1().getId(), game.getPlayer2().getId(), game.getId()));

        return game;
    }

    @GET
    @Path("/{gameId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Game getGame(@PathParam("gameId") int gameId) {
        logger.fine(String.format("GET game [%d]", gameId));
        return games.get(gameId);
    }


    @POST
    @Path("/move/{gameId}/{playerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response moveTo(@PathParam("gameId") int gameId,
                           @PathParam("playerId") int playerId,
                           String jsonString) {
        Game game = games.get(gameId);
        if (game == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        JsonObject json = Json.createReader(new StringReader(jsonString)).readObject();
        int row = json.getInt("row");
        int col = json.getInt("col");

        Player player = game.takeAction(playerId, row, col);
        logger.info(format("%s MOVED TO (%d, %d)", player.getName(), row, col));
        if (game.isOver()) {
            String message = (player.isChaser()) ?
                    format("Congrats! You caught your opponent in %d steps!", game.nSteps()) :
                    format("Oh.. Your opponent caught you in %d steps :(", game.nSteps());
            String responseJsonString = Json.createObjectBuilder()
                    .add("message", message).build().toString();
            GameService.dispose(game);
            return Response.ok()
                    .header("state", "game over")
                    .entity(responseJsonString)
                    .build();
        } else {
            return Response.ok(game).build();
        }
    }

    public static Game getGameByMessageId(int messageId) {
        final Game[] game = {null};
        CompletableFuture<Void> result = CompletableFuture.runAsync(() -> {
            do {
                try {
                    TimeUnit.MILLISECONDS.sleep(500L);
                } catch (InterruptedException e) {
                    // todo: log
                }
                game[0] = msgToGame.get(messageId);
            }
            while (game[0] == null);
        });
        try {
            result.get(10L, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
//            e.printStackTrace();
        }
        Optional.ofNullable(game[0].getId());
        return game[0];
    }

    static void dispose(Game game) {
        PlayerService.dispose(game.getPlayer1());
        PlayerService.dispose(game.getPlayer2());
        games.remove(game.getId());
        game = null;
    }
}
