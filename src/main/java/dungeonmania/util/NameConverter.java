package dungeonmania.util;

import java.util.Arrays;
import java.util.Iterator;

import dungeonmania.entities.Door;
import dungeonmania.entities.Entity;
import dungeonmania.entities.LightBulb;
import dungeonmania.entities.Portal;
import dungeonmania.entities.SwitchDoor;

public class NameConverter {
    public static String toSnakeCase(Entity entity) {
        String nameBasic = toSnakeCase(entity.getClass().getSimpleName());
        String extra = "";
        if (entity instanceof Portal) {
            extra = "_" + ((Portal) entity).getColor().toLowerCase();
        } else if (entity instanceof Door) {
            extra = ((Door) entity).isOpen() ? "_open" : "";
        } else if (entity instanceof LightBulb) {
            extra = ((LightBulb) entity).isActivated() ? "_on" : "_off";
        } else if (entity instanceof SwitchDoor) {
            extra = ((SwitchDoor) entity).isActivated() ? "_open" : "";
        }
        return nameBasic + extra;
    }

    public static String toSnakeCase(String name) {
        String[] words = name.split("(?=[A-Z])");
        if (words.length == 1)
            return words[0].toLowerCase();

        StringBuilder builder = new StringBuilder();
        Iterator<String> iter = Arrays.stream(words).iterator();
        builder.append(iter.next().toLowerCase());

        while (iter.hasNext())
            builder.append("_").append(iter.next().toLowerCase());

        return builder.toString();
    }

    public static String toSnakeCase(Class<?> clazz) {
        return toSnakeCase(clazz.getSimpleName());
    }
}
