package dungeonmania.entities.enemies;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class MoveCircularly implements MoveBehavior {

    private List<Position> movementTrajectory;
    private int nextPositionElement;
    private boolean forward;

    public MoveCircularly(Position position, int nextPositionElement, boolean forward) {
        this.movementTrajectory = position.getAdjacentPositions();
        this.nextPositionElement = nextPositionElement;
        this.forward = forward;
    }

    private void updateNextPosition() {
        if (forward) {
            nextPositionElement++;
            if (nextPositionElement == 8) {
                nextPositionElement = 0;
            }
        } else {
            nextPositionElement--;
            if (nextPositionElement == -1) {
                nextPositionElement = 7;
            }
        }
    }

    @Override
    public void move(Game game, Entity entity) {
        Position nextPos = movementTrajectory.get(nextPositionElement);
        List<Entity> entities = game.getEntities(nextPos);
        if (entities != null && entities.size() > 0 && entities.stream().anyMatch(e -> e instanceof Boulder)) {
            forward = !forward;
            updateNextPosition();
            updateNextPosition();
        }
        nextPos = movementTrajectory.get(nextPositionElement);
        entities = game.getEntities(nextPos);
        if (entities == null
                || entities.size() == 0
                || entities.stream().allMatch(e -> e.canMoveOnto(game.getMap(), entity))) {
            game.moveTo(entity, nextPos);
            updateNextPosition();
        }
    }
}
