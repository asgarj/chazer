package org.javadov.catmouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    private static int counter = 0;
    @JsonIgnore
    private final CyclicBarrier barrier;

    private final int id;
    private final Player player1;
    private final Player player2;

    public static Game create(Message message) {
        return new Game(message.getRequestor(), message.getResponder());
    }

    private Game(Player player1, Player player2) {
        this.id = ++counter;
        this.player1 = player1;
        this.player2 = player2;
        this.barrier = new CyclicBarrier(2);

        this.player1.initialise(null);
        this.player2.initialise(this.player1);
        this.player1.setChaser(true);
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

    public boolean isAllowedToMove(int playerId) {
        Player me = this.getSelf(playerId);
        Player opponent = this.getOpponent(playerId);
        return me.getState().getStep() <= opponent.getState().getStep();
    }

    private Player getSelf(int playerId) {
        return playerId == player1.getId() ? player1 : player2;
    }

    private Player getOpponent(int playerId) {
        return playerId == player1.getId() ? player2 : player1;
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

    public int nSteps() {
        return player1.getState().getStep();
    }
}
