package starshine.soulenchants.Enchantments;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import starshine.soulenchants.SoulEnchants;

public class Chiseling extends Enchantment {
    public Chiseling(String namespace) {
        super(new NamespacedKey(SoulEnchants.getPlugin(),namespace));
    }

    @Override
    public String getName() {
        return "凿石";
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }


    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return itemStack.getType() == Material.NETHERITE_PICKAXE;
    }
}
