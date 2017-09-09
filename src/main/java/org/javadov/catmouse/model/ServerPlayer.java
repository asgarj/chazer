package org.javadov.catmouse.model;

import org.javadov.catmouse.rest.GameService;
import org.javadov.catmouse.rest.PlayerService;
import static org.javadov.catmouse.CatMouseGame.logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by asgar on 4/12/17.
 */
public class ServerPlayer {
    private final String BASE_URL = "http://localhost:2005";
    final Player serverPlayer;
    Client client;
    WebTarget webTarget;

    public ServerPlayer(String name) {
        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URL);
        Response response = webTarget.path("players").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(name, MediaType.TEXT_PLAIN_TYPE));
        String jsonString = response.readEntity(String.class);
        JsonObject json = Json.createReader(new StringReader(jsonString)).readObject();
        serverPlayer = PlayerService.getPlayerById(json.getInt("id"));
    }

    public void awaitGame() {
        logger.info("awaiting message");
        WebTarget webTarget = client.target(BASE_URL).path("messages").path("inbox").path("" + serverPlayer.getId());
        Response response;
        List<LinkedHashMap> inbox;
        do {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            response = webTarget.request().get();
            inbox = response.readEntity(List.class);
        } while (inbox.isEmpty());
        int messageId = (int) inbox.get(0).get("messageId");
        logger.info("request message id "+messageId);
        webTarget = client.target(BASE_URL).path("messages").path("respond").path(""+messageId).path(""+serverPlayer.getId());
        response = webTarget.request(MediaType.TEXT_PLAIN_TYPE).post(null);
        String gameId = response.readEntity(String.class);
        playGame(messageId, gameId);
    }

    private void playGame(int messageId, String gameId) {
        Game game = GameService.getGameByMessageId(messageId);
        assert game.getId() == Integer.valueOf(gameId);

        Response response;
        Random rand = new Random();
        int nOfMoves;
        Move[] moves = Move.values();
        while (!game.isOver()) {
            nOfMoves = 1;
            if (serverPlayer.getRow() > 1)
                moves[nOfMoves++] = Move.UP;
            if (serverPlayer.getColumn() < Player.COLUMNS)
                moves[nOfMoves++] = Move.RIGHT;
            if (serverPlayer.getRow() < Player.ROWS)
                moves[nOfMoves++] = Move.DOWN;
            if (serverPlayer.getColumn() > 1)
                moves[nOfMoves++] = Move.LEFT;
            int move = rand.nextInt(nOfMoves);
            if (move == 0) move = rand.nextInt(nOfMoves);

            LinkedHashMap<String, Integer> json = new LinkedHashMap<>(2);
            json.put("row", serverPlayer.getRow()+moves[move].row);
            json.put("col", serverPlayer.getColumn()+moves[move].column);

            do {
                response = ClientBuilder.newClient().target(BASE_URL).path("games").path("move").path(gameId).path("" + serverPlayer.getId())
                        .request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(json, MediaType.APPLICATION_JSON_TYPE));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (response.getStatus() >= 400);
        }

    }
    private enum Move {
        SAME(0, 0), UP(-1, 0), RIGHT(0, 1), DOWN(1, 0), LEFT(0, -1);
        public final int row;
        public final int column;

        Move(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }
}
