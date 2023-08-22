package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class MoveAllied implements MoveBehavior {

    private boolean playerFound = false;

    @Override
    public void move(Game game, Entity entity) {
        Position nextPos;
        if (game.isPlayerCardinallyAdjacent(entity.getPosition())) playerFound = true;
        if (playerFound) {
            nextPos = game.getPlayerPreviousDistinctPosition();
        } else {
            nextPos = game.dijkstraPathFind(entity.getPosition(), game.getPlayerPosition(), entity);
        }
        game.moveTo(entity, nextPos);
    }
}
