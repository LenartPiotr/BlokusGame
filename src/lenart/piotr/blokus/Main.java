package lenart.piotr.blokus;

import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;
import lenart.piotr.blokus.engine.puzzle.SimplePuzzle;

public class Main {
    public static void main(String[] args) {
        IPuzzle puzzleOne = new SimplePuzzle(new Vector2i[]{
                new Vector2i(0, 0),
                new Vector2i(0, 1),
                new Vector2i(1, 1)
        });
        IPuzzle copy = puzzleOne.copy();
        copy.rotate(true);
        copy.flip(true);
        copy.rotate(true);
        System.out.println("Test two");
        if (puzzleOne.isTheSame(copy)) System.out.println("Second true");
        System.out.println("Test one");
        if (copy.isTheSame(puzzleOne)) System.out.println("One true");
    }
}
