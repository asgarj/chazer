package org.javadov.catmouse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.Random;

/**
 * Created by asgar on 3/24/17.
 */

@XmlRootElement
public class Player {
    private static final int ROWS = 3;
    private static final int COLUMNS = 5;
    private static int counter = 0;

    private int id;
    private final String name;
    @JsonIgnore
    private State state;
    private boolean idle = true;
    private URI uri;
    private boolean isChaser;

    public static Player createPlayer(String name) {
        return new Player(name);
    }

    public static Player createPlayer(String name, URI uri) {
        return new Player(name, uri);
    }

    private Player(String name) {
        this.id = ++counter;
        this.name = name;
        this.state = new State();
    }

    private Player(String name, URI uri) {
        this(name);
        this.uri = uri;
    }

    public void goToInitialState() {
        if (this.id == 1)
            this.state.initialise(true);
        else
            this.state.initialise(false);
    }

    public void moveTo(int row, int col) {
        this.state.moveTo(row, col);
    }

    public boolean validMove(int row, int col) {
        int distance = Math.abs(this.getRow() - row) + Math.abs(this.getColumn() - col);
        return isChaser ? distance <= 2 : distance <= 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (id != player.id) return false;
        return state != null ? state.equals(player.state) : player.state == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", state=" + state +
                '}';
    }

    public int getId() {
        return id;
    }

    public int getRow() {
        return this.state.row;
    }

    public int getColumn() {
        return this.state.col;
    }

    public String getName() {
        return name;
    }

    public boolean isChaser() {
        return isChaser;
    }

    public void setChaser(boolean chaser) {
        isChaser = chaser;
    }

    public State getState() {
        return state;
    }

    protected static class State {
        private static Random random = new Random();
        int step, row, col;

        public State() {
            this.row = random.nextInt(ROWS) + 1;
            this.col = random.nextInt(COLUMNS) + 1;
        }

        public State(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void moveTo(int row, int col) {
            this.row = row;
            this.col = col;
            this.step++;
            return;
        }

        public void initialise(boolean topLeft) {
            this.step = 0;
            if (topLeft) {
                this.row = 1;
                this.col = 1;
            } else {
                this.row = ROWS;
                this.col = COLUMNS;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof State)) return false;

            State state = (State) o;

            if (step != state.step) return false;
            if (row != state.row) return false;
            return col == state.col;
        }

        @Override
        public int hashCode() {
            int result = step;
            result = 31 * result + row;
            result = 31 * result + col;
            return result;
        }

        @Override
        public String toString() {
            return "State{" +
                    "step=" + step +
                    ", row=" + row +
                    ", col=" + col +
                    '}';
        }
    }
}
