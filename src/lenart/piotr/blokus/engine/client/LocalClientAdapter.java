package lenart.piotr.blokus.engine.client;

import lenart.piotr.blokus.basic.*;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;
import lenart.piotr.blokus.engine.game.IGameService;
import lenart.piotr.blokus.engine.game.endgame.EndgameData;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;

import java.util.List;

public class LocalClientAdapter implements IGameClient, IClientAdapter{
    private int index;
    private String name;
    private IGameService gameService;

    public LocalClientAdapter(String name, IGameService gameService) throws WrongActionException {
        this.name = name;
        this.gameService = gameService;
        index = gameService.registerClient(this);
    }

    private ICallback1<Integer> changeTurnCallback;
    @Override
    public void onChangeTurn(ICallback1<Integer> callback) { changeTurnCallback = callback; }

    private ICallback1<EndgameData> endgameDataCallback;
    @Override
    public void onEndgame(ICallback1<EndgameData> callback) { endgameDataCallback = callback; }

    private ICallback1<Integer> playerGaveUpCallback;
    @Override
    public void onPlayerGaveUp(ICallback1<Integer> callback) { playerGaveUpCallback = callback; }

    private ICallback3<Integer, Vector2i, IPuzzle> puzzlePlacedCallback;
    @Override
    public void onPuzzlePlaced(ICallback3<Integer, Vector2i, IPuzzle> callback) { puzzlePlacedCallback = callback; }

    private ICallback1<Integer> changePlayersCountCallback;
    @Override
    public void onChangePlayerCount(ICallback1<Integer> callback) { changePlayersCountCallback = callback; }

    private ICallback2<Integer, List<IPuzzle>> onGetPuzzleListCallback;
    @Override
    public void onGetPuzzleList(ICallback2<Integer, List<IPuzzle>> callback) { onGetPuzzleListCallback = callback; }

    private ICallback1<Integer> onGetMaxPlayersCountCallback;
    @Override
    public void onGetMaxPlayersCount(ICallback1<Integer> callback) { onGetMaxPlayersCountCallback = callback; }

    private ICallback1<Integer> onGetPlayersCountCallback;
    @Override
    public void onGetPlayersCount(ICallback1<Integer> callback) { onGetPlayersCountCallback = callback; }

    private ICallback2<Integer, String> onGetPlayerNameCallback;
    @Override
    public void onGetPlayerName(ICallback2<Integer, String> callback) { onGetPlayerNameCallback = callback; }

    private ICallback1<Vector2i> onGetBoardSizeCallback;
    @Override
    public void onGetBoardSize(ICallback1<Vector2i> callback) { onGetBoardSizeCallback = callback; }

    private ICallback1<String> onWrongActionCallback;
    @Override
    public void onWrongAction(ICallback1<String> callback) { onWrongActionCallback = callback; }

    private void printWrongAction(String message) {
        if (onWrongActionCallback != null)
            onWrongActionCallback.run(message);
    }

    @Override
    public void getPuzzleList(int index) {
        if (onGetPuzzleListCallback != null) {
            try {
                onGetPuzzleListCallback.run(index, gameService.getPuzzleList(index));
            } catch (WrongActionException e) {
                printWrongAction(e.getTextToDisplay());
            }
        }
    }

    @Override
    public void placePuzzle(IPuzzle puzzle, Vector2i position) {
        try {
            gameService.placePuzzle(this, puzzle, position);
        } catch (WrongActionException e) {
            printWrongAction(e.getTextToDisplay());
        }
    }

    @Override
    public void getMaxPlayersCount() {
        if (onGetMaxPlayersCountCallback != null) {
            onGetMaxPlayersCountCallback.run(gameService.getMaxPlayersCount());
        }
    }

    @Override
    public void getPlayersCount() {
        if (onGetPlayersCountCallback != null) {
            onGetPlayersCountCallback.run(gameService.getPlayersCount());
        }
    }

    @Override
    public void getPlayerName(int index) {
        if (onGetPlayerNameCallback != null) {
            try {
                onGetPlayerNameCallback.run(index, gameService.getClientName(index));
            } catch (WrongActionException e) {
                printWrongAction(e.getTextToDisplay());
            }
        }
    }

    @Override
    public void giveUp() {
        try {
            gameService.giveUp(this);
        } catch (WrongActionException e) {
            printWrongAction(e.getTextToDisplay());
        }
    }

    @Override
    public void getBoardSize() {
        if (onGetBoardSizeCallback != null) {
            onGetBoardSizeCallback.run(gameService.getBoardSize());
        }
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
}
