package dungeonmania.entities.collectables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.util.Position;

public class Sword extends CollectableEntity implements BattleItem {

    public static final double DEFAULT_ATTACK = 1;
    public static final double DEFAULT_ATTACK_SCALE_FACTOR = 1;
    public static final int DEFAULT_DURABILITY = 5;
    public static final double DEFAULT_DEFENCE = 0;
    public static final double DEFAULT_DEFENCE_SCALE_FACTOR = 1;

    private int durability;
    private double attack;

    public Sword(Position position, double attack, int durability) {
        super(position);
        this.attack = attack;
        this.durability = durability;
    }

    @Override
    public BattleStatistics applyBuff(Game game, BattleStatistics origin) {
        durability--;
        if (durability <= 0) {
            game.playerRemoveItem(this);
        }
        return BattleStatistics.applyBuff(origin, new BattleStatistics(
            0,
            attack,
            0,
            1,
            1));
    }
}
