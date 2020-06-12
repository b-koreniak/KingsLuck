package koreniak.kingsluck.server.manager;

import koreniak.kingsluck.core.leader.Leader;

import java.util.Objects;

public class GameState {
    private State state;

    private Leader winner;
    private Leader looser;

    public GameState(State state) {
        this.state = state;
    }

    public GameState(State state, Leader winner, Leader looser) {
        this.state = state;
        this.winner = winner;
        this.looser = looser;
    }

    public State getState() {
        return state;
    }

    public Leader getWinner() {
        return winner;
    }

    public Leader getLooser() {
        return looser;
    }

    public enum State {
        VICTORY, DRAW, CONTINUES;
    }
}