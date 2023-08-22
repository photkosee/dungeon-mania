package dungeonmania.battles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.util.NameConverter;

public class BattleFacade {
    private List<BattleResponse> battleResponses = new ArrayList<>();

    public void battle(Game game, Player player, Enemy enemy) {
        // 0. init
        double initialPlayerHealth = player.getBattleStatisticsHealth();
        double initialEnemyHealth = enemy.getBattleStatisticsHealth();
        String enemyString = NameConverter.toSnakeCase(enemy);
        List<Mercenary> allies = game.getEntities(Mercenary.class);

        // 1. apply buff provided by the game and player's inventory
        // getting buffing amount
        List<BattleItem> battleItems = new ArrayList<>();
        BattleStatistics playerBuff = new BattleStatistics(0, 0, 0, 1, 1);
        for (Mercenary ally : allies) {
            playerBuff = ally.applyBuff(game, playerBuff);
        }
        BattleStatistics playerBattleStatistics;

        if (player.isInEffective()) {
            playerBuff = player.applyBuff(game, playerBuff);
            playerBattleStatistics = BattleStatistics.applyBuff(player.getBattleStatistics(), playerBuff);
            if (!playerBattleStatistics.isEnabled() || !enemy.isBattleStatisticsEnabled()) return;
        } else {
            for (BattleItem item : player.getInventory().getEntities(BattleItem.class)) {
                playerBuff = item.applyBuff(game, playerBuff);
                battleItems.add(item);
            }
            playerBattleStatistics = BattleStatistics.applyBuff(player.getBattleStatistics(), playerBuff);
        }

        List<BattleRound> rounds = BattleStatistics.battle(playerBattleStatistics, enemy.getBattleStatistics());

        // 3. update health to the actual statistics
        player.setBattleStatisticsHealth(playerBattleStatistics.getHealth());
        enemy.setBattleStatisticsHealth(enemy.getBattleStatisticsHealth());

        // 5. Log the battle - solidate it to be a battle response
        battleResponses.add(new BattleResponse(
                enemyString,
                rounds.stream()
                    .map(ResponseBuilder::getRoundResponse)
                    .collect(Collectors.toList()),
                battleItems.stream()
                        .map(Entity.class::cast)
                        .map(ResponseBuilder::getItemResponse)
                        .collect(Collectors.toList()),
                initialPlayerHealth,
                initialEnemyHealth));
    }

    public List<BattleResponse> getBattleResponses() {
        return battleResponses;
    }
}
