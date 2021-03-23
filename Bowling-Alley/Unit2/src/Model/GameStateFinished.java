package Model;

public class GameStateFinished implements GameState {
    public State getState() {
        return State.FINISHED;
    }
}
