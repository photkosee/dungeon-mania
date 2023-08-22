package dungeonmania.entities.buildables;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;

public class Bow extends Buildable  {

    private int durability;

    public Bow(int durability) {
        super(null);
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
            0,
            0,
            2,
            1));
    }

    public static boolean isBuildable(Inventory inventory, Game game) {
        int wood = inventory.count(Wood.class);
        int arrows = inventory.count(Arrow.class);
        return wood >= 1 && arrows >= 3;
    }

    public static void build(Inventory inventory) {
        List<Wood> wood = inventory.getEntities(Wood.class);
        List<Arrow> arrows = inventory.getEntities(Arrow.class);
        inventory.remove(wood.get(0));
        inventory.remove(arrows.get(0));
        inventory.remove(arrows.get(1));
        inventory.remove(arrows.get(2));
    }
}
