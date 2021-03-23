package Model;

public class GameStateHalted implements GameState {
    public State getState() {
        return State.HALTED;
    }
}
