package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SceptreTest {

    @Test
    @DisplayName("Test using sceptre (ignore radius)")
    public void sceptreUse() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreDuration", "c_sceptreDuration");
        // bribe radius = 0
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // mercenary is now right next to the player
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        // ^ duration is 2 but start by this tick so only 1 tick left
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
        // ^ last tick of being an ally
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());
        // ^ end up fighting because it became an enemy again
    }

    @Test
    @DisplayName("Test using sceptre 1 duration")
    public void sceptre1() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreDuration", "c_sceptre1");
        // bribe radius = 0
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        // mercenary is now right next to the player
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        // ^ duration is 1 but start by this tick so only 0 tick left
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());
        // ^ end up fighting because it became an enemy again
    }

    @Test
    @DisplayName("Test building a sceptre")
    public void buildBow() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_AdvancedBuildablesTest", "c_BuildablesTest_BuildBow");

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
    }
}
