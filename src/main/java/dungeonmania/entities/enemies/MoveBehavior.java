package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Entity;

public interface MoveBehavior {

    public void move(Game game, Entity entity);
}
