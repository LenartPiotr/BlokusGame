package lenart.piotr.blokus.engine.game.endgame;

import java.io.Serializable;

public record EndgameClientData(String name, int points) implements Serializable { }
