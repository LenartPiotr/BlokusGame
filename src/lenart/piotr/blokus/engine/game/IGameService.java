package lenart.piotr.blokus.engine.game;

import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.client.IGameClient;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;

import java.util.List;

public interface IGameService {
    // Settings
    public void setBoardSize(Vector2i size) throws WrongActionException;
    public void setPlayersCount(int count) throws WrongActionException;

    // Players
    public int registerClient(IGameClient client) throws WrongActionException;
    public void unregisterClient(IGameClient client) throws WrongActionException;
    public String getClientName(int clientIndex) throws WrongActionException;
    public int getPlayersCount();

    // Initialize game
    public void init() throws WrongActionException;

    // Game
    public List<IPuzzle> getPuzzleList(int index) throws WrongActionException;
    public void placePuzzle(IGameClient client, IPuzzle puzzle, Vector2i position) throws WrongActionException;
    public void giveUp(IGameClient client) throws WrongActionException;
}
