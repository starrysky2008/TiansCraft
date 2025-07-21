/**
 * 模组入口
 */
package top.sunsetlab.tianscraft;

import top.sunsetlab.tianscraft.entity.ENTITIES;
import top.sunsetlab.tianscraft.item.ITEMS;

public final class ModMain {
    public static final String MOD_ID = "tianscraft";

    /**
     * 初始化函数
     */
    public static void init() {
        ITEMS.register();
        ENTITIES.register();
    }
}
