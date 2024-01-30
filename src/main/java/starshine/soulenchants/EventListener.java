package starshine.soulenchants;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.*;


public class EventListener implements Listener {



    @EventHandler
    public static void onMainMenu(InventoryClickEvent event) {


        Inventory inventory = event.getInventory();

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        InventoryView inventoryView = player.getOpenInventory();

        if (!inventoryView.getTitle().equals("SoulEnchants")) {
            return;
        }

        //Bukkit.broadcastMessage("testMainMenu");


        if (inventory.getHolder() == player && inventory.getSize() == 54) {

            //Bukkit.broadcastMessage("testMainMenu");

            int clickedSlot = event.getRawSlot();

            event.setCancelled(true);

            if (clickedSlot == 10) {

                Method.openOPEnchantMenu(player);

            }

            if (clickedSlot == 12) {

                Method.openEnchantMenu(player);
            }

            if (clickedSlot == 14) {

                ItemStack furnace = Method.getFurnace();
                player.getInventory().addItem(furnace);

            }

            if (clickedSlot == 16) {

                Bukkit.dispatchCommand(player,"soe item setcharge 100");

            }

            if (clickedSlot == 19) {

                Bukkit.dispatchCommand(player,"soe uuid");
            }

            if (clickedSlot == 21) {

                ItemStack itemStack = player.getInventory().getItemInMainHand();

                Method.fixEnchantByLore(player,itemStack);

            }
        }
    }

    @EventHandler
    public static void onOPEnchantMenu(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        InventoryView inventoryView = player.getOpenInventory();

        if (!inventoryView.getTitle().equals("SoulEnchants Enchants")) {
            return;
        }


        if (inventory.getHolder() == player) {



            event.setCancelled(true);


            ItemStack itemStack = event.getCurrentItem();

            if (itemStack == null) {
                return;
            }

            ItemStack handItem = player.getInventory().getItemInMainHand();

            for (Enchantment enchantment : Method.enchantmentList()) {

                if (itemStack.getEnchantments().containsKey(enchantment)) {

                    String name = enchantment.getName();

                    int level = Method.getLevel(handItem,enchantment);

                    if(level>=enchantment.getMaxLevel()){
                        player.sendMessage(ChatColor.WHITE + name + "附魔等级已经达到最大");
                        return;
                    }

                    if (event.getAction() == InventoryAction.PICKUP_ALL) {
                        Method.levelUp(handItem, enchantment);
                        //Bukkit.broadcastMessage("testEnchantMenu");

                        player.sendMessage("已为手上物品的 " + name + " 提升1级");
                    }

                    if (event.getAction() == InventoryAction.PICKUP_HALF) {
                        Method.levelDown(handItem, enchantment);
                        //Bukkit.broadcastMessage("testEnchantMenu");

                        player.sendMessage("已为手上物品的 " + name + " 降低1级");
                    }

                }


            }

        }
    }

    @EventHandler
    public static void onEnchantMenu(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        InventoryView inventoryView = player.getOpenInventory();

        if (!inventoryView.getTitle().equals("SoulEnchants Enchantments")) {
            return;
        }

        event.setCancelled(true);

        ItemStack itemStack = event.getCurrentItem();

        if (itemStack == null) {
            return;
        }
        ItemMeta meta = itemStack.getItemMeta();

        if (meta == null) {
            return;
        }



        ItemStack handItem = player.getInventory().getItemInMainHand();

        ItemStack handItemCopy = new ItemStack(handItem);

        if(itemStack.getType().equals(Material.NETHERITE_PICKAXE)){
            //Leveling up efficiency enchantment level

            if(Method.getLevel(handItem,SoulEnchants.chiseling)==0){
                player.sendMessage(ChatColor.RED+"手上的物品没有凿石附魔，无法升级效率附魔");
                return;
            }
            if(!Method.checkPointsForEfficiency(handItem,player)){
                player.sendMessage(ChatColor.RED+"拥有的钻币不足，无法升级效率附魔");
                return;
            }

            Method.takePointsForEfficiency(handItem,player);

            int efficiencyLevel = handItem.getEnchantmentLevel(Enchantment.DIG_SPEED);
            handItem.addUnsafeEnchantment(Enchantment.DIG_SPEED,efficiencyLevel+1);
            player.closeInventory();
            return;
        }



        for(Enchantment enchantment : Method.enchantmentList()){

            if(handItem.getEnchantments().containsKey(SoulEnchants.chiseling)){
                if(itemStack.getItemMeta().getDisplayName().contains("福临")){
                    player.sendMessage(ChatColor.RED+"手上物品拥有凿石附魔，无法获得福临附魔");
                    return;
                }
            }
            if(handItem.getEnchantments().containsKey(SoulEnchants.plenty)){
                if(itemStack.getItemMeta().getDisplayName().contains("凿石")){
                    player.sendMessage(ChatColor.RED+"手上物品拥有福临附魔，无法获得凿石附魔");
                    return;
                }
            }



            if(enchantment.getName().contains(itemStack.getItemMeta().getDisplayName())){
                Method.openProcessMenu(player,handItemCopy,enchantment);
            }


        }




    }





    @EventHandler
    public static void onProcessMenu(InventoryClickEvent event) throws IOException {

        Inventory inventory = event.getInventory();

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        InventoryView inventoryView = player.getOpenInventory();

        if (!inventoryView.getTitle().equals("SoulEnchants Process")) {
            return;
        }


        ItemStack clickedItem = event.getCurrentItem();

        if(clickedItem==null){
            return;
        }

        event.setCancelled(true);

        //Bukkit.broadcastMessage("cancel event");

        if(event.getSlot()==48){

            //Bukkit.broadcastMessage("confirm");



            ItemStack enchantBook = inventory.getItem(4);
            //Target item only for showing, same data as hand item
            ItemStack targetItem = inventory.getItem(13);

            if(enchantBook==null){return;}
            if(targetItem==null){return;}

            Enchantment enchantment = Method.findFirstCustomEnchant(enchantBook);

            if(enchantment==null){return;}

            String name = enchantment.getName();

            int enchantLevel = Method.getLevel(targetItem,enchantment);

            if(enchantLevel>=enchantment.getMaxLevel()){
                player.sendMessage(ChatColor.WHITE+name + ChatColor.GRAY+"附魔等级已达到最大");
                return;
            }

            if(!Method.reachRequireLevel(targetItem,enchantLevel)){
                player.sendMessage(ChatColor.RED + "物品等级未达到附魔的需求！");
                return;
            }


            //Cost the points to enchant
            int currentPoint = SoulEnchants.getPlayerPointsAPI().look(player.getUniqueId());
            int requirePoint = SoulEnchants.getPlugin().getConfig().getInt("points_on_enchant",200);
            if(currentPoint>=requirePoint){
                SoulEnchants.getPlayerPointsAPI().take(player.getUniqueId(),requirePoint);
            }else {
                player.sendMessage(ChatColor.RED+"钻币数量不足"+requirePoint+"，无法进行附魔");
                return;
            }



            //Finally level up hand item , not the shown item.
            ItemStack handItem = player.getInventory().getItemInMainHand();
            Method.levelUp(handItem,enchantment);
            player.sendMessage(ChatColor.WHITE+name + ChatColor.GRAY+"附魔等级成功提升");
            player.closeInventory();




        }

        if(event.getSlot()==50){
           Method.openEnchantMenu(player);
           //Bukkit.broadcastMessage("cancel");
        }







    }

    @EventHandler
    public static void onAnvilEnchant(PrepareAnvilEvent event) {

        ItemStack firstItem = event.getInventory().getItem(0);

        ItemStack secondItem = event.getInventory().getItem(1);

        ItemStack resultItem = event.getResult();

        if (firstItem == null) {
            return;
        }
        //Allow second item is null because of rename
        if (resultItem == null) {
            return;
        }


        ItemStack newResult = new ItemStack(resultItem);

        for (Enchantment enchantment : Method.enchantmentList()) {

            if (firstItem.getEnchantments().containsKey(enchantment)) {

                int level = Method.getLevel(firstItem, enchantment);

                if (newResult.getEnchantments().containsKey(enchantment)) {

                    newResult.addUnsafeEnchantment(enchantment, level);

                } else {

                    int newResultLevel = Method.getLevel(newResult, enchantment);

                    if (level >= newResultLevel) {
                        newResult.addUnsafeEnchantment(enchantment, level);
                    }

                    if (level < newResultLevel) {
                        continue;
                    }


                }


            }
            if (secondItem!=null && secondItem.getEnchantments().containsKey(enchantment)) {

                int level = Method.getLevel(firstItem, enchantment);

                if (newResult.getEnchantments().containsKey(enchantment)) {

                    newResult.addUnsafeEnchantment(enchantment, level);

                } else {

                    int newResultLevel = Method.getLevel(newResult, enchantment);

                    if (level >= newResultLevel) {
                        newResult.addUnsafeEnchantment(enchantment, level);
                    }

                    if (level < newResultLevel) {
                        continue;
                    }

                }

            }


        }

        Method.checkEnchantLore(newResult);

        event.setResult(newResult);

    }

    @EventHandler
    public static void onGrindstone(InventoryClickEvent event){

        if(event.getClickedInventory()==null){
            return;
        }

        if (event.getClickedInventory().getType() == InventoryType.GRINDSTONE && event.getSlotType()==InventoryType.SlotType.RESULT) {

            Player player = (Player) event.getWhoClicked();

            ItemStack firstItem = event.getInventory().getItem(0);
            ItemStack secondItem = event.getInventory().getItem(1);

            Inventory inventory = event.getClickedInventory();


            for(Enchantment enchantment: Method.enchantmentList()){
                if(firstItem!=null&&firstItem.getEnchantments().containsKey(enchantment)){


                    event.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED+"请不要将包含高级附魔的物品放入砂轮");

                    return;
                }
                if(secondItem!=null&&secondItem.getEnchantments().containsKey(enchantment)){
                    event.setCancelled(true);
                    player.closeInventory();

                    player.sendMessage(ChatColor.RED+"请不要将包含高级附魔的物品放入砂轮");
                    return;
                }
            }



        }


    }




    @EventHandler
    public static void onFurnacePlace(BlockPlaceEvent event) {

        Block block = event.getBlock();

        if (block.getType() != Method.getFurnace().getType()) {
            return;
        }


        ItemStack itemStack = event.getItemInHand();
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        int furnaceMark = SoulEnchants.getPlugin().getConfig().getInt("furnace_mark", 800251);

        Player player = event.getPlayer();

        if (meta.isUnbreakable() && meta.getCustomModelData() == furnaceMark) {

            block.setMetadata("enchant_menu", new FixedMetadataValue(SoulEnchants.getPlugin(), true));

            player.sendMessage("特殊附魔熔炉已放置");
        }

    }

    @EventHandler
    public static void onFurnaceOpen(PlayerInteractEvent event) {

        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            Block block = event.getClickedBlock();
            Player player = event.getPlayer();

            if (block == null || block.getType().equals(Material.AIR)) {
                return;
            }
            if (block.getType() != Method.getFurnace().getType()) {
                return;
            }
            if (!block.hasMetadata("enchant_menu")) {
                //player.sendMessage("No Meta Data");
                return;
            }

            Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(), () -> {
                Method.openEnchantMenu(player);
            }, 1L);


        }


    }
    @EventHandler
    public static void onItemExpUpdate(InventoryCloseEvent event){

        Player player = (Player) event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if(handItem.getEnchantments().isEmpty()){
            return;
        }

        Enchantment enchant = null;
        for(Enchantment enchantment: Method.enchantmentList()){
            if(handItem.getEnchantments().containsKey(enchantment)){
                enchant = enchantment;
                break;
            }
        }
        if(enchant==null){
            return;
        }

        Method.updateUUIDLevelUp(handItem);

        Method.updateLevelUpLore(handItem);


    }

    private static final HashMap<UUID,BossBar> playersSkillBar = new HashMap<>();

    @EventHandler
    public static void showSkillBar(PlayerItemHeldEvent event){
         Player player = event.getPlayer();
         int newSlot = event.getNewSlot();
         ItemStack itemStack = player.getInventory().getItem(newSlot);

         if(itemStack==null){
             return;
         }

         Enchantment enchant = null;

         for(Enchantment enchantment: Method.enchantmentList()){
             if(itemStack.getEnchantments().containsKey(enchantment)){
                 enchant= enchantment;
                 break;
             }
         }


         if(enchant==null){
             return;
         }

         BossBar skillProgressBar = Method.skillProgressBar(player,itemStack,enchant);

         if(playersSkillBar.containsKey(player.getUniqueId())){

             BossBar bossBar = playersSkillBar.get(player.getUniqueId());
             bossBar.removeAll();
             playersSkillBar.remove(player.getUniqueId());

         }

         playersSkillBar.put(player.getUniqueId(),skillProgressBar);

         if(playersSkillBar.containsKey(player.getUniqueId())){

             BossBar bossBar = playersSkillBar.get(player.getUniqueId());

             if(bossBar==skillProgressBar){
                 skillProgressBar.addPlayer(player);
             }

         }



    }

    @EventHandler
    public static void removeSkillBar(PlayerItemHeldEvent event){

        Player player = event.getPlayer();
        int newSlot = event.getNewSlot();
        ItemStack itemStack = player.getInventory().getItem(newSlot);

        if(itemStack==null){
            if(playersSkillBar.containsKey(player.getUniqueId())){

                BossBar bossBar = playersSkillBar.get(player.getUniqueId());
                bossBar.removeAll();
                playersSkillBar.remove(player.getUniqueId());

            }
        }
        if(itemStack!=null && itemStack.getEnchantments().isEmpty()){
            if(playersSkillBar.containsKey(player.getUniqueId())){

                BossBar bossBar = playersSkillBar.get(player.getUniqueId());
                bossBar.removeAll();
                playersSkillBar.remove(player.getUniqueId());

            }
        }


    }








    @EventHandler

    public static void soulBladeEvent1(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            Player player = (Player) event.getDamager();
            LivingEntity entity = (LivingEntity) event.getEntity();

            ItemStack itemStack = player.getInventory().getItemInMainHand();
            Enchantment enchantment = SoulEnchants.soulBlade;

            if (!itemStack.getEnchantments().containsKey(enchantment)) {
                // Bukkit.broadcastMessage("No Enchant");
                return;
            }

            int level = Method.getLevel(itemStack, enchantment);

            double oldDamage = event.getDamage();
            double newDamage = oldDamage;


            if (level == 1) {
                double factor = Method.getEnchantConfig().getDouble("soul_blade.level1", 1.1);
                newDamage *= factor;
            }
            if (level == 2) {
                double factor = Method.getEnchantConfig().getDouble("soul_blade.level1", 1.5);
                newDamage *= factor;
                PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, 20, 1);
                player.addPotionEffect(effect);
            }
            if (level == 3) {
                double factor = Method.getEnchantConfig().getDouble("soul_blade.level1", 2.0);
                newDamage *= factor;
                PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, 40, 1);
                player.addPotionEffect(effect);
            }

            event.setDamage(newDamage);

        }
    }

    @EventHandler
    public static void soulBladeEvent2(EntityDamageByEntityEvent event) {


        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {

            Player player = (Player) event.getDamager();

            ItemStack itemStack = player.getInventory().getItemInMainHand();
            Enchantment enchantment = SoulEnchants.soulBlade;

            if (!itemStack.getEnchantments().containsKey(enchantment)) {
                // Bukkit.broadcastMessage("No Enchant");
                return;
            }

            int exp = 1;

            int itemExp = Method.getUUIDExp(itemStack);

            Method.setUUIDExp(itemStack,itemExp+exp);

            int chargeValue = Method.getUUIDChargeValue(itemStack);

            Method.setUUIDChargeValue(itemStack,chargeValue+1);

            BossBar skillProgress = playersSkillBar.get(player.getUniqueId());

            Method.updateSkillProgress(itemStack,skillProgress);

            



        }



    }


    @EventHandler
    public static void chiselingEvent1(BlockBreakEvent event) {

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        Block block = event.getBlock();
        Material blockType = block.getType();

        Enchantment enchantment = SoulEnchants.chiseling;

        if (!tool.getEnchantments().containsKey(enchantment)) {
            //Bukkit.broadcastMessage("No Enchant");
            return;
        }

        int level = Method.getLevel(tool, enchantment);


        if (level == 1) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20, 0, false, false, false));
        }
        if (level >= 2) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10, 0, false, false, false));
        }

        boolean allowChiseling = false;



        if(Method.stoneBlockList().contains(blockType)){
            //Bukkit.broadcastMessage(blockType + "方块类型属于石头");
            allowChiseling = true;
        }

        if(level>=3 && Method.oreBlockList().contains(blockType)){
            //Bukkit.broadcastMessage(blockType +"方块类型属于矿物");
            allowChiseling = true;
        }

        if(!allowChiseling){
            //Bukkit.broadcastMessage("不允许凿石");
            return;
        }


        BlockFace face = player.getFacing();
        World world = block.getWorld();



        int centerX = block.getX();
        int centerY = block.getY();
        int centerZ = block.getZ();

        Vector direction = player.getLocation().getDirection();

        boolean faceToSky = false;
        boolean triggered = false;



        // 检查玩家是否朝向天空（Y轴正方向）
        if (direction.getY() > 0.7 || direction.getY() < -0.7) {

            faceToSky = true;

            for (int x = centerX - 1; x < centerX + 2; x++) {
                for (int z = centerZ - 1; z < centerZ + 2; z++) {

                    if(x==centerX && z==centerZ){
                        continue;
                    }

                    Location location = new Location(world, x, centerY, z);
                    Block nearByBlock = location.getBlock();

                    if (nearByBlock.getType() == Material.AIR) {
                        continue;
                    }

                    if(Method.stoneBlockList().contains(nearByBlock.getType())){
                        nearByBlock.breakNaturally(tool);
                    }else if(level>=3 && Method.oreBlockList().contains(blockType)){
                        nearByBlock.breakNaturally(tool);
                    }



                }

            }


        }




        if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {

            if (!faceToSky) {

                for (int x = centerX - 1; x < centerX + 2; x++) {
                    for (int y = centerY - 1; y < centerY + 2; y++) {

                        if(x==centerX && y==centerY){
                            continue;
                        }

                        Location location = new Location(world, x, y, centerZ);
                        Block nearByBlock = location.getBlock();

                        if (nearByBlock.getType() == Material.AIR) {
                            continue;
                        }

                        if(Method.stoneBlockList().contains(nearByBlock.getType())){
                            nearByBlock.breakNaturally(tool);
                        }else if(level>=3 && Method.oreBlockList().contains(blockType)){
                            nearByBlock.breakNaturally(tool);
                        }


                    }

                }


            }
        }
        if (face == BlockFace.EAST || face == BlockFace.WEST) {

            if (!faceToSky) {

                for (int z = centerZ - 1; z < centerZ + 2; z++) {
                    for (int y = centerY - 1; y < centerY + 2; y++) {

                        if(z==centerZ && y==centerY){
                            continue;
                        }

                        if (triggered) {
                            continue;
                        }

                        Location location = new Location(world, centerX, y, z);
                        Block nearByBlock = location.getBlock();
                        if (nearByBlock.getType() == Material.AIR) {
                            continue;
                        }

                        if(Method.stoneBlockList().contains(nearByBlock.getType())){
                            nearByBlock.breakNaturally(tool);
                        }else if(level>=3 && Method.oreBlockList().contains(blockType)){
                            nearByBlock.breakNaturally(tool);
                        }


                    }

                }

            }
        }







    }

    @EventHandler
    public static void chiselingEvent2(BlockBreakEvent event) {

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Enchantment enchantment = SoulEnchants.chiseling;

        if (!tool.getEnchantments().containsKey(enchantment)) {
            //Bukkit.broadcastMessage("No Enchant");
            return;
        }

        Block block = event.getBlock();



        int exp = 0;

        if(new Random().nextInt(100)<20){
            //Only have 20% to get 1 exp when mining blocks.
            exp=1;
        }

        int itemExp = Method.getUUIDExp(tool);

        Method.setUUIDExp(tool,itemExp+exp);

        int chargeValue = Method.getUUIDChargeValue(tool);

        Method.setUUIDChargeValue(tool,chargeValue+1);

        BossBar skillProgress = playersSkillBar.get(player.getUniqueId());

        Method.updateSkillProgress(tool,skillProgress);



        



    }




    @EventHandler

    public static void plentyEvent1(BlockBreakEvent event) {

        Player player = event.getPlayer();

        Block block = event.getBlock();

        Material blockType = block.getType();

        if (!Method.oreBlockList().contains(blockType)) {
            return;
        }

        if(block.hasMetadata("placedBlock")){
            return;
        }

        ItemStack tool = player.getInventory().getItemInMainHand();

        Enchantment enchantment = SoulEnchants.plenty;

        if (!tool.getEnchantments().containsKey(enchantment)) {
            //Bukkit.broadcastMessage("No Enchant");
            return;
        }

        int level = Method.getLevel(tool, enchantment);



        List<ItemStack> drops = (List<ItemStack>) block.getDrops();



        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {

                if(!Method.checkPlentyBrokeBlock(block)){
                    return;
                }
                int doubleChance = 0;

                int tripleChance = 0;

                if(level==1){doubleChance=Method.getEnchantConfig().getInt("plenty.level1",20);}

                if(level>=2){doubleChance=Method.getEnchantConfig().getInt("plenty.level2",100);}

                if(level>=3){tripleChance=Method.getEnchantConfig().getInt("plenty.level3",100);}

                for(ItemStack drop : drops){

                    int amount = drop.getAmount();

                    if(new Random().nextInt(100)<doubleChance){
                        amount++;
                    }

                    if(new Random().nextInt(100)<tripleChance){
                        amount++;
                    }

                    if(plentyBuffedPlayers.contains(player)){
                        amount++;
                    }

                    if(tool.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)){

                        for(int i=0;i<tool.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);i++){
                            if(new Random().nextInt(100)<25){
                                amount++;
                            }

                        }

                    }

                    drop.setAmount(amount);

                    block.getWorld().dropItemNaturally(block.getLocation(),drop);


                }

            }

        }.runTaskLater(SoulEnchants.getPlugin(),5L);











    }



    @EventHandler
    public static void plentyEvent2(BlockPlaceEvent event){
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemInHand();
        ItemMeta meta = itemStack.getItemMeta();

        if(meta==null){
            return;
        }

        int plentyMark = SoulEnchants.getPlugin().getConfig().getInt("plenty_mark");

        if(meta.isUnbreakable() && meta.getCustomModelData()==plentyMark){

            event.setCancelled(true);
            player.sendMessage("赋予了恩泽，无法放置");


        }

    }


    @EventHandler
    public static void plentyEvent3(BlockBreakEvent event) {

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Enchantment enchantment = SoulEnchants.plenty;

        if (!tool.getEnchantments().containsKey(enchantment)) {
            //Bukkit.broadcastMessage("No Enchant");
            return;
        }

        Block block = event.getBlock();

        if(!Method.oreBlockList().contains(block.getType())){
            return;
        }

        int exp = Method.getOreBlockExp(block);

        int itemExp = Method.getUUIDExp(tool);

        Method.setUUIDExp(tool,itemExp+exp);

        int chargeValue = Method.getUUIDChargeValue(tool);

        Method.setUUIDChargeValue(tool,chargeValue+1);

        BossBar skillProgress = playersSkillBar.get(player.getUniqueId());

        Method.updateSkillProgress(tool,skillProgress);


    }

    @EventHandler
    public static void playerPlacedOreBlock(BlockPlaceEvent event){

        Block block = event.getBlock();
        if(!Method.oreBlockList().contains(block.getType())){
            return;
        }

        block.setMetadata("placedBlock",new FixedMetadataValue(SoulEnchants.getPlugin(),true));



    }

    public static HashSet<Player> plentyBuffedPlayers = new HashSet<>();




    @EventHandler
    public static void accurateEvent1(EntityDamageByEntityEvent event){

        if(event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity){
            Player player = (Player) event.getDamager();
            LivingEntity entity = (LivingEntity) event.getEntity();

            ItemStack weapon = player.getInventory().getItemInMainHand();
            Enchantment enchantment = SoulEnchants.accurate;

            if(!weapon.getEnchantments().containsKey(enchantment)){
                //Bukkit.broadcastMessage("No Enchant");
                return;
            }

            int level = Method.getLevel(weapon,enchantment);

            double oldDamage = event.getDamage();
            double newDamage = oldDamage;

            if(level==1){
                double factor = Method.getEnchantConfig().getDouble("accurate.level1",1.1);
                newDamage *= factor;
            }
            if(level==2){
                double factor = Method.getEnchantConfig().getDouble("accurate.level2",1.5);
                newDamage *= factor;

            }
            if(level>=3){
                double factor = Method.getEnchantConfig().getDouble("accurate.level3",2.0);
                newDamage *= factor;

            }

            event.setDamage(newDamage);

        }
    }

    @EventHandler

    public static void accurateEvent2(EntityShootBowEvent event){
        if(event.getEntity() instanceof Player){

            Player player = (Player) event.getEntity();

            ItemStack bow = player.getInventory().getItemInMainHand();

            Enchantment enchantment = SoulEnchants.accurate;

            if(!bow.getEnchantments().containsKey(enchantment)){
                //Bukkit.broadcastMessage("No Enchant");
                return;
            }

            int level = Method.getLevel(bow,enchantment);



            if(level==2){
                Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{

                    event.setCancelled(true);
                    Method.shootArrow(player);

                },20L);
            }

            if(level>=3){

                Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{

                    event.setCancelled(true);
                    Method.shootArrow(player);

                },10L);

            }

            if(accurateSkillPlayers.contains(player)){
                Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{

                    event.setCancelled(true);
                    Method.shootArrow(player);

                },5L);
            }

        }

    }

    @EventHandler
    public static void accurateEvent3(EntityShootBowEvent event){
        if(event.getEntity() instanceof Player){

            Player player = (Player) event.getEntity();

            ItemStack bow = player.getInventory().getItemInMainHand();

            Enchantment enchantment = SoulEnchants.accurate;

            if(!bow.getEnchantments().containsKey(enchantment)){
                //Bukkit.broadcastMessage("No Enchant");
                return;
            }

            int exp = 1;

            int itemExp = Method.getUUIDExp(bow);

            Method.setUUIDExp(bow,itemExp+exp);

            int chargeValue = Method.getUUIDChargeValue(bow);

            Method.setUUIDChargeValue(bow,chargeValue+1);

            BossBar skillProgress = playersSkillBar.get(player.getUniqueId());

            Method.updateSkillProgress(bow,skillProgress);

            



        }

    }

    public static HashSet<Player>  accurateSkillPlayers = new HashSet<>();



    @EventHandler

    public static void visionEvent(InventoryCloseEvent event) {

        if (event.getPlayer() instanceof Player) {

            Player player = (Player) event.getPlayer();
            ItemStack helmet = player.getInventory().getHelmet();

            Enchantment enchantment = SoulEnchants.vision;

            PotionEffectType waterBreathing = PotionEffectType.WATER_BREATHING;
            PotionEffectType nightVision = PotionEffectType.NIGHT_VISION;

            if (helmet != null && helmet.getEnchantments().containsKey(enchantment)) {

                int level = Method.getLevel(helmet, enchantment);

                if (level >= 1) {
                    PotionEffect effect = new PotionEffect(waterBreathing, 1000000, 1,false,false);
                    player.addPotionEffect(effect);
                }
                if (level >= 2) {
                    PotionEffect effect = new PotionEffect(nightVision, 1000000, 1,false,false);
                    player.addPotionEffect(effect);
                }

            } else {

                if (player.getPotionEffect(waterBreathing) != null) {

                    if (player.getPotionEffect(waterBreathing).getDuration() > 100000) {

                        player.removePotionEffect(waterBreathing);

                    }


                }

                if (player.getPotionEffect(nightVision) != null) {

                    if (player.getPotionEffect(nightVision).getDuration() > 100000) {

                        player.removePotionEffect(nightVision);

                    }


                }
            }

        }
    }
    @EventHandler

    public static void diligentEvent1(BlockBreakEvent event){

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Enchantment enchantment = SoulEnchants.diligent;

        if(!tool.getEnchantments().containsKey(enchantment)){
            return;
        }

        int level = Method.getLevel(tool,enchantment);

        Block block = event.getBlock();
        World world = block.getWorld();

        int centerX = block.getX();
        int centerY = block.getY();
        int centerZ = block.getZ();

        for(int x =centerX-1; x< centerX+2;x++){

            for(int z = centerZ-1;z<centerZ+2;z++){


                Location location = new Location(world,x,centerY,z);
                Block nearByBlock = location.getBlock();


                if(!Method.corpList().contains(nearByBlock.getType())){
                    continue;
                }


                if(nearByBlock.getType()==Material.BEDROCK){
                    continue;
                }

                nearByBlock.breakNaturally(tool);

            }

        }

        int doubleChance = 0;
        int tripleChance = 0;

        if(level==1){doubleChance=Method.getEnchantConfig().getInt("diligent.level1",20);}

        if(level>=2){doubleChance=Method.getEnchantConfig().getInt("diligent.level2",100);}

        if(level==3){doubleChance=Method.getEnchantConfig().getInt("diligent.level3",20);}

        boolean tripleOccur = false;

        if(new Random().nextInt(100)< tripleChance){

            List<ItemStack> drops = (List<ItemStack>) block.getDrops();
            for (ItemStack drop : drops) {

                if(!Method.isFullyGrown(block)){
                    continue;
                }

                int amount = drop.getAmount();

                drop.setAmount(amount*3);


                block.getDrops().clear();

                block.getWorld().dropItemNaturally(block.getLocation(),drop);

            }
            tripleOccur = true;
        }

        if(tripleOccur){
            return;
        }

        if (new Random().nextInt(100) < doubleChance) {

            List<ItemStack> drops = (List<ItemStack>) block.getDrops();
            for (ItemStack drop : drops) {

                int amount = drop.getAmount();

                drop.setAmount(amount*2);


                block.getDrops().clear();

                block.getWorld().dropItemNaturally(block.getLocation(),drop);
            }

        }


    }

    @EventHandler
    public static void diligentEvent2(BlockBreakEvent event) {

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Enchantment enchantment = SoulEnchants.diligent;

        if (!tool.getEnchantments().containsKey(enchantment)) {
            //Bukkit.broadcastMessage("No Enchant");
            return;
        }

        Block block = event.getBlock();

        if(!Method.oreBlockList().contains(block.getType())){
            return;
        }

        if(!Method.corpList().contains(block.getType())){
            return;
        }

        int exp = 1;

        int itemExp = Method.getUUIDExp(tool);

        Method.setUUIDExp(tool,itemExp+exp);

        int chargeValue = Method.getUUIDChargeValue(tool);

        Method.setUUIDChargeValue(tool,chargeValue+1);

        BossBar skillProgress = playersSkillBar.get(player.getUniqueId());

        Method.updateSkillProgress(tool,skillProgress);

        

    }

    private static Integer getSkillDuration(int level){

        int duration = 0;

        if(level==1){duration=2000;}
        if(level==2){duration=4000;}
        if(level==3){duration=6000;}

        return duration;

    }

    private static final HashMap<Player,Enchantment> playerSkillBuffMaps = new HashMap<>();

    private static void  soulBladeSkillEffect(Player player, ItemStack itemStack, int duration){

        PotionEffect speed = player.getPotionEffect(PotionEffectType.SPEED);
        int speedAmplifier = 1;
        if(speed!=null){
            speedAmplifier = 1+ speed.getAmplifier();
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,duration,speedAmplifier));

        PotionEffect strength = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);
        int strengthAmplifier = 4;
        if(strength!=null){
            strengthAmplifier = 4+ strength.getAmplifier();
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,duration,strengthAmplifier));

        Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{

            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);

        },duration);






    }

    private static void chiselingSkillEffect(Player player, ItemStack itemStack,int duration){

        int amplifier = 5;
        PotionEffect effect = player.getPotionEffect(PotionEffectType.FAST_DIGGING);

        if(effect!=null){
            amplifier = 5+ effect.getAmplifier();
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,duration,amplifier));

        Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{

            player.removePotionEffect(PotionEffectType.FAST_DIGGING);

        },duration);


    }

    private static void plentySkillEffect(Player player, ItemStack itemStack){

        int level = Method.getLevel(itemStack,SoulEnchants.plenty);
        int duration = getSkillDuration(level);

        plentyBuffedPlayers.add(player);
        Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{ plentyBuffedPlayers.remove(player);},duration);

    }

    private static void accurateSkillEffect(Player player, ItemStack itemStack){

        int level = Method.getLevel(itemStack,SoulEnchants.accurate);
        int duration = getSkillDuration(level);

        accurateSkillPlayers.add(player);
        Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{ accurateSkillPlayers.remove(player);},duration);

    }


    public static HashSet<Player> skillUnleashingPlayers = new HashSet<>();
    public static HashSet<Player> onSkillDurationPlayers = new HashSet<>();

    @EventHandler
    public static void skillUnleash(PlayerInteractEvent event){

        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();


        if(event.getAction()==Action.RIGHT_CLICK_AIR||event.getAction()==Action.RIGHT_CLICK_BLOCK) {

            if(Method.getUUIDChargeValue(itemStack)<100){
                //如果物品的充能未满，则直接无法释放技能
                return;
            }

            if(onSkillDurationPlayers.contains(player)){
                player.sendMessage(ChatColor.RED+"无法同时释放多个技能！");
                //阻止玩家在技能持续时间内释放其他技能
                return;
            }

            if(skillUnleashingPlayers.contains(player)){
                //如果玩家已经正在使用技能，则无需执行下面的技能逻辑，避免重复触发技能。
                return;
            }


            final Enchantment[] soulEnchant = {null};

            for (Enchantment enchantment : Method.enchantmentList()) {

                if (Method.isContainEnchantment(itemStack, enchantment)) {
                    soulEnchant[0] = enchantment;
                    break;
                }

            }


            if (soulEnchant[0] != null) {

                //The logic of unleash skill

                skillUnleashingPlayers.add(player);
                Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{skillUnleashingPlayers.remove(player);},45L);

                Method.skillUnleashEffect(player);
                Method.showUnleashSkillBar(player,soulEnchant[0]);

                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        // 这里是任务执行的内容

                        if(cancelSkillPlayers.contains(player)){
                            Method.skillUnleashCancelEffect(player);
                            player.sendMessage(ChatColor.RED+"已取消技能释放");
                            this.cancel();
                            return;
                        }

                        int level = Method.getLevel(itemStack,soulEnchant[0]);
                        int duration = getSkillDuration(level);


                        if(soulEnchant[0] ==SoulEnchants.soulBlade){
                            soulBladeSkillEffect(player,itemStack,duration);
                        }
                        if(soulEnchant[0] ==SoulEnchants.chiseling){
                            chiselingSkillEffect(player,itemStack,duration);
                        }
                        if(soulEnchant[0] ==SoulEnchants.plenty){
                            plentySkillEffect(player,itemStack);
                        }
                        if(soulEnchant[0] ==SoulEnchants.accurate){
                            accurateSkillEffect(player,itemStack);
                        }

                        Method.setUUIDChargeValue(itemStack,0);



                        createSkillDurationBar(player,duration);

                        // 为物品添加技能状态的元数据，方便后续检测物品是否为释放技能的物品
                        addSkillStateData(itemStack);

                        // 延迟执行任务
                        Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(), () -> {

                            removeSkillStateData(itemStack)

                            ;}, duration);



                        //将玩家映射到对应的buff表
                        playerSkillBuffMaps.put(player,soulEnchant[0]);
                        Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{playerSkillBuffMaps.remove(player);},duration);

                        //将玩家加入到技能持续时间中的玩家表里，后续通过其他方法阻止玩家在技能持续时间内释放其他技能
                        onSkillDurationPlayers.add(player);
                        Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{onSkillDurationPlayers.remove(player);},duration);

                        //释放技能后，将充能重置为0
                        Method.setUUIDChargeValue(itemStack,0);

                    }

                }.runTaskLater(SoulEnchants.getPlugin(),40L);



            }

        }

    }

    private static final HashSet<Player> cancelSkillPlayers = new HashSet<>();
    @EventHandler
    public static void cancelUnleashSkill(PlayerInteractEvent event){

        Player player = event.getPlayer();

        if(event.getAction()==Action.LEFT_CLICK_AIR||event.getAction()==Action.LEFT_CLICK_BLOCK) {

            if(!skillUnleashingPlayers.contains(player)){
                return;
            }

            cancelSkillPlayers.add(player);
            player.sendMessage(ChatColor.RED + "正在尝试取消释放技能");
            Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(), () -> {
                cancelSkillPlayers.remove(player);
            }, 45L);


        }


    }

    private static final HashMap<Player, BossBar> playerSkillDurationBarMap = new HashMap<>();

    private static void createSkillDurationBar(Player player, int duration){

        BossBar bossBar = Bukkit.createBossBar(ChatColor.BOLD + "技能持续时间", BarColor.RED, BarStyle.SOLID);

        bossBar.addPlayer(player);

        playerSkillDurationBarMap.put(player,bossBar);
        Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{playerSkillDurationBarMap.remove(player);},duration);


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
                    this.cancel();
                }

                // 设置新的进度
                bossBar.setProgress(newProgress);

            }

        }.runTaskTimer(SoulEnchants.getPlugin(),0L,20L);

        Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{bossBar.removeAll();task.cancel();},duration);

    }



    private static void showSkillDurationBar(Player player){
        BossBar durationBar = playerSkillDurationBarMap.get(player);
        if (durationBar != null) {
            durationBar.setVisible(true);
        }
    }

    private static void hideSkillDurationBar(Player player){
         BossBar durationBar = playerSkillDurationBarMap.get(player);
        if (durationBar != null) {
            durationBar.setVisible(false);
        }
    }

    @EventHandler
    public static void skillDurationBarDisplay(PlayerItemHeldEvent event) {

        Player player = event.getPlayer();

        int previousSlot = event.getPreviousSlot();
        ItemStack previousItem = player.getInventory().getItem(previousSlot);

        int newSlot = event.getNewSlot();
        ItemStack newItem = player.getInventory().getItem(newSlot);


        boolean previousHasSkillState = itemHasSkillStateData(previousItem);
        boolean newHasSkillState = itemHasSkillStateData(newItem);

        if (previousHasSkillState) {
            hideSkillDurationBar(player);
        }
        if (newHasSkillState) {
            showSkillDurationBar(player);
        }


    }

    private static final HashMap<Player,List<PotionEffect>> tempPotionEffectSaver = new HashMap<>();

    private static void stopSkillBuff(Player player,ItemStack itemStack,Enchantment enchantment){



        int level =0;
        if(Method.isContainEnchantment(itemStack,enchantment)){
            level=Method.getLevel(itemStack,enchantment);
        }
        if(level==0) {
            return;
        }

        List<PotionEffect> buffEffects = new ArrayList<>();


        if(Method.isContainEnchantment(itemStack, SoulEnchants.soulBlade)){

            PotionEffect speedEffect = player.getPotionEffect(PotionEffectType.SPEED);
            PotionEffect strengthEffect = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);

            if(strengthEffect != null && speedEffect!=null) {
                buffEffects.add(speedEffect);
                buffEffects.add(strengthEffect);
                player.removePotionEffect(PotionEffectType.SPEED);
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                tempPotionEffectSaver.put(player, buffEffects);

                //player.sendMessage(buffEffects + "加入药水储存器");
            }

        }else if(Method.isContainEnchantment(itemStack, SoulEnchants.chiseling)) {

            PotionEffect fastDiggingEffect = player.getPotionEffect(PotionEffectType.FAST_DIGGING);
            if(fastDiggingEffect!=null) {
                buffEffects.add(fastDiggingEffect);
                player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                tempPotionEffectSaver.put(player, buffEffects);

                //player.sendMessage(buffEffects + "加入药水储存器");
            }
        }




    }

    private static void resumeSkillBuff(Player player,ItemStack itemStack,Enchantment enchantment){

        int level =0;
        if(Method.isContainEnchantment(itemStack,enchantment)){
            level=Method.getLevel(itemStack,enchantment);
        }
        if(level==0) {
            return;
        }


        if(tempPotionEffectSaver.containsKey(player)){

            List<PotionEffect> buffEffects = tempPotionEffectSaver.get(player);

            if(buffEffects==null){return;}

            int duration = 0;
            for(PotionEffect effect:buffEffects){
                if(effect!=null) {
                    player.addPotionEffect(effect);
                    duration = effect.getDuration();
                }
            }

            tempPotionEffectSaver.remove(player);

            BossBar durationBar = playerSkillDurationBarMap.get(player);

            /*
            if(durationBar!=null){
                double fullTime = getSkillDuration(level);
                double newProgress = duration/fullTime;

                if(newProgress>1.0){
                    newProgress=1.0;
                    hideSkillDurationBar(player);
                }
                if(newProgress<0.0){
                   newProgress=0.0;
                    hideSkillDurationBar(player);
                }

                durationBar.setProgress(newProgress);


            */


        }




    }

    @EventHandler
    public static void switchItemBuff(PlayerItemHeldEvent event){

        Player player = event.getPlayer();

        int previousSlot = event.getPreviousSlot();
        ItemStack previousItem = player.getInventory().getItem(previousSlot);

        int newSlot = event.getNewSlot();
        ItemStack newItem = player.getInventory().getItem(newSlot);

        for(Enchantment enchantment: Method.enchantmentList()){

            boolean previousHasSkillState = itemHasSkillStateData(previousItem);
            boolean newHasSkillState =  itemHasSkillStateData(newItem);

            if (previousHasSkillState) {
                //player.sendMessage(ChatColor.RED+ enchantment.getName()+"停");
                stopSkillBuff(player,previousItem,enchantment);
            }
            if (newHasSkillState){
                //player.sendMessage(ChatColor.GREEN + enchantment.getName()+ "开");
                resumeSkillBuff(player, newItem, enchantment);
            }
        }

    }

    private static void addSkillStateData(ItemStack itemStack){

        //添加技能持续时间的数据到物品上，方便之后检测物品是否处于技能释放的状态

        ItemMeta meta = itemStack.getItemMeta();
        if(meta!=null){
            meta.getPersistentDataContainer().set(
                    new NamespacedKey(SoulEnchants.getPlugin(), "onSkill"),
                    PersistentDataType.STRING,
                    "onSkill"
            );
            itemStack.setItemMeta(meta);
        }

    }

    private static void removeSkillStateData(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            NamespacedKey key = new NamespacedKey(SoulEnchants.getPlugin(), "onSkill");
            PersistentDataContainer container = meta.getPersistentDataContainer();
            if (container.has(key, PersistentDataType.STRING)) {
                container.remove(key);
                itemStack.setItemMeta(meta);
            }
        }
    }

    private static boolean itemHasSkillStateData(ItemStack itemStack){

        //检测物品是否处于技能状态

        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return false;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return false;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(SoulEnchants.getPlugin(), "onSkill");
        return container.has(key, PersistentDataType.STRING);

    }

}
