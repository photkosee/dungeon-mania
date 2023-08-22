package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Destroyable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Overlapable;
import dungeonmania.entities.Player;
import dungeonmania.entities.SwampTile;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Enemy extends Entity implements Battleable, Destroyable, Overlapable {

    private BattleStatistics battleStatistics;
    private MoveBehavior moveBehavior;
    private int stickDuration = 0;

    public Enemy(Position position, double health, double attack) {
        super(position.asLayer(Entity.CHARACTER_LAYER));
        battleStatistics = new BattleStatistics(
                health,
                attack,
                0,
                BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER);
    }

    public void setMoveBehavior(MoveBehavior moveBehavior) {
        this.moveBehavior = moveBehavior;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    @Override
    public double getBattleStatisticsHealth() {
        return battleStatistics.getHealth();
    }

    @Override
    public void setBattleStatisticsHealth(double health) {
        battleStatistics.setHealth(health);
    }

    @Override
    public boolean isBattleStatisticsEnabled() {
        return battleStatistics.isEnabled();
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            map.gameBattle(player, this);
        } else if (entity instanceof SwampTile && !isStuck()) {
            getStuck(((SwampTile) entity).getStickDuration(), map.getGame());
        }
    }

    @Override
    public void onDestroy(GameMap map) {
        map.gameUnsubscribe(getId());
    }

    public boolean isStuck() {
        return stickDuration > 0;
    }

    public void getStuck(int duration, Game game) {
        this.stickDuration = duration;
    }

    public void move(Game game) {
        if (!isStuck()) {
            moveBehavior.move(game, this);
        } else {
            stickDuration--;
        }
    }
}
