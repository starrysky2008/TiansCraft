/**
 * 对劫掠的修改
 */

package top.sunsetlab.tianscraft.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.sunsetlab.tianscraft.entity.ENTITIES;
import top.sunsetlab.tianscraft.entity.EntityTian;

@Mixin(Raid.class)
public abstract class MixinRaid {
    @Shadow @Final private RandomSource random;

    @Shadow @Final private ServerLevel level;

    @Shadow public abstract void joinRaid(int i, Raider raider, @Nullable BlockPos blockPos, boolean bl);

    @Shadow private int groupsSpawned;

    /**
     * 在劫掠开始时调用
     * 用来把匿名兲加入劫掠
     * @param blockPos 劫掠位置
     * @param ci CallbackInfo
     */
    @Inject(method = "spawnGroup", at = @At("HEAD"))
    private void inject(BlockPos blockPos, CallbackInfo ci){
        int baseSpawn = switch (level.getDifficulty()) {
            case PEACEFUL -> 0;
            case EASY -> 3;
            case NORMAL -> 4;
            case HARD -> 5;
        };
        int bonusSpawn = switch (level.getDifficulty()){
            case PEACEFUL -> 0;
            case EASY -> 3;
            case NORMAL -> 5;
            case HARD -> 5;
        };
        for(int i=0;i<baseSpawn+random.nextInt(bonusSpawn);i++){
            EntityTian raider = ENTITIES.entityTian.get().create(level);
            if (raider != null) {
                this.joinRaid(groupsSpawned+1,raider,blockPos,false);
                raider.setHome(blockPos);
            }
        }
    }
}