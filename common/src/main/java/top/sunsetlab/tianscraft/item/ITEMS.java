/**
 * 物品相关
 */

package top.sunsetlab.tianscraft.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.sunsetlab.tianscraft.ModMain;

public class ITEMS {
    // 注册器
    public static final DeferredRegister<Item> itemDeferredRegister;
    // 创造模式物品栏注册器
    public static final DeferredRegister<CreativeModeTab> creativeModeTabDeferredRegister;
    // 船发射器
    public static final RegistrySupplier<Item> itemBoatLauncher;
    // 兲（当物品栏icon用的）
    public static final RegistrySupplier<Item> itemTian;
    // 创造模式物品栏
    public static final RegistrySupplier<CreativeModeTab> tiansCraftTab;

    static {
        // 很常规的注册
        itemDeferredRegister = DeferredRegister.create(ModMain.MOD_ID, Registries.ITEM);
        itemBoatLauncher = itemDeferredRegister.register("boat_launcher", BoatLauncher::new);
        itemTian = itemDeferredRegister.register("tian",() -> new Item(new Item.Properties()
                .rarity(Rarity.EPIC)
                .stacksTo(4)));
        creativeModeTabDeferredRegister = DeferredRegister.create(ModMain.MOD_ID, Registries.CREATIVE_MODE_TAB);
        tiansCraftTab = creativeModeTabDeferredRegister.register("tianscraft", () -> CreativeTabRegistry.create(
                Component.translatable("itemGroup.tianscraft"),
                () -> new ItemStack(itemTian.get())));
    }

    public static void register() {
        itemDeferredRegister.register();
        creativeModeTabDeferredRegister.register();
    }
}
