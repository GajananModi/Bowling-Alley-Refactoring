package Model;

public class GameStateManager {
    private GameState state;

    public void setStateObject(State st) {
        if (st.equals(State.HALTED)) {
            state = new GameStateHalted();
        } else if (st.equals(State.RUNNING)) {
            state = new GameStateRunning();
        } else if (st.equals(State.FINISHED)) {
            state = new GameStateFinished();
        }
    }

    public GameState getState() {
        return state;
    }
}
