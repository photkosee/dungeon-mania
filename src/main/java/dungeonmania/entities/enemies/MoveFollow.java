package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class MoveFollow implements MoveBehavior {

    @Override
    public void move(Game game, Entity entity) {
        Position nextPos = game.dijkstraPathFind(entity.getPosition(), game.getPlayerPosition(), entity);
        game.moveTo(entity, nextPos);
    }
}
