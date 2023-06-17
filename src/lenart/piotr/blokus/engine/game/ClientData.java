package lenart.piotr.blokus.engine.game;

import lenart.piotr.blokus.engine.puzzle.IPuzzle;

import java.util.ArrayList;
import java.util.List;

public class ClientData {
    private List<IPuzzle> puzzles;
    private boolean pass;
    private String name;

    public ClientData(){
        this.puzzles = new ArrayList<>();
        pass = false;
    }

    public void setNewPuzzles(List<IPuzzle> puzzles) {
        this.puzzles = puzzles;
    }

    public int pointsLeft() {
        int sum = 0;
        for (IPuzzle p: puzzles) {
            sum += p.size();
        }
        return sum;
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

    public int puzzlesCount() {
        return puzzles.size();
    }

    public boolean passed(){
        return pass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
