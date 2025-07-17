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
    public static DeferredRegister<Item> itemDeferredRegister;
    public static DeferredRegister<CreativeModeTab> creativeModeTabDeferredRegister;
    public static RegistrySupplier<Item> itemBoatLauncher;
    public static RegistrySupplier<Item> itemTian;
    public static RegistrySupplier<CreativeModeTab> tiansCraftTab;

    static {
        creativeModeTabDeferredRegister = DeferredRegister.create(ModMain.MOD_ID, Registries.CREATIVE_MODE_TAB);
        tiansCraftTab = creativeModeTabDeferredRegister.register("tianscraft", () -> CreativeTabRegistry.create(
                Component.translatable("itemGroup.tianscraft"),
                () -> new ItemStack(itemTian.get())));
        itemDeferredRegister = DeferredRegister.create(ModMain.MOD_ID, Registries.ITEM);
        itemBoatLauncher = itemDeferredRegister.register("boat_launcher", BoatLauncher::new);
        itemTian = itemDeferredRegister.register("tian",() -> new Item(new Item.Properties()
                .rarity(Rarity.EPIC)
                .stacksTo(4)));
    }

    public static void register() {
        itemDeferredRegister.register();
        creativeModeTabDeferredRegister.register();
    }
}
