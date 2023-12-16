package starshine.soulenchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Method {

    public static List<Enchantment> enchantmentList (){

        List<Enchantment> list = new ArrayList<>();
        list.add(SoulEnchants.soulBlade);
        list.add(SoulEnchants.chiseling);
        return list;


    }

    public static void openMainMenu(Player player) {

        Inventory inventory = Bukkit.createInventory(player, 54, "SoulEnchants");

        ItemStack enchantMenu = new ItemStack(Material.ENCHANTED_BOOK);


        Method.setName(enchantMenu, ChatColor.AQUA + "打开附魔菜单" );
        Method.addLore(enchantMenu, ChatColor.WHITE + "在这里获取附魔");




        inventory.setItem(10, enchantMenu);
        inventory.setItem(12, enchantMenu);
        inventory.setItem(14, enchantMenu);
        inventory.setItem(16, enchantMenu);
        inventory.setItem(19, enchantMenu);
        inventory.setItem(21, enchantMenu);
        inventory.setItem(23, enchantMenu);
        inventory.setItem(25, enchantMenu);
        inventory.setItem(28, enchantMenu);
        inventory.setItem(34, enchantMenu);
        inventory.setItem(47, enchantMenu);
        inventory.setItem(49, enchantMenu);
        inventory.setItem(51, enchantMenu);


        player.openInventory(inventory);

    }

    public static void openEnchantMenu(Player player) {

        Inventory inventory = Bukkit.createInventory(player, 54, "SoulEnchants Enchants");



        List<Enchantment> list = Method.enchantmentList();

        for(int i=0;  i<list.size(); i++){

            ItemStack enchantBook = new ItemStack(Material.ENCHANTED_BOOK);

            Enchantment enchantment = list.get(i);

            enchantBook.addEnchantment(enchantment,enchantment.getMaxLevel());

            String name = enchantment.getName();

            Method.setName(enchantBook,name);

            inventory.setItem(i,enchantBook);

        }



        player.openInventory(inventory);

    }

    public static void levelUp(ItemStack itemStack, Enchantment enchantment){

        if(itemStack.getEnchantments().containsKey(enchantment)){

            int level = Method.getLevel(itemStack,enchantment);

            if(level+1>enchantment.getMaxLevel()){
                return;
            }

            itemStack.addEnchantment(enchantment,level+1);

        }else {

            itemStack.addEnchantment(enchantment,1);
        }

    }

    public static void levelDown(ItemStack itemStack, Enchantment enchantment){

        if(itemStack.getEnchantments().containsKey(enchantment)){

            int level = Method.getLevel(itemStack,enchantment);

            if(level-1>enchantment.getStartLevel()){
                return;
            }

            itemStack.addEnchantment(enchantment,level-1);

        }else {
            return;
        }

    }



    public static void checkEnchantLore(ItemStack itemStack){



        for(Enchantment enchantment : Method.enchantmentList()){

            if(!itemStack.getEnchantments().containsKey(enchantment)){
                return;
            }


            String name = enchantment.getName();
            int level = Method.getLevel(itemStack,enchantment);
            String romanLevel = Method.NUMERALS[level];
            String fullName = name + " " + romanLevel;


            ItemMeta meta = itemStack.getItemMeta();
            if(meta==null){
                return;
            }

            List<String> lore = meta.getLore();
            if(lore==null){
                lore=new ArrayList<>();
            }

            boolean alreadyHaveLore = false;

            for(String line: lore){
                if (line.contains(fullName)){
                    alreadyHaveLore = true;
                    break;
                }
            }

            if(alreadyHaveLore){
                return;
            }

            lore.add(ChatColor.WHITE + fullName);

            meta.setLore(lore);

            itemStack.setItemMeta(meta);

        }





    }

    public static void addLore(ItemStack itemStack,String string){
        ItemMeta meta = itemStack.getItemMeta();
        if(meta!=null){
            List<String> lore = meta.getLore();
            if(lore==null){
                lore=new ArrayList<>();
            }
            lore.add(string);
            meta.setLore(lore);
        }
        itemStack.setItemMeta(meta);
    }
    public static void setName(ItemStack itemStack,String string){
        ItemMeta meta = itemStack.getItemMeta();
        if(meta!=null){
            meta.setDisplayName(string);
        }
        itemStack.setItemMeta(meta);
    }

    public static int getLevel(ItemStack item, Enchantment enchant) {
        if (item.getItemMeta() != null) {
            item.getItemMeta().getEnchants();
            if (!item.getItemMeta().getEnchants().isEmpty()) {
                for (Map.Entry<Enchantment, Integer> e : item.getItemMeta().getEnchants().entrySet()) {
                    if (e.getKey().equals(enchant)) {
                        return e.getValue();
                    }
                }
            }
        }
        return 0;
    }

    public static final String[] NUMERALS = {"0","I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};


}
