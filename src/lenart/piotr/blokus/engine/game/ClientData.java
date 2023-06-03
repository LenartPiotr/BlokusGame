package lenart.piotr.blokus.engine.game;

import lenart.piotr.blokus.engine.puzzle.IPuzzle;

import java.util.ArrayList;
import java.util.List;

public class ClientData {
    private final List<IPuzzle> puzzles;
    private boolean pass;

    public ClientData(List<IPuzzle> puzzles){
        this.puzzles = puzzles;
        pass = false;
    }

    public void setPass(){
        pass = true;
    }
    public boolean removePuzzle(IPuzzle puzzle){
        return puzzles.removeIf(p -> p.isTheSame(puzzle));
    }
    public List<IPuzzle> getCopyOfPuzzlesList(){
        List<IPuzzle> copy = new ArrayList<>();
        puzzles.forEach(p -> copy.add(p.copy()));
        return copy;
    }

    public boolean passed(){
        return pass;
    }
}
