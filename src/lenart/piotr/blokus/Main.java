package lenart.piotr.blokus;

import lenart.piotr.blokus.basic.Vector2i;
import lenart.piotr.blokus.engine.puzzle.IPuzzle;
import lenart.piotr.blokus.engine.puzzle.PuzzleGenerator;
import lenart.piotr.blokus.engine.puzzle.SimplePuzzle;

public class Main {
    public static void main(String[] args) {
        PuzzleGenerator generator = new PuzzleGenerator();
        System.out.println(generator.getDefaultPuzzleSet().size());
    }
}
