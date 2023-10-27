package com.sagesmp.sagesmp.handlers;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.UUID;

public class SageSword extends ItemStack {
    public SageSword() {
        super(Material.NETHERITE_SWORD);

        ItemMeta meta = getItemMeta();
        meta.setDisplayName("§5SageSword");
        meta.setLore(Collections.singletonList("The sword forged from the remains of the dragon."));

        UUID uuid = UUID.randomUUID();
        AttributeModifier damageModifier = new AttributeModifier(uuid, "generic.attack_damage", 13.5, AttributeModifier.Operation.ADD_NUMBER);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, damageModifier);
        meta.setCustomModelData(1);

        setItemMeta(meta);
    }
}
