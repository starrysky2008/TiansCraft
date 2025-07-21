/**
 * 实体类，可以在这里处理实体相关内容
 */

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
    // 通用注册器
    public static final DeferredRegister<EntityType<?>> deferredRegister;
    // 实体兲
    public static final RegistrySupplier<EntityType<EntityTian>> entityTian;
    // logger
    public static final Logger logger = LogUtils.getLogger();

    static {
        // 根据modid创建注册器
        deferredRegister = DeferredRegister.create(ModMain.MOD_ID, Registries.ENTITY_TYPE);
        // 注册实体兲
        entityTian = deferredRegister.register("tian",
                () -> EntityType.Builder.of(EntityTian::new, MobCategory.MISC)
                        .sized(0.6F, 1.95F)
                        .eyeHeight(1.74F)
                        .passengerAttachments(2.0125F)
                        .ridingOffset(-0.7F)
                        .build("tian"));
    }

    /**
     * 注册模组实体/渲染器/属性
     */
    public static void register(){
        // 注册实体，渲染器，属性
        logger.debug("Fucking Entity Registration started");
        deferredRegister.register();
        TianRenderer.init();
        EntityAttributeRegistry.register(entityTian, EntityTian::createAttributes);
        logger.debug("Fucking Entity Registration ended");
    }
}
