package lenart.piotr.blokus.engine.puzzle;

import lenart.piotr.blokus.basic.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class PuzzleGenerator {
    public List<IPuzzle> getDefaultPuzzleSet(){
        List<IPuzzle> list = new ArrayList<>();

        String[] puzzlesInString = new String[]{
                // Ones & Twos & Threes
                "0,0", "0,0 1,0",
                "0,0 0,1 -1,1", "0,0 0,1 0,2",

                // Fours
                "0,0 0,1 0,2 0,3", "0,0 0,1 1,0 1,1",
                "0,0 0,1 0,2 1,0", "0,0 0,1 1,1 1,2",
                "0,0 0,1 0,2 -1,1",

                // Fives
                "0,0 1,0 1,1 1,2 1,3", "0,0 1,0 1,1 2,1 3,1",
                "0,0 0,1 0,2 0,3 -1,2", "0,0 0,1 0,2 -1,1 -1,2",
                "0,0 0,1 0,2 0,3 0,4", "0,0 0,1 0,2 -1,2 -2,2",
                "0,0 0,1 -1,1 -2,1 -2,2", "0,0 0,1 0,2 1,2 -1,1",
                "0,0 0,1 0,2 1,2 -1,2", "0,0 -1,0 -1,1 -2,1, -2,2",
                "0,0 1,0 0,1 -1,0 0,-1", "0,0 0,1 1,1 2,1 2,0"
        };

        for (String s : puzzlesInString) {
            String[] parts = s.split(" ");
            Vector2i[] vList = new Vector2i[parts.length];
            for (int i = 0; i < parts.length; i++) {
                String[] coords = parts[i].split(",");
                vList[i] = new Vector2i(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
            }
            list.add(new SimplePuzzle(vList));
        }

        return list;
    }
}
