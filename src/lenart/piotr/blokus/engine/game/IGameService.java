package lenart.piotr.blokus.engine.game;

import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.client.IClient;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;

public interface IGameService {
    // Settings
    public void setBoardSize(Vector2i size) throws WrongActionException;
    public void setPlayersCount(int count) throws WrongActionException;
    public Vector2i getBoardSize();

    // Players
    public int registerClient(IClient client) throws WrongActionException;
    public void unregisterClient(IClient client) throws WrongActionException;

    // Initialize game
    public void init() throws WrongActionException;
}
