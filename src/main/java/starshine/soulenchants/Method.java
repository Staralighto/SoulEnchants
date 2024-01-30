package starshine.soulenchants;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

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

        ItemStack beacon = new ItemStack(Material.BEACON);
        Method.setName(beacon, ChatColor.AQUA + "物品充能" );
        Method.addLore(beacon, ChatColor.WHITE + "将手上物品的能量设置为100");

        ItemStack nameTag = new ItemStack(Material.NAME_TAG);
        Method.setName(nameTag, ChatColor.AQUA + "UUID查询" );
        Method.addLore(nameTag, ChatColor.WHITE + "查询手上物品的UUID");

        ItemStack anvil = new ItemStack(Material.ANVIL);
        Method.setName(anvil, ChatColor.AQUA + "通过lore修复附魔" );
        Method.addLore(anvil, ChatColor.WHITE + "通过查询lore找回手上物品丢失的附魔");


        ItemStack paper = new ItemStack(Material.PAPER);





        inventory.setItem(10, enchantMenu);
        inventory.setItem(12, handbook);
        inventory.setItem(14, furnace);
        inventory.setItem(16, beacon);
        inventory.setItem(19, nameTag);
        inventory.setItem(21, anvil);
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

        ItemStack pickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemStack handItem = player.getInventory().getItemInMainHand();
        pickaxe.addEnchantment(Enchantment.DIG_SPEED,1);
        Method.addLore(pickaxe,ChatColor.WHITE+"拥有凿石附魔的物品可以升级效率");
        Method.addLore(pickaxe,ChatColor.WHITE+"需要手持物品拥有附魔：凿石");
        Method.addLore(pickaxe,ChatColor.WHITE+"点击升级效率附魔");
        Method.addLore(pickaxe,ChatColor.AQUA+"消耗钻币："+Method.requirePointsForEfficiency(handItem));


        inventory.setItem(49,pickaxe);


        player.openInventory(inventory);

    }

    public static void openProcessMenu(Player player, ItemStack itemStack, Enchantment enchantment){

        if(!enchantment.canEnchantItem(itemStack)){
            player.sendMessage(ChatColor.RED+"手上物品不符合该附魔要求的类型");
            return;
        }

        Inventory inventory = Bukkit.createInventory(player, 54, "SoulEnchants Process");

        ItemStack blueGlass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        Method.setName(blueGlass," ");
        Method.addGlasses(inventory,blueGlass);

        ItemStack enchantBook = new ItemStack(Material.ENCHANTED_BOOK);
        String name = enchantment.getName();
        Method.setName(enchantBook,name);
        enchantBook.addUnsafeEnchantment(enchantment,enchantment.getMaxLevel());

        int level = Method.getLevel(itemStack,enchantment);
        String process = Method.process(enchantment,level);
        Method.addLore(itemStack,process);
        Method.addLore(itemStack, ChatColor.WHITE + "最低需求物品等级："+Method.requireLevel(itemStack,level));
        //Display the point cost base on the config
        int requirePoint = SoulEnchants.getPlugin().getConfig().getInt("points_on_enchant",200);
        Method.addLore(itemStack, ChatColor.AQUA + "消耗钻币："+requirePoint);

        inventory.setItem(4,enchantBook);

        inventory.setItem(13,itemStack);

        inventory.setItem(48,Method.confirmButton());
        inventory.setItem(50,Method.cancelButton());

        player.openInventory(inventory);


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
        if(meta!=null){
            meta.setUnbreakable(true);
        }
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
        if(meta!=null){
            meta.setUnbreakable(true);
        }
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

        process = ChatColor.GOLD + name + Method.NUMERALS[currentLevel] + "->" + name + Method.NUMERALS[currentLevel+1];

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

            Method.makeItemData(itemStack,enchantment);


        }

        Method.checkEnchantLore(itemStack);

        Method.checkExpLore(itemStack);

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

        Method.checkExpLore(itemStack);

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

    public static void fixEnchantByLore(Player player,ItemStack itemStack){

        if(itemStack==null){
            return;
        }

        boolean fixed = false;

        for(Enchantment enchantment : Method.enchantmentList()){

            if(itemStack.getEnchantments().containsKey(enchantment)){
                continue;
            }

            String name = enchantment.getName();

            ItemMeta meta = itemStack.getItemMeta();

            if(meta==null){
                continue;
            }

            List<String> lore = meta.getLore();

            if(lore==null){
                lore=new ArrayList<>();
            }


            for (String line : lore) {

                if (line.contains(name)) {

                    String numeralPart = line.split(" ")[1];
                    int level = Method.romanToArabic(numeralPart);

                    for (int round = 0; round < level; round++) {
                        Method.levelUp(itemStack, enchantment);
                    }

                    player.sendMessage(ChatColor.GOLD+"成功修复"+name+"附魔");

                    fixed= true;

                    break;


                }

            }

        }

        if(!fixed){
            player.sendMessage(ChatColor.AQUA+"手上物品没有需要修复的附魔或未查询成功");
        }


    }

    public static void checkExpLore(ItemStack itemStack){

        ItemMeta meta = itemStack.getItemMeta();

        if(meta==null){
            return;
        }
        List<String> lore = meta.getLore();

        if(lore==null){
            lore = new ArrayList<>();
        }

        boolean haveLevelLore = false;

        for (String line : lore) {

            if (line.contains("等级")) {
                haveLevelLore = true;
                break;
            }
        }
        if(!haveLevelLore){
            lore.add(ChatColor.GRAY+"等级: 1");
        }

        boolean haveExpLore = false;

        for (String line : lore) {

            if (line.contains("经验值")) {
                haveExpLore = true;
                break;
            }
        }
        if(!haveExpLore){
            lore.add(ChatColor.GRAY+"经验值: 0/100");
        }

        meta.setLore(lore);

        itemStack.setItemMeta(meta);


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
        list.add(Material.COAL_ORE);
        list.add(Material.IRON_ORE);
        list.add(Material.GOLD_ORE);
        list.add(Material.DIAMOND_ORE);
        list.add(Material.REDSTONE_ORE);
        list.add(Material.LAPIS_ORE);
        list.add(Material.EMERALD_ORE);
        list.add(Material.ANCIENT_DEBRIS);

        return list;
    }

    public static List<Material> fortuneBlockList(){

        List<Material> list = new ArrayList<>();
        list.add(Material.COAL_ORE);
        list.add(Material.DIAMOND_ORE);
        list.add(Material.REDSTONE_ORE);
        list.add(Material.LAPIS_ORE);
        list.add(Material.EMERALD_ORE);

        return list;
    }

    public static boolean checkPlentyBrokeBlock(Block block){
        Location location = block.getLocation();
        World world = location.getWorld();
        if(world==null){
            return false;
        }
        if(location.getBlock().getType()!=Material.AIR){
            return false;
        }
        Particle particle = Particle.CLOUD;
        world.spawnParticle(particle,location,3,0.5,0.5,0.5,0.5);
        return true;
    }


    public static int getOreBlockExp(Block block){

        int exp = 0;

        HashMap<Material,Integer> map = new HashMap<>();
        map.put(Material.COAL_ORE,1);
        map.put(Material.IRON_ORE,2);
        map.put(Material.GOLD_ORE,3);
        map.put(Material.DIAMOND_ORE,10);
        map.put(Material.REDSTONE_ORE,1);
        map.put(Material.LAPIS_ORE,1);
        map.put(Material.ANCIENT_DEBRIS,30);

        if(map.get(block.getType())!=null){
            exp = map.get(block.getType());
        }

        return exp;
    }

    public static List<Material> stoneBlockList(){

        FileConfiguration config = SoulEnchants.getPlugin().getConfig();

        List<String> stringList = config.getStringList("stone_block");
        List<Material> stoneBlocklist = new ArrayList<>();

        for(String string : stringList){
            stoneBlocklist.add(Material.valueOf(string));
        }


        if(stoneBlocklist.isEmpty()) {
            //Back up default  stone block
            stoneBlocklist.add(Material.STONE);
            stoneBlocklist.add(Material.COBBLESTONE);
            stoneBlocklist.add(Material.GRANITE);
            stoneBlocklist.add(Material.DIORITE);
            stoneBlocklist.add(Material.ANDESITE);
            stoneBlocklist.add(Material.SANDSTONE);
            stoneBlocklist.add(Material.RED_SANDSTONE);
            stoneBlocklist.add(Material.DIRT);
            stoneBlocklist.add(Material.GRASS_BLOCK);
            stoneBlocklist.add(Material.GRASS_BLOCK);
            stoneBlocklist.add(Material.MYCELIUM);
            stoneBlocklist.add(Material.PODZOL);
            stoneBlocklist.add(Material.END_STONE);
        }



        return stoneBlocklist;
    }



    public static List<Material> corpList(){

        List<Material> cropList = new ArrayList<>();

        cropList.add(Material.WHEAT);
        cropList.add(Material.CARROTS);
        cropList.add(Material.POTATOES);
        cropList.add(Material.BEETROOTS);
        cropList.add(Material.NETHER_WART);


        return cropList;
    }

    public static final String[] NUMERALS = {"O","I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    public static void shootArrow(Player player){

        Arrow arrow = (Arrow) player.launchProjectile(Arrow.class);
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
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

        return YamlConfiguration.loadConfiguration(new File(SoulEnchants.getPlugin().getDataFolder(), "itemdata.yml"));
    }

    public static void saveDataConfig(){

        FileConfiguration config = Method.getDataConfig();

        try {
            config.save(new File(SoulEnchants.getPlugin().getDataFolder(), "itemdata.yml"));
            //Bukkit.broadcastMessage("Saving Config");
        } catch (Exception e){
            Bukkit.getLogger().warning(e.toString());
        }
    }

    public static void addPlentyMark(List<ItemStack> list) {

        for (ItemStack itemStack : list) {
            ItemMeta meta = itemStack.getItemMeta();

            int plentyMark = SoulEnchants.getPlugin().getConfig().getInt("plenty_mark", 800250);

            if (meta == null) {
                return;
            }

            meta.setCustomModelData(plentyMark);

            meta.setUnbreakable(true);

            itemStack.setItemMeta(meta);

        }
    }

    public static boolean isFullyGrown(Block cropBlock) {
        if (cropBlock.getBlockData() instanceof Ageable) {
            Ageable ageable = (Ageable) cropBlock.getBlockData();
            return ageable.getAge() == ageable.getMaximumAge();
        }
        return false;
    }


    public static boolean isContainEnchantment(ItemStack itemStack,Enchantment enchantment){

        if(itemStack==null){
            return false;
        }
        return itemStack.getEnchantments().containsKey(enchantment);


    }



    public static void makeItemData(ItemStack itemStack, Enchantment enchantment) {
        String itemUUID = UUID.randomUUID().toString();
        ItemMeta meta = itemStack.getItemMeta();
        NamespacedKey key = new NamespacedKey(SoulEnchants.getPlugin(), "UUID");

        assert meta != null;
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, itemUUID);

        FileConfiguration config = SoulEnchants.getPlugin().getConfig();

        String name = enchantment.getName();

        // 使用set方法将读取的值设置到对应的键上
        config.set(itemUUID + ".typename", name);
        config.set(itemUUID + ".level", 1);
        config.set(itemUUID + ".exp", 0);
        config.set(itemUUID + ".charge_value", 0);


        // 保存配置文件
        SoulEnchants.getPlugin().saveConfig();

        itemStack.setItemMeta(meta);
    }

    public static String getUUID(ItemStack itemStack){
        NamespacedKey key = new NamespacedKey(SoulEnchants.getPlugin(),"UUID");

        ItemMeta meta = itemStack.getItemMeta();

        if(meta==null){
            return null;
        }
        String itemUUID =  meta.getPersistentDataContainer().get(key,PersistentDataType.STRING);

        return itemUUID;


    }

    public static String getUUIDTypeName(ItemStack itemStack){

        String itemUUID = Method.getUUID(itemStack);
        FileConfiguration config = SoulEnchants.getPlugin().getConfig();
        return config.getString(itemUUID + ".typename",null);

    }

    public static void setUUIDTypeName(ItemStack itemStack,String typename){

        String itemUUID = Method.getUUID(itemStack);
        FileConfiguration config = SoulEnchants.getPlugin().getConfig();
        config.set(itemUUID + ".typename",typename);
        SoulEnchants.getPlugin().saveConfig();

    }

    public static int getUUIDLevel(ItemStack itemStack){

        String itemUUID = Method.getUUID(itemStack);
        FileConfiguration config = SoulEnchants.getPlugin().getConfig();
        return config.getInt(itemUUID + ".level",1);

    }

    public static void setUUIDLevel(ItemStack itemStack,int level){

        String itemUUID = Method.getUUID(itemStack);
        FileConfiguration config = SoulEnchants.getPlugin().getConfig();
        config.set(itemUUID + ".level",level);
        SoulEnchants.getPlugin().saveConfig();

    }

    public static int getUUIDExp(ItemStack itemStack){

        String itemUUID = Method.getUUID(itemStack);
        FileConfiguration config = SoulEnchants.getPlugin().getConfig();
        return config.getInt(itemUUID + ".exp",0);

    }

    public static void setUUIDExp(ItemStack itemStack,int exp){

        String itemUUID = Method.getUUID(itemStack);
        FileConfiguration config = SoulEnchants.getPlugin().getConfig();
        config.set(itemUUID + ".exp",exp);
        //Bukkit.broadcastMessage("exp set to "+ exp);
        SoulEnchants.getPlugin().saveConfig();

    }

    public static int getUUIDChargeValue(ItemStack itemStack){

        String itemUUID = Method.getUUID(itemStack);
        FileConfiguration config = SoulEnchants.getPlugin().getConfig();
        return config.getInt(itemUUID + ".charge_value",1);

    }

    public static void setUUIDChargeValue(ItemStack itemStack,int chargeValue){

        String itemUUID = Method.getUUID(itemStack);
        FileConfiguration config = SoulEnchants.getPlugin().getConfig();
        config.set(itemUUID + ".charge_value",chargeValue);
        SoulEnchants.getPlugin().saveConfig();

    }

    public static void updateUUIDLevelUp(ItemStack itemStack){

        int itemExp = Method.getUUIDExp(itemStack);

        if(itemExp>=100){

            Method.setUUIDExp(itemStack,itemExp-100);

            int itemLevel = Method.getUUIDLevel(itemStack);

            Method.setUUIDLevel(itemStack,itemLevel+1);

        }

    }



    public static void updateLevelUpLore(ItemStack itemStack){


        ItemMeta meta = itemStack.getItemMeta();

        if(meta==null){
            return;
        }
        List<String> lore = meta.getLore();

        if(lore==null){
            lore = new ArrayList<>();
        }


        for (int i=0;i<lore.size();i++) {

            String line = lore.get(i);

            if(line.contains("经验值")){
                int UUIDExp = Method.getUUIDExp(itemStack);
                //The content after 3rd character is exp
                line = line.split(":")[0]+ ": " + UUIDExp;
                lore.set(i,line);
            }

            if(line.contains("等级")){
                int UUIDLevel = Method.getUUIDLevel(itemStack);
                //The content after 3rd character is exp
                line = line.split(":")[0]+ ": " + UUIDLevel;
                lore.set(i,line);
            }

        }




        meta.setLore(lore);

        itemStack.setItemMeta(meta);

    }

    public static BossBar skillProgressBar(Player player,ItemStack itemStack, Enchantment enchantment){

        String name = enchantment.getName();
        BossBar bossBar = Bukkit.createBossBar(ChatColor.BOLD +name, BarColor.BLUE, BarStyle.SOLID);
        bossBar.setProgress(0.0);
        int chargeValue = Method.getUUIDChargeValue(itemStack);
        double skillProgress = (double) chargeValue / 100;

        if(skillProgress< 0){
            skillProgress = 0;
        }

        if(skillProgress>1.0){
            skillProgress = 1.0;
        }

        bossBar.setProgress(skillProgress);

        return bossBar;
    }



    public static void updateSkillProgress(ItemStack itemStack,BossBar bossBar){

        if(bossBar==null){
            return;
        }

        int chargeValue = Method.getUUIDChargeValue(itemStack);
        double skillProgress = (double) chargeValue / 100;

        if(skillProgress<=0){skillProgress=0;}
        if(skillProgress>=1.0){skillProgress=1.0;}

        bossBar.setProgress(skillProgress);

    }

    public static void showSkillDurationBar(Player player, int duration){


        BossBar bossBar = Bukkit.createBossBar(ChatColor.BOLD + "技能持续时间", BarColor.RED, BarStyle.SOLID);

        bossBar.addPlayer(player);

        bossBar.setProgress(1.0);

        double time = (double) duration/20;

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                // 这里是任务执行的内容


                double newProgress = bossBar.getProgress() - (1/time);


                // 确保进度不小于0
                if (newProgress < 0) {
                    newProgress = 0;
                }

                // 设置新的进度
                bossBar.setProgress(newProgress);


            }

        }.runTaskTimer(SoulEnchants.getPlugin(),0L,20L);

        Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{bossBar.removeAll();task.cancel();},duration);


    }

    public static boolean unleashSkillCheck(PlayerInteractEvent event, Player player,ItemStack itemStack){

        if (Method.skillPlayerItemMap.containsKey(player)) {
            //if player already use skill, return false.
            return false;
        }

        if(event.getAction()==Action.RIGHT_CLICK_AIR || event.getAction()==Action.RIGHT_CLICK_BLOCK){
            return Method.getUUIDChargeValue(itemStack) >= 100;
        }

        return false;

    }

    public static void skillUnleashEffect(Player player){

        World world = player.getWorld();
        Location location = player.getLocation();

        world.playSound(location,Sound.BLOCK_BEACON_POWER_SELECT,3f,3f);

        Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{

            world.spawnParticle(Particle.CLOUD,location,15);
            world.playSound(location,Sound.BLOCK_BEACON_ACTIVATE,3f,3f);


        },40L);



    }

    public static void skillUnleashCancelEffect(Player player){

        World world = player.getWorld();
        Location location = player.getLocation();

        world.playSound(location,Sound.BLOCK_BEACON_DEACTIVATE,2f,2f);
        world.spawnParticle(Particle.REDSTONE,location,15);


    }



    public static HashMap<Player,ItemStack> skillPlayerItemMap = new HashMap<>();

    public static int requireLevel(ItemStack  itemStack, int enchantLevel){
        if(enchantLevel==0){
            //Level 0->1 require level
            return 0;

        }
        if(enchantLevel==1){
            //Level 1->2 require level
            return 100;

        }
        if(enchantLevel==2){
            //Level 2->3 require level
            return 200;
        }


        return 99999;
    }

    public static boolean reachRequireLevel(ItemStack itemStack, int enchantLevel){

        int UUIDLevel = Method.getUUIDLevel(itemStack);

        if(enchantLevel==0){
            //Level 0->1 require level
            return true;

        }
        if(enchantLevel==1){
            //Level 1->2 require level
            if(UUIDLevel>=100){
                return true;
            }

        }
        if(enchantLevel==2){
            //Level 2->3 require level
            if(UUIDLevel>=200){
                return true;
            }

        }


        return false;



    }

    public static int requirePointsForEfficiency(ItemStack itemStack){

        int level = itemStack.getEnchantmentLevel(Enchantment.DIG_SPEED);

        int requirePoints = 50;

        if(level>=0){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase1",5);
        }
        if(level>=10){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase2",10);
        }
        if(level>=20){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase3",20);
        }
        if(level>=30){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase4",30);
        }
        if(level>=40){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase5",50);
        }

        return requirePoints;

    }

    public static boolean checkPointsForEfficiency(ItemStack itemStack, Player player){
        int level = itemStack.getEnchantmentLevel(Enchantment.DIG_SPEED);
        int points = SoulEnchants.getPlayerPointsAPI().look(player.getUniqueId());

        int requirePoints = 50;

        if(level>=0){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase1",5);
        }
        if(level>=10){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase2",10);
        }
        if(level>=20){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase3",20);
        }
        if(level>=30){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase4",30);
        }
        if(level>=40){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase5",50);
        }

        return points>=requirePoints;



    }
    public static void takePointsForEfficiency(ItemStack itemStack, Player player){

        int level = itemStack.getEnchantmentLevel(Enchantment.DIG_SPEED);


        // 根据附魔等级判断具体的效果
        int requirePoints = 50;

        if(level>=0){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase1",5);
        }
        if(level>=10){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase2",10);
        }
        if(level>=20){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase3",20);
        }
        if(level>=30){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase4",30);
        }
        if(level>=40){
            requirePoints = SoulEnchants.getPlugin().getConfig().getInt("point_on_efficiency.phase5",50);
        }

        // 扣取玩家点数
        SoulEnchants.getPlayerPointsAPI().take(player.getUniqueId(), requirePoints);

        player.sendMessage("已消耗"+requirePoints+"钻币进行效率附魔升级");



    }

    public static Set<Player> unleashSkillPlayer = new HashSet<>();

    public static void showUnleashSkillBar(Player player){

        if(unleashSkillPlayer.contains(player)){
            player.sendMessage("无法重复释放技能");
            return;
        }else {
            unleashSkillPlayer.add(player);
        }


        BossBar bossBar = Bukkit.createBossBar(ChatColor.BOLD + "技能正在释放", BarColor.RED, BarStyle.SOLID);

        bossBar.addPlayer(player);

        bossBar.setProgress(0.0);



        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                // 这里是任务执行的内容


                double newProgress = bossBar.getProgress() + ((double) 1 /40);


                // 确保进度不小于0
                if (newProgress < 0) {
                    newProgress = 0;
                }
                // 确保进度不大于1.0w
                if (newProgress > 1.0) {
                    newProgress = 1.0;
                }

                // 设置新的进度
                bossBar.setProgress(newProgress);


            }

        }.runTaskTimer(SoulEnchants.getPlugin(),0L,1L);

        Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{

            bossBar.removeAll();
            task.cancel();
            unleashSkillPlayer.remove(player);

        },40L);



    }


    private static final Map<Character, Integer> romanValues = new HashMap<>();

    static {
        romanValues.put('I', 1);
        romanValues.put('V', 5);
        romanValues.put('X', 10);
        romanValues.put('L', 50);
        romanValues.put('C', 100);
        romanValues.put('D', 500);
        romanValues.put('M', 1000);
    }

    public static int romanToArabic(String roman) {
        int result = 0;
        int prevValue = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            int currentValue = romanValues.get(roman.charAt(i));

            if (currentValue >= prevValue) {
                result += currentValue;
            } else {
                result -= currentValue;
            }

            prevValue = currentValue;
        }

        return result;
    }


















}
