package com.sagesmp.sagesmp.handlers;

import com.sagesmp.sagesmp.handlers.SageSword;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class SageSwordRecipe {
    public SageSwordRecipe(JavaPlugin plugin, NamespacedKey key) {
        ItemStack customSword = new SageSword();
        ShapelessRecipe recipe = new ShapelessRecipe(key, customSword);

        recipe.addIngredient(Material.DRAGON_EGG);

        plugin.getServer().addRecipe(recipe);
    }
}
