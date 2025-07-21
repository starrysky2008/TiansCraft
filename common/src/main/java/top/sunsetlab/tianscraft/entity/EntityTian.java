/**
 * 匿名兲实体
 */

package top.sunsetlab.tianscraft.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// 非常好的妖魔兲
public class EntityTian extends AbstractIllager {
    /**
     * 构造函数
     * @param entityType 实体类型（不知道干什么的）
     * @param level 所在世界
     */
    protected EntityTian(EntityType<? extends AbstractIllager> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * 创建属性，在这里可以添加额外的属性
     * @return 一个AttributeBuilder
     */
    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = LivingEntity.createLivingAttributes();
        builder.add(Attributes.ATTACK_DAMAGE,8.0); // 攻击伤害
        builder.add(Attributes.FOLLOW_RANGE,16.0); // 跟随距离（大概没什么用了，之前遗留的产物）
        builder.add(Attributes.ARMOR,10.0d); // 护甲值
        return builder;
    }

    // 匿名兲的ai

    /**
     * 注册实体行为
     */
    @Override
    protected void registerGoals() {
        int i=0;
        this.goalSelector.addGoal(i++,new FloatGoal(this)); // 在水上漂浮
        // this.goalSelector.addGoal(i++,new PanicGoal(this,0.5)); // 受击恐慌
        this.goalSelector.addGoal(i++,new LookAtPlayerGoal(this, Player.class,6.0f)); // 注视玩家
        this.goalSelector.addGoal(i++,new Raider.HoldGroundAttackGoal(this,10.0f));// 袭击
        this.goalSelector.addGoal(i++,new MeleeAttackGoal(this,0.5,true));// 追杀
        this.goalSelector.addGoal(i++,new RandomStrollGoal(this,0.6f));// 随机行走
        this.goalSelector.addGoal(i,new RandomLookAroundGoal(this));// 四处看
    }

    // 匿名的家，袭击时用到
    // 在没有事情做的时候会回到这里
    private BlockPos home;
    // 火球技能冷却（ticks）
    private int cooldown_skill = 10 * 20;
    // 破船技能冷却（ticks）
    private int cooldown_breakboat = 3 * 20;
    // 自然回复生命力冷却（ticks）
    private int cooldown_heal = 2 * 20;

    /**
     * 设置家
     * @param pos 家的位置
     */
    public void setHome(BlockPos pos){
        this.home = pos;
    }

    /**
     * 上层注册来的不知用途的函数
     * @param serverLevel 世界
     * @param i ？
     * @param bl ？
     */
    @Override
    public void applyRaidBuffs(ServerLevel serverLevel, int i, boolean bl) {
    }

    /**
     * 袭击成功庆祝的音效
     * @return 音效
     */
    @Override
    public @NotNull SoundEvent getCelebrateSound() {
        return SoundEvents.PILLAGER_CELEBRATE;
    }

    /**
     * 每tick更新时调用
     */
    @Override
    public void tick() {
        super.tick();
        // 在20格内搜索玩家/铁傀儡/村民
        if(this.getTarget()!=null && this.getTarget().isAlive()) {
            List<Entity> entities = this.level().getEntities(
                    (Entity) null,
                    new AABB(this.blockPosition().getBottomCenter().add(-20, -5, -20),
                            this.blockPosition().getBottomCenter().add(20, 5, 20)),
                    (entity) -> (entity instanceof IronGolem
                            || (entity instanceof Player && !((Player) entity).isCreative())
                            || entity instanceof Villager)
                            && entity instanceof LivingEntity);
            // 随机选取
            this.setTarget((LivingEntity) entities.get(random.nextInt(entities.size())));
        }else if (!this.getTarget().isAlive()) {
            this.setTarget(null);
        }
        // 自动回家
        if(this.navigation.isDone() && home != null){
            this.navigation.moveTo(this.navigation.createPath(home,5),0.5);
        }
        // 各种技能冷却更新
        if(this.cooldown_skill > 0) cooldown_skill--;
        if(this.cooldown_breakboat > 0) cooldown_breakboat--;
        if(this.cooldown_heal > 0) cooldown_heal--;

        // 豪火球之术！
        if(this.getTarget() != null && this.getTarget().isAlive() && cooldown_skill == 0) {
            this.getLookControl().setLookAt(this.getTarget());
            // 一个从自身到目标的单位向量
            Vec3 motion = this.getTarget().blockPosition().getCenter().add(
                    this.blockPosition().getCenter().add(0,1,0).reverse()
            ).normalize();
            // 火球加速度
            double ACC = 1.5d;
            Fireball fireball = EntityType.SMALL_FIREBALL.create(level());
            if (fireball != null) {
                fireball.setPos(this.blockPosition().getCenter().add(0,1,0).add(motion));
                fireball.lerpMotion(
                        motion.scale(ACC).x,
                        motion.scale(ACC).y,
                        motion.scale(ACC).z
                );
                this.level().addFreshEntity(fireball);
            }
            this.cooldown_skill = 20 * 10;
        }
        // 破船
        if(this.cooldown_breakboat == 0){
            boolean flag = false;
            for (Entity ent:this.level().getEntities((Entity) null,this.getAttackBoundingBox(),
                    entity -> entity instanceof Boat)) {
                ent.hurt(damageSources().mobAttack(this),8.0f);
                flag = true;
            }
            if (flag) cooldown_breakboat = 2 * 20;
        }
        // 回血
        if(this.cooldown_heal == 0 && this.getHealth() < this.getMaxHealth()){
            this.heal(1.0f);
            this.cooldown_heal = 15;
        }
    }

    /**
     * 是否应该掉落战利品
     * @return 结果
     */
    @Override
    protected boolean shouldDropLoot() {
        return true;
    }

    /**
     * 基础经验值掉落
     * @return 掉落经验数量
     */
    @Override
    protected int getBaseExperienceReward() {
        return 5;
    }
}
