package starshine.soulenchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Method {

    public static List<Enchantment> enchantmentList (){

        List<Enchantment> list = new ArrayList<>();
        list.add(SoulEnchants.soulBlade);
        list.add(SoulEnchants.chiseling);
        list.add(SoulEnchants.plenty);
        list.add(SoulEnchants.accurate);
        list.add(SoulEnchants.vision);
        list.add(SoulEnchants.diligent);


        return list;


    }

    public static void openMainMenu(Player player) {

        Inventory inventory = Bukkit.createInventory(player, 54,  "SoulEnchants");


        ItemStack enchantMenu = new ItemStack(Material.ENCHANTED_BOOK);
        Method.setName(enchantMenu, ChatColor.AQUA + "打开附魔菜单" );
        Method.addLore(enchantMenu, ChatColor.WHITE + "在这里获取附魔");

        ItemStack handbook = new ItemStack(Material.BOOK);
        Method.setName(enchantMenu, ChatColor.AQUA + "打开图鉴菜单" );
        Method.addLore(enchantMenu, ChatColor.WHITE + "在这里查看附魔的效果");

        ItemStack furnace = new ItemStack(Material.FURNACE);
        Method.setName(furnace, ChatColor.AQUA + "获得附魔熔炉" );
        Method.addLore(furnace, ChatColor.WHITE + "放置后可以右键打开强化菜单");

        ItemStack paper = new ItemStack(Material.PAPER);





        inventory.setItem(10, enchantMenu);
        inventory.setItem(12, handbook);
        inventory.setItem(14, furnace);
        inventory.setItem(16, paper);
        inventory.setItem(19, paper);
        inventory.setItem(21, paper);
        inventory.setItem(23, paper);
        inventory.setItem(25, paper);
        inventory.setItem(28, paper);
        inventory.setItem(34, paper);
        inventory.setItem(47, paper);
        inventory.setItem(49, paper);
        inventory.setItem(51, paper);


        player.openInventory(inventory);




    }

    public static void openOPEnchantMenu(Player player) {

        Inventory inventory = Bukkit.createInventory(player, 54, "SoulEnchants Enchants");


        List<Enchantment> list = Method.enchantmentList();

        for(int i=0;  i<list.size(); i++){

            ItemStack enchantBook = new ItemStack(Material.ENCHANTED_BOOK);

            Enchantment enchantment = list.get(i);

            enchantBook.addUnsafeEnchantment(enchantment,enchantment.getMaxLevel());

            String name = enchantment.getName();

            Method.setName(enchantBook,ChatColor.AQUA+ name);

            inventory.setItem(i,enchantBook);

        }



        player.openInventory(inventory);

    }

    public static void openEnchantMenu(Player player) {

        Inventory inventory = Bukkit.createInventory(player, 54, "SoulEnchants Enchantments");

        List<String> soulBladeLore = Method.getEnchantConfig().getStringList("soul_blade.lore");
        Method.setSlotLore(inventory, SoulEnchants.soulBlade, 10, soulBladeLore);

        List<String> chiselingLore = Method.getEnchantConfig().getStringList("chiseling.lore");
        Method.setSlotLore(inventory, SoulEnchants.chiseling,12, chiselingLore);

        List<String> plentyLore = Method.getEnchantConfig().getStringList("plenty.lore");
        Method.setSlotLore(inventory, SoulEnchants.plenty, 14, plentyLore);

        List<String> accurateLore = Method.getEnchantConfig().getStringList("accurate.lore");
        Method.setSlotLore(inventory, SoulEnchants.accurate, 16, accurateLore);

        List<String> diligentLore = Method.getEnchantConfig().getStringList("diligent.lore");
        Method.setSlotLore(inventory, SoulEnchants.diligent, 19, diligentLore);

        List<String> visionLore = Method.getEnchantConfig().getStringList("vision.lore");
        Method.setSlotLore(inventory, SoulEnchants.vision, 21, visionLore);


        player.openInventory(inventory);

    }

    public static void openProcessMenu(Player player, ItemStack itemStack, Enchantment enchantment){

        Inventory inventory = Bukkit.createInventory(player, 54, "SoulEnchants Process");

        ItemStack whiteGlass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        Method.setName(whiteGlass,"");
        Method.addGlasses(inventory,whiteGlass);

        ItemStack enchantBook = new ItemStack(Material.ENCHANTED_BOOK);
        String name = enchantment.getName();
        Method.setName(enchantBook,name);
        enchantBook.addUnsafeEnchantment(enchantment,enchantment.getMaxLevel());

        inventory.setItem(4,enchantBook);

        inventory.setItem(13,itemStack);

        inventory.setItem(48,Method.confirmButton());
        inventory.setItem(51,Method.cancelButton());


    }



    public static Enchantment findFirstCustomEnchant(ItemStack itemStack){

        for(Enchantment enchantment: enchantmentList()){

            if(itemStack.getEnchantments().containsKey(enchantment)){

                return enchantment;

            }

        }

        return null;


    }

    public static void addGlasses(Inventory inventory,ItemStack itemStack){


        inventory.setItem(9,itemStack);
        inventory.setItem(10,itemStack);
        inventory.setItem(11,itemStack);
        inventory.setItem(12,itemStack);
        inventory.setItem(14,itemStack);
        inventory.setItem(15,itemStack);
        inventory.setItem(16,itemStack);
        inventory.setItem(17,itemStack);
        inventory.setItem(22,itemStack);

    }

    public static ItemStack confirmButton(){
        ItemStack itemStack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        Method.setName(itemStack, ChatColor.GREEN+"确认");
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        return itemStack;
    }

    public static boolean isConfirmButton(ItemStack itemStack){

        if(itemStack.getType()!=Material.GREEN_STAINED_GLASS_PANE){
            return false;
        }

        ItemMeta meta = itemStack.getItemMeta();

        if(meta==null){
            return false;
        }
        return meta.isUnbreakable();

    }

    public static ItemStack cancelButton(){
        ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        Method.setName(itemStack, ChatColor.GREEN+"取消");
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        return itemStack;
    }

    public static boolean isCancelButton(ItemStack itemStack){

        if(itemStack.getType()!=Material.RED_STAINED_GLASS_PANE){
            return false;
        }

        ItemMeta meta = itemStack.getItemMeta();

        if(meta==null){
            return false;
        }
        return meta.isUnbreakable();

    }


    public static String fullName (Enchantment enchantment , int level){

        String name = enchantment.getName();
        String numeral = Method.NUMERALS[level];

        return ChatColor.WHITE + name + numeral;


    }

    public static String process(Enchantment enchantment, int currentLevel){

        String name = enchantment.getName();
        String process = null;

        process = name + Method.NUMERALS[currentLevel] + "->" + name + Method.NUMERALS[currentLevel+1];

        if(currentLevel>=enchantment.getMaxLevel()){
            process = name + "附魔的等级已经达到最大";
        }

        return process;

    }








    public static void setSlotLore(Inventory inventory,Enchantment enchantment, int slot ,List<String> lore){

        String name = enchantment.getName();

        ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);

        Method.setName(itemStack,name);

        ItemMeta meta = itemStack.getItemMeta();
        if(meta==null){
            return;
        }

        lore.replaceAll(s -> ChatColor.GRAY + s);

        meta.setLore(lore);

        itemStack.setItemMeta(meta);

        inventory.setItem(slot,itemStack);

    }

    public static ItemStack getFurnace(){

        ItemStack itemStack = new ItemStack(Material.FURNACE);

        int furnaceMark = SoulEnchants.getPlugin().getConfig().getInt("furnace_mark",800251);

        ItemMeta meta = itemStack.getItemMeta();


        assert meta != null;
        meta.setCustomModelData(furnaceMark);
        meta.setUnbreakable(true);

        itemStack.setItemMeta(meta);

        return itemStack;

    }

    public static void levelUp(ItemStack itemStack, Enchantment enchantment){

        if(itemStack.getEnchantments().containsKey(enchantment)){

            int level = Method.getLevel(itemStack,enchantment);

            if(level+1>enchantment.getMaxLevel()){
                return;
            }

            itemStack.addUnsafeEnchantment(enchantment,level+1);

        }else {

            itemStack.addUnsafeEnchantment(enchantment,1);
        }

        Method.checkEnchantLore(itemStack);

    }

    public static void levelDown(ItemStack itemStack, Enchantment enchantment){

        if(itemStack.getEnchantments().containsKey(enchantment)){

            int level = Method.getLevel(itemStack,enchantment);

            if(level-1>enchantment.getStartLevel()){
                return;
            }

            itemStack.addUnsafeEnchantment(enchantment,level-1);

        }else {
            return;
        }

        Method.checkEnchantLore(itemStack);

    }



    public static void checkEnchantLore(ItemStack itemStack){

        if(itemStack==null){
            return;
        }

        for(Enchantment enchantment : Method.enchantmentList()){

            if(!itemStack.getEnchantments().containsKey(enchantment)){
                continue;
            }


            String name = enchantment.getName();
            int level = Method.getLevel(itemStack,enchantment);


            String romanLevel = Method.NUMERALS[level];
            String fullName = ChatColor.WHITE + name + " " + romanLevel;


            ItemMeta meta = itemStack.getItemMeta();
            if(meta==null){
                continue;
            }

            List<String> lore = meta.getLore();
            if(lore==null){
                lore=new ArrayList<>();
            }


            boolean replaced = false;

            for(int i=0;i<lore.size();i++){

                String line = lore.get(i);

                if(line.contains(name) && line.length()<=10){

                    lore.set(i,fullName);
                    replaced = true;
                    break;
                }

            }



            if(!replaced){
                lore.add(fullName);
            }



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

    public static void addLoreList(ItemStack itemStack,List<String> strings){

        ItemMeta meta = itemStack.getItemMeta();
        if(meta!=null){
            List<String> lore = meta.getLore();
            if(lore==null){
                lore=new ArrayList<>();
            }

            lore.addAll(strings);

            meta.setLore(lore);

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



    public static List<Material> oreBlockList(){

        List<Material> list = new ArrayList<>();
        list.add(Material.COAL);
        list.add(Material.IRON_ORE);
        list.add(Material.GOLD_ORE);
        list.add(Material.DIAMOND_ORE);
        list.add(Material.REDSTONE_ORE);
        list.add(Material.LAPIS_ORE);
        list.add(Material.ANCIENT_DEBRIS);

        return list;
    }

    public static List<Material> stoneBlockList(){

        List<Material> list = new ArrayList<>(Method.oreBlockList());

        list.add(Material.STONE);
        list.add(Material.COBBLESTONE);
        list.add(Material.GRANITE);
        list.add(Material.DIORITE);
        list.add(Material.ANDESITE);
        list.add(Material.SANDSTONE);
        list.add(Material.RED_SANDSTONE);
        list.add(Material.SMOOTH_STONE);
        list.add(Material.DIRT);
        list.add(Material.GRASS_BLOCK);
        list.add(Material.GRASS_BLOCK);
        list.add(Material.MYCELIUM);
        list.add(Material.PODZOL);



        return list;
    }

    public static List<Material> corpList(){

        List<Material> cropList = new ArrayList<>();

        cropList.add(Material.WHEAT);
        cropList.add(Material.CARROTS);
        cropList.add(Material.POTATOES);
        cropList.add(Material.BEETROOTS);
        cropList.add(Material.MELON_STEM);
        cropList.add(Material.PUMPKIN_STEM);
        cropList.add(Material.NETHER_WART);


        return cropList;
    }

    public static final String[] NUMERALS = {"O","I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    public static void shootArrow(Player player){

        Arrow arrow = (Arrow) player.launchProjectile(Arrow.class);
        arrow.setVelocity(player.getEyeLocation().getDirection().multiply(3)); // 设置弓箭的速度

    }

    public static FileConfiguration getEnchantConfig(){

        return YamlConfiguration.loadConfiguration(new File(SoulEnchants.getPlugin().getDataFolder(),"enchantment.yml"));
    }

    public static void saveEnchantConfig(){

        FileConfiguration config = Method.getEnchantConfig();

        try {
            config.save(new File(SoulEnchants.getPlugin().getDataFolder(), "enchantment.yml"));
        } catch (Exception ignored){

        }
    }

    public static FileConfiguration getDataConfig(){

        return YamlConfiguration.loadConfiguration(new File(SoulEnchants.getPlugin().getDataFolder(),"data.yml"));
    }

    public static void saveDataConfig(){

        FileConfiguration config = Method.getEnchantConfig();

        try {
            config.save(new File(SoulEnchants.getPlugin().getDataFolder(), "data.yml"));
        } catch (Exception ignored){

        }
    }

    public static void addPlentyMark(ItemStack itemStack){

        ItemMeta meta = itemStack.getItemMeta();

        int plentyMark = SoulEnchants.getPlugin().getConfig().getInt("plenty_mark",800250);

        if(meta==null){
            return;
        }

        meta.setCustomModelData(plentyMark);

        meta.setUnbreakable(true);

        itemStack.setItemMeta(meta);

    }

    public static boolean isFullyGrown(Block cropBlock) {
        if (cropBlock.getBlockData() instanceof Ageable) {
            Ageable ageable = (Ageable) cropBlock.getBlockData();
            return ageable.getAge() == ageable.getMaximumAge();
        }
        return false;
    }

    public static void initializeBasicData(ItemStack itemStack,Enchantment enchantment) {

        UUID itemUUID = UUID.randomUUID();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }
        NamespacedKey key = new NamespacedKey(JavaPlugin.getProvidingPlugin(SoulEnchants.class), "item_uuid");

        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, itemUUID.toString());

        itemStack.setItemMeta(meta);

        String type = "";

        if(enchantment==SoulEnchants.soulBlade){
            type = Method.getDataConfig().getString(itemUUID.toString() + ".type", "陵劲");
        }
        if(enchantment==SoulEnchants.chiseling){
            type = Method.getDataConfig().getString(itemUUID.toString() + ".type", "凿石");
        }
        if(enchantment==SoulEnchants.plenty){
            type = Method.getDataConfig().getString(itemUUID.toString() + ".type", "福临");
        }
        if(enchantment==SoulEnchants.accurate){
            type = Method.getDataConfig().getString(itemUUID.toString() + ".type", "精准");
        }
        if(enchantment==SoulEnchants.diligent){
            type = Method.getDataConfig().getString(itemUUID.toString() + ".type", "勤奋");
        }
        if(enchantment==SoulEnchants.vision){
            type = Method.getDataConfig().getString(itemUUID.toString() + ".type", "夜视");
        }

        int level = Method.getDataConfig().getInt(itemUUID.toString() + ".level", 1);
        int exp = Method.getDataConfig().getInt(itemUUID.toString() + ".exp", 0);
        int nextLevelExp = Method.getDataConfig().getInt(itemUUID.toString() + ".next_level_exp", 200);
        int chargeValue = Method.getDataConfig().getInt(itemUUID.toString() + ".charge_value", 0);
        int skillCharge = Method.getDataConfig().getInt(itemUUID.toString() + ".skill_charge", 100);

    }

    public static UUID getUUID(ItemStack itemStack) {
        NamespacedKey key = new NamespacedKey(JavaPlugin.getProvidingPlugin(SoulEnchants.class), "item_uuid");
        String uuidString = itemStack.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        return uuidString != null ? UUID.fromString(uuidString) : null;
    }

    public static String getDataTypeName(ItemStack itemStack){
        UUID itemUUID = Method.getUUID(itemStack);
        if(itemUUID!=null){
            return Method.getDataConfig().getString(itemUUID.toString() + ".type");
        }
        return null;
    }
    public static int getDataLevel(ItemStack itemStack){
        UUID itemUUID = Method.getUUID(itemStack);
        if(itemUUID!=null){
            return Method.getDataConfig().getInt(itemUUID.toString() + ".level");
        }
        return -1;
    }
    public static int getDataExp(ItemStack itemStack){
        UUID itemUUID = Method.getUUID(itemStack);
        if(itemUUID!=null){
            return Method.getDataConfig().getInt(itemUUID.toString() + ".exp");
        }
        return -1;
    }
}
