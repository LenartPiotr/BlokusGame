package lenart.piotr.blokus.engine;

import lenart.piotr.blokus.basic.Vector2i;

import java.util.List;

public interface IGameClient {
    public int getIndex();

    // Actions from IGameService
    public void changeTurn(int index);
    public void setIndex(int index);
    public String getName();
    public void endgame(EndgameData data);

    // Actions to IGameService
    public List<IPuzzle> getPuzzleList(int index);
    public void placePuzzle(IPuzzle puzzle, Vector2i position);
    public int getPlayersCount();
    public String getPlayerName(int index);
}
