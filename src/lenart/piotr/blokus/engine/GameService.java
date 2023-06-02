package lenart.piotr.blokus.engine;

import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;

import java.util.List;

public class GameService implements IGameService{
    private List<IGameClient> clients;
    private int playersCount;


    @Override
    public void setBoardSize(Vector2i size) {

    }

    @Override
    public void setPlayersCount(int count) {

    }

    @Override
    public int registerClient(IGameClient client) {
        return 0;
    }

    @Override
    public void unregisterClient(IGameClient client) {

    }

    @Override
    public String getClientName(int clientIndex) {
        return null;
    }

    @Override
    public int getPlayersCount() {
        return 0;
    }

    @Override
    public void init() {

    }

    @Override
    public List<IPuzzle> getPuzzleList(int index) {
        return null;
    }

    @Override
    public void placePuzzle(IGameClient client, IPuzzle puzzle, Vector2i position) {

    }

    @Override
    public void giveUp(IGameClient client) {

    }
}
