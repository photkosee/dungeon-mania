package dungeonmania.entities.enemies;

import dungeonmania.util.Position;

public class ZombieToast extends Enemy {

    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
        setMoveBehavior(new MoveRandomly());
    }
}
