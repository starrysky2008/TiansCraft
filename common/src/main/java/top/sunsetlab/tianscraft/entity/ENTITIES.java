package top.sunsetlab.tianscraft.entity;

import com.mojang.logging.LogUtils;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.slf4j.Logger;
import top.sunsetlab.tianscraft.ModMain;
import top.sunsetlab.tianscraft.entity.render.TianRenderer;

public class ENTITIES {
    public static DeferredRegister<EntityType<?>> deferredRegister;
    public static RegistrySupplier<EntityType<EntityTian>> entityTian;
    public static Logger logger = LogUtils.getLogger();

    static {
        deferredRegister = DeferredRegister.create(ModMain.MOD_ID, Registries.ENTITY_TYPE);
        entityTian = deferredRegister.register("tian",
                () -> EntityType.Builder.of(EntityTian::new, MobCategory.MISC)
                        .sized(0.6F, 1.95F)
                        .eyeHeight(1.74F)
                        .passengerAttachments(2.0125F)
                        .ridingOffset(-0.7F)
                        .build("tian"));
    }

    public static void register(){
        deferredRegister.register();
        TianRenderer.init();
        EntityAttributeRegistry.register(() -> entityTian.get(), EntityTian::createMobAttributes);
    }
}
