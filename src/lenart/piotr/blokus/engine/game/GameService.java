package lenart.piotr.blokus.engine.game;

import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.client.IClient;
import lenart.piotr.blokus.engine.exceptions.WrongActionException;
import lenart.piotr.blokus.engine.game.endgame.EndgameClientData;
import lenart.piotr.blokus.engine.game.endgame.EndgameData;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;
import lenart.piotr.blokus.engine.puzzle.PuzzleGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameService implements IGameService {
    private final List<PlayerData> clients;
    private int playersCount;
    private Vector2i boardSize;
    private boolean inGame;
    private int turn;
    private int[][] board;
    private int defaultPuzzlePiecesCount;

    public GameService() {
        clients = new ArrayList<>();
    }

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
    public Vector2i getBoardSize() {
        return new Vector2i(boardSize);
    }

    @Override
    public int registerClient(IClient client) throws WrongActionException {
        if (inGame) throw new WrongActionException("Cannot register new users during game");
        if (getClientIndex(client) != -1) throw new WrongActionException("This client already exists in game");
        clients.add(new PlayerData(client, new ClientData()));
        setupClientCallbacks(client);
        client.onDisconnect(() -> {
            try {
                unregisterClient(client);
            } catch (WrongActionException ignored) { }
        });
        client.invoke("getName", 0);
        client.invoke("setIndex", clients.size() - 1);
        broadcast("changePlayerCount", clients.size());
        return clients.size() - 1;
    }

    private void broadcast(String key, Object message) {
        clients.forEach(c -> c.client.invoke(key, message));
    }

    private void setupClientCallbacks(IClient client) {
        client.on("getIndex", ignored -> {
            client.invoke("setIndex", getClientIndex(client));
        });
        client.on("getMaxPlayersCount", ignored -> {
            client.invoke("setMaxPlayersCount", playersCount);
        });
        client.on("getPlayersCount", ignored -> {
            client.invoke("setPlayersCount", clients.size());
        });
        client.on("placePuzzle", data -> {
            PuzzlePlaceRecord puzzlePlaceData = (PuzzlePlaceRecord) data;
            try {
                placePuzzle(client, puzzlePlaceData.puzzle, puzzlePlaceData.position);
            } catch (WrongActionException e) {
                client.invoke("wrongAction", e.getTextToDisplay());
            }
        });
        client.on("setName", data -> {
            int index = getClientIndex(client);
            if (index != -1) {
                clients.get(index).data.setName((String)data);
                broadcast("playerChangeName", new PlayerChangeNickRecord(index, (String) data));
            }
        });
        client.on("getPuzzleList", data -> {
            int index = (int) data;
            if (index < 0 || index >= clients.size()) return;
            client.invoke("setPuzzleList", new PlayerPuzzleListRecord(index, clients.get(index).data.getCopyOfPuzzlesList()));
        });
        client.on("getPlayersNicks", ignored -> {
            int count = clients.size();
            String[] nicks = new String[count];
            for (int i = 0; i < count; i++) {
                nicks[i] = clients.get(i).data.getName();
            }
            client.invoke("setPlayersNicks", nicks);
        });
        client.on("giveUp", ignored -> {
            try {
                giveUp(client);
            } catch (WrongActionException e) {
                client.invoke("wrongAction", e.getTextToDisplay());
            }
        });
        client.on("getBoardSize", ignored -> {
            client.invoke("setBoardSize", boardSize);
        });
    }

    private int getClientIndex(IClient client){
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).client.equals(client)) return i;
        }
        return -1;
    }

    @Override
    public void unregisterClient(IClient client) throws WrongActionException {
        int index = getClientIndex(client);
        if (index < 0) throw new WrongActionException("This client does not belong to this game");
        if (inGame) {
            giveUp(client);
        } else {
            clients.remove(index);
            for (int i = index; i < clients.size(); i++) {
                clients.get(i).client.invoke("setIndex", i);
            }
            broadcast("changePlayerCount", clients.size());
        }
    }

    @Override
    public void init() throws WrongActionException {
        if (inGame) throw new WrongActionException("Game already started");
        if (playersCount != clients.size()) throw new WrongActionException("Count of players in room is not the same as registered");
        PuzzleGenerator generator = new PuzzleGenerator();
        for (int i = 0; i < playersCount; i++) {
            clients.get(i).data.setNewPuzzles(generator.getDefaultPuzzleSet());
        }
        defaultPuzzlePiecesCount = clients.get(0).data.puzzlesCount();
        board = new int[boardSize.x()][boardSize.y()];
        for (int x = 0; x < boardSize.x(); x++)
            for (int y = 0; y < boardSize.y(); y++){
                board[x][y] = -1;
            }
        inGame = true;
        turn = 0;
        broadcast("startGame", 0);
        broadcast("changeTurn", turn);
    }

    private List<IPuzzle> getPuzzleList(int index) throws WrongActionException {
        if (!inGame) throw new WrongActionException("Game does not exists");
        if (index < 0 || index >= playersCount) throw new WrongActionException("Incorrect index");
        return clients.get(index).data.getCopyOfPuzzlesList();
    }

    private int getBoardValueOrDefault(Vector2i position, int def){
        if (!position.inRange(boardSize)) return def;
        return board[position.x()][position.y()];
    }

    private void placePuzzle(IClient client, IPuzzle puzzle, Vector2i position) throws WrongActionException {
        if (!inGame) throw new WrongActionException("Game does not exists");
        int index = getClientIndex(client);
        if (index < 0) throw new WrongActionException("This client does not belong to game");
        if (index != turn) throw new WrongActionException("Not your turn");
        if (!puzzle.getMinBound().add(position).inRange(boardSize)) throw new WrongActionException("Puzzle is not in board range");
        if (!puzzle.getMaxBound().add(position).inRange(boardSize)) throw new WrongActionException("Puzzle is not in board range");
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
        if (cornerConnectionsCount == 0) {
            if (clients.get(index).data.puzzlesCount() == defaultPuzzlePiecesCount) {
                boolean corner = false;
                for (Vector2i pos : puzzle.getFields()) {
                    Vector2i pos2 = pos.add(position);
                    if ((pos2.x() == 0 || pos2.x() == boardSize.x() - 1) && (pos2.y() == 0 || pos2.y() == boardSize.y() - 1)) {
                        corner = true;
                        break;
                    }
                }
                if (!corner) {
                    throw new WrongActionException("Your first puzzle must be placed at board corner");
                }
            } else
                throw new WrongActionException("Your puzzle must have corner connection with another your puzzle");
        }
        if (sideConnectionsCount != 0) throw new WrongActionException("Your puzzle can not have side connection with another your puzzle");
        if (!clients.get(index).data.removePuzzle(puzzle)) throw new WrongActionException("This puzzle does not exists in your inventory");
        for (Vector2i pos : puzzle.getFields()) {
            Vector2i pos2 = pos.add(position);
            board[pos2.x()][pos2.y()] = index;
        }
        broadcast("placePuzzle", new PuzzlePlaceRecord(index, position, puzzle));
        do {
            turn++;
            turn %= playersCount;
        } while (turn != index && clients.get(turn).data.passed());
        broadcast("changeTurn", turn);
    }

    private void giveUp(IClient client) throws WrongActionException {
        if (!inGame) throw new WrongActionException("Game does not exists");
        int index = getClientIndex(client);
        if (index < 0) throw new WrongActionException("This client does not belong to game");
        clients.get(index).data.setPass();
        broadcast("playerGiveUp", index);
        int playersInGame = 0;
        for (PlayerData d : clients) {
            if (!d.data.passed()) playersInGame++;
        }
        if (playersInGame == 0) {
            endgame();
            return;
        }
        if (turn != index) return;
        do {
            turn++;
            turn %= playersCount;
        } while (clients.get(turn).data.passed());
        broadcast("changeTurn", turn);
    }

    private void endgame(){
        EndgameData endgameData = new EndgameData();
        for (int i = 0; i < playersCount; i++) {
            endgameData.add(clients.get(i).data.getName(), clients.get(i).data.pointsLeft());
        }
        endgameData.sort(Comparator.comparingInt(EndgameClientData::points));
        broadcast("endgame", endgameData);
        inGame = false;
        clients.removeIf(c -> !c.client.isActive());
    }

    private record PlayerData(IClient client, ClientData data) { }

    public record PuzzlePlaceRecord(int playerIndex, Vector2i position, IPuzzle puzzle) implements Serializable { }
    public record PlayerChangeNickRecord(int playerIndex, String newNick) implements Serializable { }
    public record PlayerPuzzleListRecord(int playerIndex, List<IPuzzle> puzzles) implements Serializable { }
}
