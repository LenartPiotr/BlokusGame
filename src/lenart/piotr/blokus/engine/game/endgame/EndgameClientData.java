package lenart.piotr.blokus.engine.game.endgame;

public record EndgameClientData(String name, int points) {

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
}
