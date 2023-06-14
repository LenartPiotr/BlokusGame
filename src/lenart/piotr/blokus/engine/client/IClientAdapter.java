package lenart.piotr.blokus.engine.client;

import lenart.piotr.blokus.basic.ICallback1;
import lenart.piotr.blokus.basic.ICallback2;
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

    public void onGetPuzzleList(ICallback2<Integer, List<IPuzzle>> callback);
    public void onGetMaxPlayersCount(ICallback1<Integer> callback);
    public void onGetPlayersCount(ICallback1<Integer> callback);
    public void onGetPlayerName(ICallback2<Integer, String> callback);
    public void onGetBoardSize(ICallback1<Vector2i> callback);

    public void onWrongAction(ICallback1<String> callback);

    public void getPuzzleList(int index);
    public void placePuzzle(IPuzzle puzzle, Vector2i position);
    public void getMaxPlayersCount();
    public void getPlayersCount();
    public void getPlayerName(int index);
    public void giveUp();
    public void getBoardSize();
}
