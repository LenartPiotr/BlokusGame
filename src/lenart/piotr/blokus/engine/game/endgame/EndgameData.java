package lenart.piotr.blokus.engine.game.endgame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EndgameData implements Serializable {
    private final List<EndgameClientData> data;

    public EndgameData(){
        data = new ArrayList<>();
    }

    public void add(String name, int points) {
        data.add(new EndgameClientData(name, points));
    }

    public int count() {
        return data.size();
    }

    public EndgameClientData get(int index) {
        return data.get(index);
    }

    public void sort(Comparator<? super EndgameClientData> c) {
        data.sort(c);
    }
}
