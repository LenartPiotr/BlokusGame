package lenart.piotr.blokus.engine.game;

import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.IGameClient;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;
import lenart.piotr.blokus.engine.puzzle.PuzzleGenerator;

import java.util.List;

public class GameService implements IGameService {
    private List<IGameClient> clients;
    private int playersCount;
    private Vector2i boardSize;
    private boolean inGame;
    private ClientData[] clientsData;
    private int turn;
    private int[][] board;

    @Override
    public void setBoardSize(Vector2i size) throws WrongActionException {
        if (inGame) throw new WrongActionException("Cannot change board size in game");
        if (size.x() < 3 || size.y() < 3) throw new WrongActionException("Board dimension cannot be less than 3");
        this.boardSize = new Vector2i(size);
    }

    @Override
    public void setPlayersCount(int count) throws WrongActionException {
        if (inGame) throw new WrongActionException("Cannot change players count in game");
        if (count < 1) throw new WrongActionException("Game must have at least one player");
        playersCount = count;
    }

    @Override
    public int registerClient(IGameClient client) throws WrongActionException {
        if (inGame) throw new WrongActionException("Cannot register new users during game");
        if (clients.contains(client)) throw new WrongActionException("This client already exists in game");
        clients.add(client);
        client.setIndex(clients.size() - 1);
        return clients.size() - 1;
    }

    private int getClientIndex(IGameClient client){
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).equals(client)) return i;
        }
        return -1;
    }

    @Override
    public void unregisterClient(IGameClient client) throws WrongActionException {
        int index = getClientIndex(client);
        if (index < 0) throw new WrongActionException("This client does not belong to this game");
        if (inGame) {
            giveUp(client);
        } else {
            clients.remove(index);
            for (int i = index; i < clients.size(); i++) {
                clients.get(i).setIndex(i);
            }
        }
    }

    @Override
    public String getClientName(int clientIndex) throws WrongActionException {
        if (clientIndex < 0 || clientIndex >= clients.size()) throw new WrongActionException("There is no client with this ID");
        return clients.get(clientIndex).getName();
    }

    @Override
    public int getPlayersCount() {
        return playersCount;
    }

    @Override
    public void init() throws WrongActionException {
        if (inGame) throw new WrongActionException("Game already started");
        if (playersCount != clients.size()) throw new WrongActionException("Count of players in room is not the same as registered");
        clientsData = new ClientData[playersCount];
        PuzzleGenerator generator = new PuzzleGenerator();
        for (int i = 0; i < playersCount; i++) {
            clientsData[i] = new ClientData(generator.getDefaultPuzzleSet());
        }
        board = new int[boardSize.x()][boardSize.y()];
        for (int x = 0; x < boardSize.x(); x++)
            for (int y = 0; y < boardSize.y(); y++){
                board[x][y] = -1;
            }
        inGame = true;
        turn = 0;
        clients.forEach(c -> c.changeTurn(turn));
    }

    @Override
    public List<IPuzzle> getPuzzleList(int index) throws WrongActionException {
        if (!inGame) throw new WrongActionException("Game does not exists");
        if (index < 0 || index >= playersCount) throw new WrongActionException("Incorrect index");
        return clientsData[index].getCopyOfPuzzlesList();
    }

    private int getBoardValueOrDefault(Vector2i position, int def){
        if (!position.inRange(boardSize)) return def;
        return board[position.x()][position.y()];
    }

    @Override
    public void placePuzzle(IGameClient client, IPuzzle puzzle, Vector2i position) throws WrongActionException {
        if (!inGame) throw new WrongActionException("Game does not exists");
        int index = getClientIndex(client);
        if (index < 0) throw new WrongActionException("This client does not belong to game");
        if (index != turn) throw new WrongActionException("Not your turn");
        if (!puzzle.getMinBound().inRange(boardSize)) throw new WrongActionException("Puzzle is not in board range");
        if (!puzzle.getMaxBound().inRange(boardSize)) throw new WrongActionException("Puzzle is not in board range");
        int cornerConnectionsCount = 0;
        int sideConnectionsCount = 0;
        Vector2i[] sideVectors = new Vector2i[]{
                new Vector2i(0,1), new Vector2i(1,0), new Vector2i(0,-1), new Vector2i(-1,0)
        };
        Vector2i[] cornerVectors = new Vector2i[]{
                new Vector2i(1,1), new Vector2i(1,-1), new Vector2i(-1,1), new Vector2i(-1,-1)
        };
        for (Vector2i pos : puzzle.getFields()){
            if (getBoardValueOrDefault(pos.add(position), -1) >= 0) throw new WrongActionException("Your puzzle must be placed on empty fields");
            if (sideConnectionsCount == 0)
                for (Vector2i sv : sideVectors)
                    if (getBoardValueOrDefault(pos.add(sv).add(position), -1) == index) sideConnectionsCount++;
            if (cornerConnectionsCount == 0)
                for (Vector2i cv : cornerVectors)
                    if (getBoardValueOrDefault(pos.add(cv).add(position), -1) == index) cornerConnectionsCount++;
        }
        if (cornerConnectionsCount == 0) throw new WrongActionException("Your puzzle must have corner connection with another your puzzle");
        if (sideConnectionsCount != 0) throw new WrongActionException("Your puzzle can not have side connection with another your puzzle");
        if (!clientsData[index].removePuzzle(puzzle)) throw new WrongActionException("This puzzle does not exists in your inventory");
        for (Vector2i pos : puzzle.getFields()) {
            Vector2i pos2 = pos.add(position);
            board[pos2.x()][pos2.y()] = index;
        }
        clients.forEach(c -> c.placedPuzzle(index, position, puzzle));
        do {
            turn++;
            turn %= playersCount;
        } while (turn != index && clientsData[turn].passed());
        clients.forEach(c -> c.changeTurn(turn));
    }

    @Override
    public void giveUp(IGameClient client) throws WrongActionException {
        if (!inGame) throw new WrongActionException("Game does not exists");
        int index = getClientIndex(client);
        if (index < 0) throw new WrongActionException("This client does not belong to game");
        clientsData[index].setPass();
        clients.forEach(c -> c.playerGaveUp(index));
        int playersInGame = 0;
        for (ClientData d : clientsData) {
            if (!d.passed()) playersInGame++;
        }
        if (playersInGame == 0) {
            endgame();
            return;
        }
        if (turn != index) return;
        do {
            turn++;
            turn %= playersCount;
        } while (clientsData[turn].passed());
        clients.forEach(c -> c.changeTurn(turn));
    }

    private void endgame(){
        EndgameData endgameData = new EndgameData();
        // fill endgame data
        clients.forEach(c -> c.endgame(endgameData));
        inGame = false;
        clients.removeIf(c -> !c.active());
    }
}
