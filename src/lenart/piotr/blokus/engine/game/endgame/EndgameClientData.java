package lenart.piotr.blokus.engine.game.endgame;

public class EndgameClientData {
    private final String name;
    private final int points;
    public EndgameClientData(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
}
