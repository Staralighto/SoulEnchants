package starshine.soulenchants;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.*;




public class EventListener implements Listener {

    public static Set<Player> unleashSkillPlayer = new HashSet<>();

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

                Method.openEnchantMenu(player);
            }

            if (clickedSlot == 19) {

                Method.openEnchantMenu(player);
            }

            if (clickedSlot == 21) {

                Method.openEnchantMenu(player);
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

        //Bukkit.broadcastMessage("testEnchantMenu");


        if (inventory.getHolder() == player) {

            int clickedSlot = event.getRawSlot();

            event.setCancelled(true);

            //Bukkit.broadcastMessage("testEnchantMenu");

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

            if(currentPoint>=600){
                SoulEnchants.getPlayerPointsAPI().take(player.getUniqueId(),600);
            }else {
                player.sendMessage(ChatColor.RED+"钻币数量不足600，无法进行附魔");
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
        if (secondItem == null) {
            return;
        }
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
            if (secondItem.getEnchantments().containsKey(enchantment)) {

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
    public static void soulBladeSkillUnleash(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if(!Method.unleashSkillCheck(event,player,itemStack)){
            return;
        }

        if (!Method.isContainEnchantment(itemStack, SoulEnchants.soulBlade)) {
            return;
        }

        Method.showUnleashSkillBar(player);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                // 这里是任务执行的内容

                ItemStack currentItem = player.getInventory().getItemInMainHand();
                if(!Method.isContainEnchantment(currentItem,SoulEnchants.soulBlade)){
                    player.sendMessage("技能已取消释放");
                    this.cancel();
                    return;
                }


                int duration = 6000;

                PotionEffect speed = player.getPotionEffect(PotionEffectType.SPEED);
                int speedAmplifier = 2;
                if(speed!=null){
                    speedAmplifier = 2+ speed.getAmplifier();
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,duration,speedAmplifier));

                PotionEffect strength = player.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);
                int strengthAmplifier = 5;
                if(strength!=null){
                    strengthAmplifier = 5+ strength.getAmplifier();
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,duration,strengthAmplifier));

                Method.setUUIDChargeValue(itemStack,0);
                Method.showSkillDurationBar(player,duration);


            }

        }.runTaskLater(SoulEnchants.getPlugin(),40L);





    }

    @EventHandler
    public static void chiselingEvent1(BlockBreakEvent event) {

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
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

        Block block = event.getBlock();
        BlockFace face = player.getFacing();
        World world = block.getWorld();

        if(!Method.stoneBlockList().contains(block.getType())){
            return;
        }

        int centerX = block.getX();
        int centerY = block.getY();
        int centerZ = block.getZ();

        Vector direction = player.getLocation().getDirection();

        // 检查玩家是否朝向天空（Y轴正方向）
        if (direction.getY() > 0.7 || direction.getY() < -0.7) {

            for (int x = centerX - 1; x < centerX + 2; x++) {
                for (int z = centerZ - 1; z < centerZ + 2; z++) {

                    Location location = new Location(world, x, centerY, z);
                    Block nearByBlock = location.getBlock();

                    if (nearByBlock.getType() == Material.AIR) {
                        continue;
                    }
                    if (nearByBlock.getType() == Material.BEDROCK) {
                        continue;
                    }

                    if(Method.stoneBlockList().contains(nearByBlock.getType())){
                        nearByBlock.breakNaturally(tool);

                    }




                }

            }


            return;
        }


        if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {

            for (int x = centerX - 1; x < centerX + 2; x++) {
                for (int y = centerY - 1; y < centerY + 2; y++) {

                    Location location = new Location(world, x, y, centerZ);
                    Block nearByBlock = location.getBlock();

                    if (nearByBlock.getType() == Material.AIR) {
                        continue;
                    }

                    if (nearByBlock.getType() == Material.BEDROCK) {
                        continue;
                    }


                    if(Method.stoneBlockList().contains(nearByBlock.getType())){
                        nearByBlock.breakNaturally(tool);

                    }


                }

            }
            return;

        }
        if (face == BlockFace.EAST || face == BlockFace.WEST) {

            for (int z = centerZ - 1; z < centerZ + 2; z++) {
                for (int y = centerY - 1; y < centerY + 2; y++) {

                    Location location = new Location(world, centerX, y, z);
                    Block nearByBlock = location.getBlock();
                    if (nearByBlock.getType() == Material.AIR) {
                        continue;
                    }
                    if (nearByBlock.getType() == Material.BEDROCK) {
                        continue;
                    }


                    if(Method.stoneBlockList().contains(nearByBlock.getType())){
                        nearByBlock.breakNaturally(tool);

                    }


                }

            }
            return;

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
    public static void chiselingSkillUnleash(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if(!Method.unleashSkillCheck(event,player,itemStack)){
            return;
        };

        if (!Method.isContainEnchantment(itemStack, SoulEnchants.chiseling)) {
            return;
        }




        Method.showUnleashSkillBar(player);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                // 这里是任务执行的内容

                ItemStack currentItem = player.getInventory().getItemInMainHand();
                if(!Method.isContainEnchantment(currentItem,SoulEnchants.chiseling)){
                    player.sendMessage("技能已取消释放");
                    this.cancel();
                    return;
                }

                int amplifier = 5;

                PotionEffect effect = player.getPotionEffect(PotionEffectType.FAST_DIGGING);
                if(effect!=null){
                    amplifier = 5+ effect.getAmplifier();
                }

                int duration = 6000;

                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,duration,amplifier));

                Method.setUUIDChargeValue(itemStack,0);
                Method.showSkillDurationBar(player,duration);


            }

        }.runTaskLater(SoulEnchants.getPlugin(),40L);


    }



    @EventHandler

    public static void plentyEvent1(BlockBreakEvent event) {

        Player player = event.getPlayer();

        Block block = event.getBlock();

        if (!Method.oreBlockList().contains(block.getType())) {
            return;
        }

        ItemStack tool = player.getInventory().getItemInMainHand();

        Enchantment enchantment = SoulEnchants.plenty;

        if (!tool.getEnchantments().containsKey(enchantment)) {
            //Bukkit.broadcastMessage("No Enchant");
            return;
        }

        int level = Method.getLevel(tool, enchantment);

        int doubleChance = 0;

        int tripleChance = 0;



        if(level==1){doubleChance=Method.getEnchantConfig().getInt("plenty.level1",20);}

        if(level>=2){doubleChance=Method.getEnchantConfig().getInt("plenty.level2",100);}

        if(level>=3){tripleChance=Method.getEnchantConfig().getInt("plenty.level3",20);}

        boolean tripleOccur = false;

        List<ItemStack> drops = (List<ItemStack>) block.getDrops();

        if(plentySkillPlayers.contains(player)){

            for (ItemStack drop : drops) {

                int amount = drop.getAmount();

                drop.setAmount(amount*2);

                Method.addPlentyMark(drop);

                block.getDrops().clear();

                block.getWorld().dropItemNaturally(block.getLocation(),drop);
            }

        }

        if(new Random().nextInt(100)< tripleChance){


            for (ItemStack drop : drops) {

                int amount = drop.getAmount();

                drop.setAmount(amount*3);

                Method.addPlentyMark(drop);

                block.getDrops().clear();

                block.getWorld().dropItemNaturally(block.getLocation(),drop);



            }

            tripleOccur = true;
        }

        if(tripleOccur){
            return;
        }

        if (new Random().nextInt(100) < doubleChance) {


            for (ItemStack drop : drops) {

                int amount = drop.getAmount();

                drop.setAmount(amount*2);

                Method.addPlentyMark(drop);

                block.getDrops().clear();

                block.getWorld().dropItemNaturally(block.getLocation(),drop);
            }

        }






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

    public static HashSet<Player> plentySkillPlayers = new HashSet<>();

    @EventHandler
    public static void plentySkillUnleash(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if(!Method.unleashSkillCheck(event,player,itemStack)){
            return;
        };

        if (!Method.isContainEnchantment(itemStack, SoulEnchants.plenty)) {
            return;
        }

        Method.showUnleashSkillBar(player);



        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                // 这里是任务执行的内容

                ItemStack currentItem = player.getInventory().getItemInMainHand();
                if(!Method.isContainEnchantment(currentItem,SoulEnchants.plenty)){
                    player.sendMessage("技能已取消释放");
                    this.cancel();
                    return;
                }

                plentySkillPlayers.add(player);

                int duration = 6000;

                Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{plentySkillPlayers.remove(player);},duration);

                Method.setUUIDChargeValue(itemStack,0);
                Method.showSkillDurationBar(player,duration);


            }

        }.runTaskLater(SoulEnchants.getPlugin(),40L);

    }

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
    public static void accurateSkillUnleash(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if(!Method.unleashSkillCheck(event,player,itemStack)){
            return;
        };

        if (!Method.isContainEnchantment(itemStack, SoulEnchants.accurate)) {
            return;
        }

        Method.showUnleashSkillBar(player);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                // 这里是任务执行的内容

                ItemStack currentItem = player.getInventory().getItemInMainHand();
                if(!Method.isContainEnchantment(currentItem,SoulEnchants.accurate)){
                    player.sendMessage("技能已取消释放");
                    this.cancel();
                    return;
                }

                int duration = 6000;

                accurateSkillPlayers.add(player);

                Bukkit.getScheduler().runTaskLater(SoulEnchants.getPlugin(),()->{accurateSkillPlayers.remove(player);},duration);

                Method.setUUIDChargeValue(itemStack,0);
                Method.showSkillDurationBar(player,duration);


            }

        }.runTaskLater(SoulEnchants.getPlugin(),40L);

    }

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

}
