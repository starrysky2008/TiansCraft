package top.sunsetlab.tianscraft.entity.render;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import top.sunsetlab.tianscraft.entity.ENTITIES;
import top.sunsetlab.tianscraft.entity.EntityTian;

public class TianRenderer extends MobRenderer<EntityTian, PlayerModel<EntityTian>> {
    public static void init() {
        // 注册渲染器，不然会崩溃
        EntityRendererRegistry.register(ENTITIES.entityTian, TianRenderer::new);
    }

    public TianRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), false), 0.5F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityTian entity) {
        // 匿名材质（群里偷的）
        return ResourceLocation.fromNamespaceAndPath("tianscraft","textures/entity/tian.png");
    }
}
