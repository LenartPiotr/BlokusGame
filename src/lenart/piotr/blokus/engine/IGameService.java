package lenart.piotr.blokus.engine;

import lenart.piotr.blokus.basic.Vector2i;

import java.util.List;

public interface IGameService {
    // Settings
    public void setBoardSize(Vector2i size);
    public void setPlayersCount(int count);

    // Players
    public int registerClient(IGameClient client);
    public void unregisterClient(IGameClient client);
    public String getClientName(int clientIndex);
    public int getPlayersCount();

    // Initialize game
    public void init();

    // Game
    public List<IPuzzle> getPuzzleList(int index);
    public void placePuzzle(IGameClient client, IPuzzle puzzle, Vector2i position);
}
