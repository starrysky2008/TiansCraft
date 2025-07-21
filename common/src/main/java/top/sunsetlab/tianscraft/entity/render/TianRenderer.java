/**
 * 匿名兲实体渲染器
 */
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
    /**
     * 初始化部分
     * 用于注册渲染器
     */
    public static void init() {
        // 注册渲染器，不然会崩溃
        EntityRendererRegistry.register(ENTITIES.entityTian, TianRenderer::new);
    }

    /**
     * 构造器函数
     * @param context 渲染器上下文
     */
    public TianRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER_SLIM), false), 0.5F);
    }

    /**
     * 匿名兲材质
     * @param entity 实体实例
     * @return 一个带命名空间的材质路径
     */
    @Override
    public @NotNull ResourceLocation getTextureLocation(EntityTian entity) {
        // 匿名材质（群里偷的）
        return ResourceLocation.fromNamespaceAndPath("tianscraft","textures/entity/tian.png");
    }
}
