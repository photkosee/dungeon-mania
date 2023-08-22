package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable, BattleItem {

    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;
    private boolean allied = false;
    private boolean beingControlled = false;
    private int duration = 0;
    private double alliedAttack;
    private double alliedDefence;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
                    double alliedAttack, double alliedDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.alliedAttack = alliedAttack;
        this.alliedDefence = alliedDefence;
        setMoveBehavior(new MoveFollow());
    }

    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied) return;
        super.onOverlap(map, entity);
    }

    @Override
    public void getStuck(int duration, Game game) {
        if (isFollowingPlayer(game)) return;
        super.getStuck(duration, game);
    }

    private boolean isFollowingPlayer(Game game) {
        return game.isPlayerCardinallyAdjacent(getPosition()) && allied;
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        Position distance = Position.calculatePositionBetween(player.getPosition(), getPosition());
        int raduis = distance.magnitude();
        return (bribeRadius >= raduis
                && player.countEntityOfType(Treasure.class) >= bribeAmount)
                || player.countEntityOfType(Sceptre.class) >= 1;
    }

    /**
     * bribe the merc
     */
    protected void bribe(Game game, Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }
    }

    public void setAllied(boolean allied) {
        this.allied = allied;
        if (!allied) {
            setMoveBehavior(new MoveFollow());
        } else {
            setMoveBehavior(new MoveAllied());
        }
    }

    @Override
    public void interact(Player player, Game game) {
        setAllied(true);
        if (player.countEntityOfType(Sceptre.class) >= 1) {
            this.duration = player.getSceptreDuration() + 1;
            beingControlled = true;
        } else {
            bribe(game, player);
        }
    }

    @Override
    public boolean isInteractable(Player player) {
        return !allied && canBeBribed(player);
    }

    @Override
    public void move(Game game) {
        duration--;
        if (duration <= 0 && beingControlled) {
            setAllied(false);
            beingControlled = false;
        }
        super.move(game);
    }

    @Override
    public BattleStatistics applyBuff(Game game, BattleStatistics origin) {
        if (!allied) return origin;
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            alliedAttack,
            alliedDefence,
            1,
            1));
    }
}
