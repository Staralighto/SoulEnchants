package starshine.soulenchants;

import com.google.common.util.concurrent.ServiceManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;
import java.util.Random;

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

        Bukkit.broadcastMessage("testMainMenu");



        if (inventory.getHolder() == player && inventory.getSize() == 54) {

            Bukkit.broadcastMessage("testMainMenu");

            int clickedSlot = event.getRawSlot();

            event.setCancelled(true);

            if (clickedSlot == 10) {

               Method.openEnchantMenu(player);

            }

            if (clickedSlot == 12) {

                Method.openEnchantMenu(player);
            }

            if (clickedSlot == 14) {

                Method.openEnchantMenu(player);
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
    public static void onEnchantMenu(InventoryClickEvent event){
        Inventory inventory = event.getInventory();

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        InventoryView inventoryView = player.getOpenInventory();

        if (!inventoryView.getTitle().equals("SoulEnchants Enchants")) {
            return;
        }

        Bukkit.broadcastMessage("testEnchantMenu");


        if (inventory.getHolder() == player && inventory.getSize() == 54) {

            int clickedSlot = event.getRawSlot();

            event.setCancelled(true);

            Bukkit.broadcastMessage("testEnchantMenu");

            ItemStack itemStack = event.getCurrentItem();

            if(itemStack==null){
                return;
            }

            ItemStack handItem = player.getInventory().getItemInMainHand();

            for(Enchantment enchantment : Method.enchantmentList()){

                if(itemStack.getEnchantments().containsKey(enchantment)){

                    String name = enchantment.getName();

                    if(event.getAction()== InventoryAction.PICKUP_ALL){
                        Method.levelUp(handItem,enchantment);
                        Bukkit.broadcastMessage("testEnchantMenu");

                        player.sendMessage("已为手上物品的 " +name + " 提升1级");
                    }

                    if(event.getAction()== InventoryAction.PICKUP_HALF){
                        Method.levelDown(handItem,enchantment);
                        Bukkit.broadcastMessage("testEnchantMenu");

                        player.sendMessage("已为手上物品的 " +name + " 降低1级");
                    }

                }


            }

        }
    }

    @EventHandler
    public static void onAnvilEnchant(PrepareAnvilEvent event){

        ItemStack firstItem = event.getInventory().getItem(0);

        ItemStack secondItem = event.getInventory().getItem(1);

        ItemStack resultItem = event.getResult();

        if(firstItem==null){
            return;
        }
        if(secondItem==null){
            return;
        }
        if(resultItem==null){
            return;
        }


        ItemStack newResult = new ItemStack(resultItem);

        for(Enchantment enchantment : Method.enchantmentList()){

            if(firstItem.getEnchantments().containsKey(enchantment)){

                int level = Method.getLevel(firstItem,enchantment);

                if(newResult.getEnchantments().containsKey(enchantment)) {

                    newResult.addUnsafeEnchantment(enchantment, level);

                } else {

                    int newResultLevel = Method.getLevel(newResult,enchantment);

                    if(level>=newResultLevel){
                        newResult.addUnsafeEnchantment(enchantment,level);
                    }

                    if(level<newResultLevel){
                        continue;
                    }



                }



            }
            if(secondItem.getEnchantments().containsKey(enchantment)){

                int level = Method.getLevel(firstItem,enchantment);

                if(newResult.getEnchantments().containsKey(enchantment)) {

                    newResult.addUnsafeEnchantment(enchantment, level);

                } else {

                    int newResultLevel = Method.getLevel(newResult,enchantment);

                    if(level>=newResultLevel){
                        newResult.addUnsafeEnchantment(enchantment,level);
                    }

                    if(level<newResultLevel){
                        continue;
                    }

                }

            }


        }

        Method.checkEnchantLore(newResult);

        event.setResult(newResult);

    }

    @EventHandler
    public static void updateEnchantLoreByClick(InventoryClickEvent event){

        ItemStack itemStack = event.getCurrentItem();

        if(itemStack==null){
            return;
        }
        Method.checkEnchantLore(itemStack);


    }

    @EventHandler
    public static void updateEnchantLoreByOpen(InventoryOpenEvent event) {

        ItemStack[] itemStacks = event.getInventory().getContents();
        for (ItemStack itemStack : itemStacks) {

            if (itemStack == null) {
                return;
            }

            Method.checkEnchantLore(itemStack);

        }
    }

    @EventHandler
    public static void updateEnchantLoreByDrag(InventoryDragEvent event) {

        ItemStack itemStack = event.getCursor();


        if (itemStack == null) {
            return;
        }

        Method.checkEnchantLore(itemStack);


    }



    @EventHandler

    public static void soulBladeEvent1(EntityDamageByEntityEvent event){

        if(event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity){
            Player player = (Player) event.getDamager();
            LivingEntity entity = (LivingEntity) event.getEntity();

            ItemStack itemStack = player.getInventory().getItemInMainHand();
            Enchantment enchantment = SoulEnchants.soulBlade;

            if(!itemStack.getEnchantments().containsKey(enchantment)){
                Bukkit.broadcastMessage("No Enchant");
                return;
            }

            int level = Method.getLevel(itemStack,enchantment);

            double oldDamage = event.getDamage();
            double newDamage = oldDamage;


            if(level==1){
                double factor = Method.getEnchantConfig().getDouble("soul_blade.level1",1.1);
                newDamage *= factor;
            }
            if(level==2){
                double factor = Method.getEnchantConfig().getDouble("soul_blade.level1",1.5);
                newDamage *= factor;
                PotionEffect effect = new PotionEffect(PotionEffectType.SPEED,20,1);
                player.addPotionEffect(effect);
            }
            if(level==3){
                double factor = Method.getEnchantConfig().getDouble("soul_blade.level1",2.0);
                newDamage *= factor;
                PotionEffect effect = new PotionEffect(PotionEffectType.SPEED,40,1);
                player.addPotionEffect(effect);
            }

            event.setDamage(newDamage);

        }
    }

    @EventHandler
    public static void chiselingEvent1(BlockBreakEvent event){

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Enchantment enchantment = SoulEnchants.chiseling;

        if(!tool.getEnchantments().containsKey(enchantment)){
            Bukkit.broadcastMessage("No Enchant");
            return;
        }

        int level = Method.getLevel(tool,enchantment);

        Block block = event.getBlock();
        BlockFace face = player.getFacing();
        World world = block.getWorld();

        int centerX = block.getX();
        int centerY = block.getY();
        int centerZ = block.getZ();

        Vector direction = player.getLocation().getDirection();

        // 检查玩家是否朝向天空（Y轴正方向）
        if (direction.getY() > 0.8 || direction.getY() < -0.8) {
            for(int x = centerX -1 ; x<= centerX +1; x++){
                for(int z= centerZ - 1; z<= centerZ + 1 ; z++){

                    Location location = new Location(world,x,centerY,z);
                    Block nearByBlock = location.getBlock();
                    if(nearByBlock.getType()== Material.AIR){
                        return;
                    }
                    if(!Method.stoneBlockList().contains(nearByBlock.getType())){
                        return;
                    }
                    nearByBlock.breakNaturally(tool);
                    if(level==1){
                        event.setCancelled(true);
                        break;
                    }

                }

            }


            return;
        }


        if(face==BlockFace.NORTH || face == BlockFace.SOUTH ){

            for(int x = centerX -1 ; x<= centerX + 1; x++){
                for(int y= centerY - 1; y<= centerY + 1 ; y++){

                    Location location = new Location(world,x,y,centerZ);
                    Block nearByBlock = location.getBlock();
                    if(nearByBlock.getType()== Material.AIR){
                        return;
                    }
                    if(!Method.stoneBlockList().contains(nearByBlock.getType())){
                        return;
                    }
                    nearByBlock.breakNaturally(tool);
                    if(level==1){
                        event.setCancelled(true);
                        break;
                    }

                }

            }

        }
        if(face==BlockFace.EAST || face == BlockFace.WEST ){

            for(int z = centerZ -1 ; z<= centerZ + 1; z++){
                for(int y= centerY - 1; y<= centerY + 1 ; y++){

                    Location location = new Location(world,centerX,y,z);
                    Block nearByBlock = location.getBlock();
                    if(nearByBlock.getType()== Material.AIR){
                        return;
                    }
                    if(!Method.stoneBlockList().contains(nearByBlock.getType())){
                        return;
                    }
                    nearByBlock.breakNaturally(tool);
                    if(level==1){
                        event.setCancelled(true);
                        break;
                    }

                }

            }

        }


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
            Bukkit.broadcastMessage("No Enchant");
            return;
        }

        int level = Method.getLevel(tool, enchantment);

        int doubleChance = 0;

        int tripleChance = 0;



        if(level==1){doubleChance=Method.getEnchantConfig().getInt("plenty.level1",20);}

        if(level>=2){doubleChance=Method.getEnchantConfig().getInt("plenty.level2",100);}

        if(level>=3){tripleChance=Method.getEnchantConfig().getInt("plenty.level3",20);}

        boolean tripleOccur = false;

        if(new Random().nextInt(100)< tripleChance){

            List<ItemStack> drops = (List<ItemStack>) block.getDrops();
            for (ItemStack drop : drops) {

                int amount = drop.getAmount();

                drop.setAmount(amount*3);

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
            }

        }


    }
    @EventHandler
    public static void accurateEvent1(EntityDamageByEntityEvent event){

        if(event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity){
            Player player = (Player) event.getDamager();
            LivingEntity entity = (LivingEntity) event.getEntity();

            ItemStack weapon = player.getInventory().getItemInMainHand();
            Enchantment enchantment = SoulEnchants.accurate;

            if(!weapon.getEnchantments().containsKey(enchantment)){
                Bukkit.broadcastMessage("No Enchant");
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
                Bukkit.broadcastMessage("No Enchant");
                return;
            }

            int level = Method.getLevel(bow,enchantment);

            if(level>=2){
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

        }

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
                    PotionEffect effect = new PotionEffect(waterBreathing, 100000, 1);
                    player.addPotionEffect(effect);
                }
                if (level >= 2) {
                    PotionEffect effect = new PotionEffect(nightVision, 100000, 1);
                    player.addPotionEffect(effect);
                }

            } else {

                if (player.getPotionEffect(waterBreathing) != null) {

                    if (player.getPotionEffect(waterBreathing).getDuration() > 10000) {

                        player.removePotionEffect(waterBreathing);

                    }


                }

                if (player.getPotionEffect(nightVision) != null) {

                    if (player.getPotionEffect(nightVision).getDuration() > 10000) {

                        player.removePotionEffect(nightVision);

                    }


                }
            }

        }
    }

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

        for(int x =centerX-1; x<= centerX+1;x++){

            for(int z = centerZ-1;z<=centerZ+1;z++){


                Location location = new Location(world,x,centerY,z);
                Block nearByBlock = location.getBlock();


                if(!Method.corpList().contains(nearByBlock.getType())){
                    return;
                }

                nearByBlock.breakNaturally(tool);

            }

        }

        int doubleChance = 0;
        int tripleChance = 0;

        if(level==1){doubleChance=20;}

        if(level>=2){doubleChance=100;}

        if(level==3){tripleChance=20;}

        boolean tripleOccur = false;

        if(new Random().nextInt(100)< tripleChance){

            List<ItemStack> drops = (List<ItemStack>) block.getDrops();
            for (ItemStack drop : drops) {

                int amount = drop.getAmount();

                drop.setAmount(amount*3);

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
            }

        }











    }

}
