package com.dtteam.dtcobblemon.mixin;

import com.cobblemon.mod.common.api.ai.BehaviourConfigurationContext;
import com.cobblemon.mod.common.api.ai.config.task.PlaceHoneyInSaccLeavesTaskConfig;
import com.dtteam.dtcobblemon.behavior.PlaceHoneyReplacedBehavior;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlaceHoneyInSaccLeavesTaskConfig.class)
public class PlaceHoneyInDynSacLeavesTaskConfig {

    @Inject(method = "createTask", at = @At("RETURN"), cancellable = true, remap = false)
    @SuppressWarnings("unchecked")
    private void createTask(LivingEntity entity, BehaviourConfigurationContext behaviourConfigurationContext, CallbackInfoReturnable<BehaviorControl<? super LivingEntity>> cir){
        BehaviorControl<? extends LivingEntity> behaviorControl = cir.getReturnValue();
        if (behaviorControl instanceof Behavior<?> behavior){
            cir.setReturnValue(new PlaceHoneyReplacedBehavior((Behavior<LivingEntity>)behavior));
        }
    }

}

