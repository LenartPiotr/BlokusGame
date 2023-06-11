package lenart.piotr.blokus.engine.client;

import lenart.piotr.blokus.basic.ICallback1;
import lenart.piotr.blokus.basic.ICallback3;
import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;
import lenart.piotr.blokus.engine.game.endgame.EndgameData;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;

import java.util.List;

public interface IClientAdapter {
    public int getIndex();
    public String getName();

    public void onChangeTurn(ICallback1<Integer> callback);
    public void onEndgame(ICallback1<EndgameData> callback);
    public void onPlayerGaveUp(ICallback1<Integer> callback);
    public void onPuzzlePlaced(ICallback3<Integer, Vector2i, IPuzzle> callback);
    public void onChangePlayerCount(ICallback1<Integer> callback);

    public List<IPuzzle> getPuzzleList(int index) throws WrongActionException;
    public void placePuzzle(IPuzzle puzzle, Vector2i position) throws WrongActionException;
    public int getMaxPlayersCount();
    public int getPlayersCount();
    public String getPlayerName(int index) throws WrongActionException;
    public void giveUp() throws WrongActionException;
    public Vector2i getBoardSize();
}
