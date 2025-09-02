package com.wenhao.itempickuprange.mixin;

import com.wenhao.itempickuprange.config.ModConfig;
import com.wenhao.itempickuprange.util.PlayerEntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity implements PlayerEntityAccess {

    @Final
    private PlayerInventory inventory;

    private static final double DEFAULT_PICKUP_RANGE = 1.0;
    private double customPickupRange = DEFAULT_PICKUP_RANGE;

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        World world = this.getWorld();
        if (!world.isClient) {
            List<ItemEntity> items = world.getEntitiesByClass(
                    ItemEntity.class,
                    this.getBoundingBox().expand(customPickupRange, customPickupRange, customPickupRange),
                    Entity::isAlive
            );

            for (ItemEntity item : items) {
                if (!item.cannotPickup() && item.squaredDistanceTo(this) < customPickupRange * customPickupRange) {
                    item.onPlayerCollision((PlayerEntity) (Object) this);
                }
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putDouble("CustomPickupRange", this.customPickupRange);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("CustomPickupRange")) {
            this.customPickupRange = nbt.getDouble("CustomPickupRange");
        }
        this.customPickupRange = Math.min(
                Math.max(this.customPickupRange, ModConfig.get().minPickupRange),
                ModConfig.get().maxPickupRange
        );
    }

    @Override
    public void setCustomPickupRange(double range) {
        this.customPickupRange = Math.min(
                Math.max(range, ModConfig.get().minPickupRange),
                ModConfig.get().maxPickupRange
        );
    }

    public double getCustomPickupRange() {
        return this.customPickupRange;
    }
}
