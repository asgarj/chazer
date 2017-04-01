package org.javadov.catmouse.rest;

import org.javadov.catmouse.model.Player;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;

/**
 * Created by asgar on 3/23/17.
 */

@Path("/action")
public class ActionProcessor {
    private static final int WIDTH = 8;
    private static final int HEIGHT = 6;
    private static final Object lock = new Object();
    static Player player1;
    static Player player2;

    @Path("/{id}/{row}/{col}")
    @GET
    @Produces("text/plain")
    public String move(@PathParam("id") Integer playerId,
                       @PathParam("row") Integer row,
                       @PathParam("col") Integer column) {
        synchronized (lock) {
            Player player = (playerId == 1) ? player1 : player2;
            Player otherPlayer = (playerId == 2) ? player1 : player2;

            player.moveTo(row, column);
            if (player.equals(otherPlayer)) {
                gameOver();
                return "newgame over";
//                return Response.status(201).entity("GameDeprecated Over").build();
            } else {
                return "moved";
//                return Response.ok().build();
            }
        }
    }

    private void gameOver() {
        player1.goToInitialState();
        player2.goToInitialState();
    }


}
