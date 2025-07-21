/**
 * 船发射器
 */

package top.sunsetlab.tianscraft.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class BoatLauncher extends BowItem {
    /**
     * 构造函数
     */
    public BoatLauncher() {
        // 物品属性/分类
        super(new Properties().stacksTo(1)
                .arch$tab(CreativeModeTabs.COMBAT)
                .arch$tab(ITEMS.tiansCraftTab)
                .rarity(Rarity.UNCOMMON));
    }

    /**
     * 远程武器接受的弹药类型
     * @return 接受的类型
     */
    @Override
    public @NotNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return itemStack -> itemStack.getItem() instanceof BoatItem;
    }

    /**
     * 释放时动作
     * @param itemStack 发射器物品
     * @param level 世界
     * @param livingEntity 使用者
     * @param i 使用时间？
     */
    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
        if (livingEntity instanceof Player player) {
            ItemStack itemStack2 = player.getProjectile(itemStack);
            if (!itemStack2.isEmpty()) {
                int j = this.getUseDuration(itemStack, livingEntity) - i;
                float f = getPowerForTime(j);
                if (!((double) f < 0.1)) {
                    List<ItemStack> list = draw(itemStack, itemStack2, player);
                    if (level instanceof ServerLevel serverLevel) {
                        if (!list.isEmpty()) {
                            this.shoot_boat(serverLevel, livingEntity, f);
                            player.getCooldowns().addCooldown(this, 2 * 20);
                        }
                    }
                }
            }
        }
    }

    /**
     * 发射船只的函数
     * @param level 世界
     * @param livingEntity 使用者
     * @param power 力量（0-1）
     */
    protected void shoot_boat(ServerLevel level, LivingEntity livingEntity, float power) {
        // 仰角/航向角
        double yaw = Math.toRadians(livingEntity.getYHeadRot());
        double pitch = Math.toRadians(livingEntity.getXRot());

        // 水平向量长度
        double xzLen = (float) Math.cos(pitch);

        double x = -xzLen * (float) Math.sin(yaw);
        double y = -(float) Math.sin(pitch);
        double z = xzLen * (float) Math.cos(yaw);

        // 初速度
        double acc = power * 2.5;

        Boat ent = new Boat(level, livingEntity.getX(), livingEntity.getY()+1, livingEntity.getZ());
        ent.lerpMotion(x*acc, y*acc, z*acc);
        level.addFreshEntity(ent);
    }
}