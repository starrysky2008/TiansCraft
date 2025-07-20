package top.sunsetlab.tianscraft.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

// 非常好的妖魔兲
public class EntityTian extends AgeableMob {
    protected EntityTian(EntityType<? extends AgeableMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = LivingEntity.createLivingAttributes();
        builder.add(Attributes.ATTACK_DAMAGE,4.0);
        builder.add(Attributes.FOLLOW_RANGE,16.0);
        return builder;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return this;
    }

    // 匿名兲的ai
    @Override
    protected void registerGoals() {
        int i=0;
        this.goalSelector.addGoal(i++,new FloatGoal(this));
        this.goalSelector.addGoal(i++,new PanicGoal(this,0.5));
        this.goalSelector.addGoal(i++,new MoveBackToVillageGoal(this,0.5,true));
        this.goalSelector.addGoal(i++,new MeleeAttackGoal(this,0.5,true));
        this.goalSelector.addGoal(i++,new LookAtPlayerGoal(this, Player.class,6.0f));
        this.goalSelector.addGoal(i,new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        for(Entity ent:this.level().getEntities(
                (Entity) null,
                new AABB(this.blockPosition().getBottomCenter().add(-20, -5, -20),
                        this.blockPosition().getBottomCenter().add(20, 5, 20)),
                entity -> entity instanceof IronGolem
        )){
            if(ent instanceof LivingEntity livingEntity){
                this.setTarget(livingEntity);
            }
        }
    }
}
