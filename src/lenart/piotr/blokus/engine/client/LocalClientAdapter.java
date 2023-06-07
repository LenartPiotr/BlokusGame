package lenart.piotr.blokus.engine.client;

import lenart.piotr.blokus.basic.ICallback;
import lenart.piotr.blokus.basic.ICallback3;
import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;
import lenart.piotr.blokus.engine.game.IGameService;
import lenart.piotr.blokus.engine.game.endgame.EndgameData;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;

import java.util.List;

public class LocalClientAdapter implements IGameClient, IClientAdapter{
    private int index;
    private String name;
    private IGameService gameService;

    private ICallback<Integer> changeTurnCallback;
    private ICallback<EndgameData> endgameDataCallback;
    private ICallback<Integer> playerGaveUpCallback;
    private ICallback3<Integer, Vector2i, IPuzzle> puzzlePlacedCallback;
    private ICallback<Integer> changePlayersCountCallback;

    public LocalClientAdapter(String name, IGameService gameService) throws WrongActionException {
        this.name = name;
        this.gameService = gameService;
        index = gameService.registerClient(this);
    }

    @Override
    public void onChangeTurn(ICallback<Integer> callback) {
        changeTurnCallback = callback;
    }

    @Override
    public void onEndgame(ICallback<EndgameData> callback) {
        endgameDataCallback = callback;
    }

    @Override
    public void onPlayerGaveUp(ICallback<Integer> callback) {
        playerGaveUpCallback = callback;
    }

    @Override
    public void onPuzzlePlaced(ICallback3<Integer, Vector2i, IPuzzle> callback) {
        puzzlePlacedCallback = callback;
    }

    @Override
    public void onChangePlayerCount(ICallback<Integer> callback) {
        changePlayersCountCallback = callback;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void changeTurn(int index) {
        if (changeTurnCallback != null)
            changeTurnCallback.run(index);
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void endgame(EndgameData data) {
        if (endgameDataCallback != null)
            endgameDataCallback.run(data);
    }

    @Override
    public void playerGaveUp(int index) {
        if (playerGaveUpCallback != null)
            playerGaveUpCallback.run(index);
    }

    @Override
    public boolean active() {
        return true;
    }

    @Override
    public void placedPuzzle(int playerIndex, Vector2i position, IPuzzle puzzle) {
        if (puzzlePlacedCallback != null)
            puzzlePlacedCallback.run(playerIndex, position, puzzle);
    }

    @Override
    public void changePlayerCount(int count) {
        if (changePlayersCountCallback != null)
            changePlayersCountCallback.run(count);
    }

    @Override
    public List<IPuzzle> getPuzzleList(int index) throws WrongActionException {
        return gameService.getPuzzleList(index);
    }

    @Override
    public void placePuzzle(IPuzzle puzzle, Vector2i position) throws WrongActionException {
        gameService.placePuzzle(this, puzzle, position);
    }

    @Override
    public int getMaxPlayersCount() {
        return gameService.getPlayersCount();
    }

    @Override
    public String getPlayerName(int index) throws WrongActionException {
        return gameService.getClientName(index);
    }

    @Override
    public void giveUp() throws WrongActionException {
        gameService.giveUp(this);
    }
}
