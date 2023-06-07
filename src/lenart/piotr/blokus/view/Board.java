package lenart.piotr.blokus.view;

import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {
    private Vector2i size;
    private int[][] table;

    public void initNewBoard(Vector2i newSize){
        size = new Vector2i(newSize);
        table = new int[size.x()][size.y()];
        for (int x = 0; x < size.x(); x++)
            for (int y = 0; y < size.y(); y++) {
                table[x][y] = -1;
            }
        repaint();
    }

    public void placePuzzle(IPuzzle puzzle, int color, Vector2i position) {
        for (Vector2i field: puzzle.getFields()) {
            Vector2i pos = field.add(position);
            if (pos.inRange(size)) {
                table[pos.x()][pos.y()] = color;
            }
        }
        repaint();
    }

    private Color getColor(int index) {
        return switch (index) {
            case 0 -> Color.white;
            case 1 -> Color.red;
            case 2 -> Color.yellow;
            case 3 -> Color.green;
            case 4 -> Color.blue;
            default -> Color.magenta;
        };
    }

    @Override
    public void paint(Graphics g) {
        int width = this.getWidth();
        int height = this.getHeight();
        g.clearRect(0, 0, width, height);
        double widthField = width / (double)size.x();
        double heightField = height / (double)size.y();
        for (int x = 0; x < size.x(); x++) {
            for (int y = 0; y < size.y(); y++) {
                g.setColor(getColor(table[x][y]));
                g.fillRect((int)Math.floor(x * widthField), (int)Math.floor(y * heightField), (int)Math.ceil(widthField), (int)Math.ceil(heightField));
            }
        }
        g.setColor(Color.black);
        for (int i = 0; i < size.x(); i++) {
            g.drawLine((int)(i * widthField), 0, (int)(i * widthField), height);
        }
        for (int i = 0; i < size.y(); i++) {
            g.drawLine(0, (int)(i * heightField), width, (int)(i * heightField));
        }
    }
}
