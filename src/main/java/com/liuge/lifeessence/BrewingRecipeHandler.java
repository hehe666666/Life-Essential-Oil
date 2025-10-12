package com.liuge.lifeessence;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = LifeEssenceMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrewingRecipeHandler {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // 创建粗制药水
            ItemStack awkwardPotion = new ItemStack(Items.POTION);
            PotionUtils.setPotion(awkwardPotion, Potions.AWKWARD);

            // 创建金苹果
            ItemStack goldenApple = new ItemStack(Items.GOLDEN_APPLE);

            // 创建生命精油
            ItemStack lifeEssentialOil = new ItemStack(LifeEssenceMod.LIFE_ESSENTIAL_OIL.get());

            // 注册酿造配方：金苹果 + 粗制药水 → 生命精油
            // 注意：酿造台会自动处理3瓶药水的转换
            BrewingRecipeRegistry.addRecipe(
                    new CustomBrewingRecipe(awkwardPotion, goldenApple, lifeEssentialOil, 300) // 300 tick = 15秒
            );
        });
    }
}