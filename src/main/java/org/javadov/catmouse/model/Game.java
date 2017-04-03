package org.javadov.catmouse.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * This class encapsulates the game object and
 * provides functionality to manipulate the game.
 * @author asgar
 */

@XmlRootElement
public class Game {
    private static int counter = 0;

    private final CyclicBarrier barrier;

    private final int id;
    private Player player1;
    private Player player2;

    public Game(Player player1, Player player2) {
        this.id = ++counter;
        this.player1 = player1;
        this.player2 = player2;
        this.player1.setChaser(true);

        this.barrier = new CyclicBarrier(2);
    }

    public static Game create(Player player1, Player player2) {
        player1.initialise();
        player2.initialise();
        Game game = new Game(player1, player2);
        return game;
    }

    public Player takeAction(int playerId, int row, int col) {
        Player player = (playerId == player1.getId()) ? player1 : player2;
        player.moveTo(row, col);

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        return player;
    }

    public boolean isOver() {
        return player1.getState().equals(player2.getState());
    }

    public int getId() {
        return id;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }
}
