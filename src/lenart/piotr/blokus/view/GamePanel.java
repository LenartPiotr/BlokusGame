package lenart.piotr.blokus.view;

import lenart.piotr.blokus.basic.ICallback0;
import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.game.GameService;
import lenart.piotr.blokus.engine.client.IClient;
import lenart.piotr.blokus.engine.game.endgame.EndgameData;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel boardPanel;
    private JPanel puzzleMainPanel;

    private JLabel errorMessage;
    private JLabel turnLabel;
    private Board board;
    private List<JPanel> userPuzzlePanels;

    private JLabel[] playersNick;
    private int playerIndex;
    private int playersCount;
    private boolean[] surrender;
    private String[] playersNames;

    private final ICallback0 onExit;

    private int turn;

    private final IClient client;

    public GamePanel(IClient client, ICallback0 onExit) {
        this.client = client;
        this.onExit = onExit;

        setupClientCallbacks();

        initializeComponents();

        client.invoke("getIndex", 0);
        client.invoke("getMaxPlayersCount", 0);

        playersCount = 0;

        playersNick = new JLabel[playersCount];
        surrender = new boolean[playersCount];
        playersNames = new String[playersCount];
        turn = 0;

        add(mainPanel);

        board.setOnClickListener((position, puzzle) -> {
            errorMessage.setText("");
            if (turn == playerIndex)
                client.invoke("placePuzzle", new GameService.PuzzlePlaceRecord(playerIndex, position, puzzle));
        });
    }

    private void playerUpdateNick(int index, String newNick) {
        // UPDATE + CHECK INDEX
    }

    private void setupClientCallbacks() {
        client.on("setIndex", data -> {
            playerIndex = (int) data;
            // UPDATE
        });
        client.on("setMaxPlayersCount", data -> {
            playersCount = (int) data;
            playersNick = new JLabel[playersCount];
            surrender = new boolean[playersCount];
            playersNames = new String[playersCount];
            for (int i = 0; i < playersCount; i++) {
                surrender[i] = false;
                playersNames[i] = "???";
            }
            client.invoke("getPlayersNicks", 0);
            // UPDATE
        });
        client.on("setPlayersNicks", data -> {
            String[] nicks = (String[]) data;
            for (int i = 0; i < nicks.length; i++) {
                playerUpdateNick(i, nicks[i]);
            }
            setupUsersPuzzlePanels();
        });
        client.on("wrongAction", data -> {
            String text = (String) data;
            errorMessage.setText(text);
        });
        client.on("placePuzzle", data -> {
            GameService.PuzzlePlaceRecord record = (GameService.PuzzlePlaceRecord) data;
            board.placePuzzle(record.puzzle(), record.playerIndex(), record.position());
            client.invoke("getPuzzleList", record.playerIndex());
            // UPDATE
        });
        client.on("setPuzzleList", data -> {
            GameService.PlayerPuzzleListRecord record = (GameService.PlayerPuzzleListRecord) data;
            int index = record.playerIndex();
            if (index < playersCount) {
                setPuzzlesView(userPuzzlePanels.get(index), record.puzzles(), index == playerIndex, index);
            }
            // UPDATE
        });
        client.on("changeTurn", data -> {
            turn = (int) data;
            if (turn < playersCount)
                turnLabel.setText("Turn: " + playersNames[turn]);
            // UPDATE
        });
        client.on("playerGiveUp", data -> {
            int playerIndex = (int) data;
            // UPDATE
        });
        client.on("playerChangeName", data -> {
            GameService.PlayerChangeNickRecord record = (GameService.PlayerChangeNickRecord) data;
            if (turn == record.playerIndex()) {
                turnLabel.setText("Turn: " + record.newNick());
            }
            playerUpdateNick(record.playerIndex(), record.newNick());
        });
        client.on("playerGiveUp", data -> {
            int p = (int) data;
            surrender[p] = true;
            playersNick[p].setForeground(Color.gray);
        });
        client.on("endgame", data -> {
            EndgameData endgameData = (EndgameData) data;
            setEndGame(endgameData);
        });
    }

    private void createNewBoard(Vector2i size) {
        boardPanel.removeAll();
        boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.Y_AXIS));
        board = new Board(size);
        Dimension boardDimension = new Dimension(401, 401);
        board.setPreferredSize(boardDimension);
        board.setMaximumSize(boardDimension);
        board.setMinimumSize(boardDimension);
        boardPanel.add(board);
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private void setupUsersPuzzlePanels() {
        puzzleMainPanel.removeAll();
        puzzleMainPanel.setLayout(new BoxLayout(puzzleMainPanel, BoxLayout.X_AXIS));
        userPuzzlePanels = new ArrayList<>();
        for (int i = 0; i < playersCount; i++) {
            JPanel userPuzzlePanel = new JPanel();
            userPuzzlePanels.add(userPuzzlePanel);
            puzzleMainPanel.add(userPuzzlePanel);
            client.invoke("getPuzzleList", i);
        }
        puzzleMainPanel.revalidate();
        puzzleMainPanel.repaint();
    }

    private void initializeComponents() {
        mainPanel.removeAll();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel header = new JPanel();
        mainPanel.add(header);

        turnLabel = new JLabel();
        turnLabel.setText("Turn: ???");
        header.add(turnLabel);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
        mainPanel.add(contentPanel);

        boardPanel = new JPanel();
        contentPanel.add(boardPanel);

        createNewBoard(new Vector2i(1, 1));

        puzzleMainPanel = new JPanel();
        puzzleMainPanel.setLayout(new BoxLayout(puzzleMainPanel, BoxLayout.X_AXIS));
        contentPanel.add(puzzleMainPanel);

        JPanel footer = new JPanel();
        footer.setLayout(new BoxLayout(footer, BoxLayout.X_AXIS));
        mainPanel.add(footer);

        JLabel cancelLabel = new JLabel("X");
        cancelLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                errorMessage.setText("");
            }
        });
        cancelLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        footer.add(cancelLabel);

        JPanel footerSpace = new JPanel();
        Dimension footerSpaceSize = new Dimension(10,10);
        footerSpace.setPreferredSize(footerSpaceSize);
        footerSpace.setMinimumSize(footerSpaceSize);
        footerSpace.setMaximumSize(footerSpaceSize);
        footer.add(footerSpace);

        errorMessage = new JLabel();
        footer.add(errorMessage);

        JButton surrenderButton = new JButton("Surrender");
        surrenderButton.addActionListener(action -> {
            if (turn == playerIndex) {
                client.invoke("giveUp", playerIndex);
            }
        });
        footer.add(surrenderButton);
    }

    private void setEndGame(EndgameData data) {
        turn = -1;
        contentPanel.add(new EndgamePanel(data, this.onExit));
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void setPuzzlesView(JPanel panel, List<IPuzzle> list, boolean onclick, int index) {
        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel playerNick = new JLabel(playersNames[index]);
        if (surrender[index]) playerNick.setForeground(Color.gray);
        playersNick[index] = playerNick;
        panel.add(playerNick);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension size = new Dimension(30, 30);
        Vector2i minSize = new Vector2i(5,5);
        for (IPuzzle puzzle : list) {
            Puzzle p = new Puzzle(puzzle, minSize, true, index);
            p.setPreferredSize(size);
            p.setMaximumSize(size);
            p.setMinimumSize(size);
            p.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (onclick) {
                p.setOnClick(this::pickedPuzzle);
            }
            panel.add(p);
        }
        panel.revalidate();
        panel.repaint();
    }

    private void pickedPuzzle(IPuzzle puzzle) {
        board.setPickedPuzzle(puzzle, 0);
    }
}
