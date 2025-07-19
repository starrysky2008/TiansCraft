package top.sunsetlab.tianscraft;

import top.sunsetlab.tianscraft.entity.ENTITIES;
import top.sunsetlab.tianscraft.item.ITEMS;

public final class ModMain {
    public static final String MOD_ID = "tianscraft";

    public static void init() {
        ITEMS.register();
        ENTITIES.register();
    }
}
