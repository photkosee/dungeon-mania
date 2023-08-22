package dungeonmania.entities.enemies;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class MoveRandomly implements MoveBehavior {

    private Position getNextRandomPosition(Game game, Entity entity) {
        Position nextPos;
        GameMap map = game.getMap();
        Random randGen = new Random();
        List<Position> pos = entity.getCardinallyAdjacentPositions();
        pos = pos
            .stream()
            .filter(p -> map.canMoveTo(entity, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            nextPos = entity.getPosition();
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
        }
        return nextPos;
    }

    @Override
    public void move(Game game, Entity entity) {
        Position nextPos = getNextRandomPosition(game, entity);
        game.moveTo(entity, nextPos);
    }
}
