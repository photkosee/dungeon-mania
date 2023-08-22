package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Task3Test {

    @Test
    @DisplayName("Testing a mercenary can be bribed within a radius")
    public void bribeRadius() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_mercenaryTest_bribeRadius", "c_mercenaryTest_bribeRadius");

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up treasure at 3,1
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(7, 1), getMercPos(res));
        // out of radius
        assertThrows(InvalidActionException.class, () ->
            dmc.interact(mercId)
        );

        res = dmc.tick(Direction.RIGHT);
        // player at 4,1
        assertEquals(new Position(6, 1), getMercPos(res));
        // attempt bribe within radius
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // no fight since it became an ally
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
    }

    @Test
    @DisplayName("Testing destroying a zombie toast spawner")
    public void toastDestruction() {
        //  PLA  ZTS
        //  SWO
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_zombieTest_toastDestruction", "c_zombieTest_toastDestruction");
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // cardinally adjacent: true, has sword: false
        assertThrows(InvalidActionException.class, () ->
                dmc.interact(spawnerId)
        );
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // pick up sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // cardinally adjacent: false, has sword: true
        assertThrows(InvalidActionException.class, () ->
                dmc.interact(spawnerId)
        );
        assertEquals(1, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // move right
        res = dmc.tick(Direction.RIGHT);

        // cardinally adjacent: true, has sword: true, but invalid_id
        assertThrows(IllegalArgumentException.class, () ->
                dmc.interact("random_invalid_id")
        );
        // cardinally adjacent: true, has sword: true
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));
    }

    @Test
    @DisplayName("Testing ally applying buff")
    public void allyBuff() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_allyBuff", "c_allyBuff");

        assertEquals(1, TestUtils.getEntities(res, "assassin").size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
        // bribe radius over the map with 0 cost
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        double atkBuff = Integer.parseInt(TestUtils.getValueFromConfigFile("ally_attack", "c_allyBuff"));
        double defBuff = Integer.parseInt(TestUtils.getValueFromConfigFile("ally_defence", "c_allyBuff"));
        assertEquals(10, atkBuff);
        assertEquals(7, defBuff);

        double enemyAttack = Integer.parseInt(TestUtils.getValueFromConfigFile("assassin_attack", "c_allyBuff"));
        double expectedDamage = (enemyAttack - defBuff) / 10;

        // an assassin has 10/10, player has 5/5 if ally buff doesn't apply (+10/7) player would die
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        BattleResponse battle = res.getBattles().get(0);
        RoundResponse firstRound = battle.getRounds().get(0);
        assertEquals(expectedDamage, -firstRound.getDeltaCharacterHealth(), 0.001);
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(0, TestUtils.getEntities(res, "assassin").size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
    }

    @Test
    @DisplayName(
        "Test bombing a treasure doesn't count toward treasure goal"
    )
    public void placeBombRadius2() throws InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_bombTreasure", "c_bombTreasure");

        // Activate Switch
        res = dmc.tick(Direction.RIGHT);

        // Pick up Bomb
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "bomb").size());

        // Place Cardinally Adjacent
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());

        // Check Bomb exploded with radius 2
        //
        //                 Boulder/Switch        Wall            Wall
        //                Bomb                   Treasure
        //
        //                Treasure
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());
        assertEquals(0, TestUtils.getEntities(res, "boulder").size());
        assertEquals(0, TestUtils.getEntities(res, "switch").size());
        assertEquals(0, TestUtils.getEntities(res, "wall").size());
        assertEquals(0, TestUtils.getEntities(res, "treasure").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        // treasure goal is 1, not done after 1 treasure is destroyed
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }
}
