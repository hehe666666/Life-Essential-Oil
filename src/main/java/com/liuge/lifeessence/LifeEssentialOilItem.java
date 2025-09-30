package com.liuge.lifeessence;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.network.chat.Component;

public class LifeEssentialOilItem extends Item {
    public LifeEssentialOilItem(Properties properties) {
        super(properties);
    }

    // 添加这个方法：右键开始使用物品
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        // 开始使用物品（右键饮用）
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayer player) {
            try {
                // 获取玩家数据
                CompoundTag rootTag = player.getPersistentData();
                CompoundTag persistedTag = rootTag.getCompound("PlayerPersisted");
                double currentMaxHealth;

                // 从死亡惩罚系统读取当前最大生命值
                if (persistedTag.contains("deathpenalty_max_health")) {
                    currentMaxHealth = persistedTag.getDouble("deathpenalty_max_health");
                } else {
                    currentMaxHealth = player.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
                    // 初始化数据
                    persistedTag.putDouble("deathpenalty_max_health", currentMaxHealth);
                }

                // 增加1点最大生命值（2点属性值 = 1颗心）
                double newMaxHealth = currentMaxHealth + 2.0D;

                // 保存到死亡惩罚系统
                persistedTag.putDouble("deathpenalty_max_health", newMaxHealth);
                rootTag.put("PlayerPersisted", persistedTag);

                // 更新当前最大生命值
                player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(newMaxHealth);

                // 恢复当前生命值
                if (player.getHealth() < newMaxHealth) {
                    player.setHealth((float) newMaxHealth);
                }

                // 发送消息给玩家
                player.sendSystemMessage(Component.literal("§a使用生命精油，最大生命值增加至 " + newMaxHealth));

                // 消耗物品
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            } catch (Exception e) {
                player.sendSystemMessage(Component.literal("§c使用生命精油时出现错误"));
                e.printStackTrace();
            }
        }

        return stack;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32; // 使用时间（tick）
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK; // 饮用动画
    }
}