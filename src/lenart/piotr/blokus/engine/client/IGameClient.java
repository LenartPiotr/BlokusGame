package lenart.piotr.blokus.engine.client;

import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;
import lenart.piotr.blokus.engine.game.endgame.EndgameData;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;

import java.util.List;

public interface IGameClient {
    public int getIndex();

    // Actions from IGameService
    public void changeTurn(int index);
    public void setIndex(int index);
    public String getName();
    public void endgame(EndgameData data);
    public void playerGaveUp(int index);
    public boolean active();
    public void placedPuzzle(int playerIndex, Vector2i position, IPuzzle puzzle);
    public void changePlayerCount(int count);

    // Actions to IGameService
    public List<IPuzzle> getPuzzleList(int index) throws WrongActionException;
    public void placePuzzle(IPuzzle puzzle, Vector2i position) throws WrongActionException;
    public int getMaxPlayersCount();
    public int getPlayersCount();
    public String getPlayerName(int index) throws WrongActionException;
    public void giveUp() throws WrongActionException;
    public Vector2i getBoardSize();
}
