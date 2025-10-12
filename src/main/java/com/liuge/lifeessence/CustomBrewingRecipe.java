package com.liuge.lifeessence;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public class CustomBrewingRecipe implements IBrewingRecipe {
    private final ItemStack input;
    private final ItemStack ingredient;
    private final ItemStack output;
    private final int brewingTime;

    public CustomBrewingRecipe(ItemStack input, ItemStack ingredient, ItemStack output, int brewingTime) {
        this.input = input;
        this.ingredient = ingredient;
        this.output = output;
        this.brewingTime = brewingTime;
    }

    @Override
    public boolean isInput(ItemStack input) {
        // 检查输入是否为粗制药水
        return input.getItem() == this.input.getItem() &&
                net.minecraft.world.item.alchemy.PotionUtils.getPotion(input) ==
                        net.minecraft.world.item.alchemy.PotionUtils.getPotion(this.input);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        // 检查材料是否为金苹果
        return ingredient.getItem() == this.ingredient.getItem();
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        // 如果输入和材料都匹配，返回输出
        if (isInput(input) && isIngredient(ingredient)) {
            return this.output.copy();
        }
        return ItemStack.EMPTY;
    }

    // 自定义方法获取酿造时间（以tick为单位）
    public int getBrewingTime() {
        return brewingTime;
    }
}