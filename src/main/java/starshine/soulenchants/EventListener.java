package starshine.soulenchants;

import com.google.common.util.concurrent.ServiceManager;
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
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

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


        if (inventory.getHolder() == player && inventory.getSize() == 54) {

            int clickedSlot = event.getRawSlot();

            event.setCancelled(true);

            if (clickedSlot == 10) {


            }

            if (clickedSlot == 12) {


            }

            if (clickedSlot == 14) {


            }

            if (clickedSlot == 16) {


            }

            if (clickedSlot == 19) {


            }

            if (clickedSlot == 21) {


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

        if (!inventoryView.getTitle().equals("SoulEnchants")) {
            return;
        }


        if (inventory.getHolder() == player && inventory.getSize() == 54) {

            int clickedSlot = event.getRawSlot();

            event.setCancelled(true);

            ItemStack itemStack = event.getCurrentItem();

            if(itemStack==null){
                return;
            }

            for(Enchantment enchantment : Method.enchantmentList()){

                if(itemStack.getEnchantments().containsKey(enchantment)){

                    String name = enchantment.getName();

                    if(event.getAction()== InventoryAction.PICKUP_ALL){
                        Method.levelUp(itemStack,enchantment);

                        player.sendMessage("已为手上物品的 " +name + " 提升1级");
                    }

                    if(event.getAction()== InventoryAction.PICKUP_HALF){
                        Method.levelDown(itemStack,enchantment);

                        player.sendMessage("已为手上物品的 " +name + " 降低1级");
                    }

                }


            }

        }
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
                return;
            }

            int level = Method.getLevel(itemStack,enchantment);

            double oldDamage = event.getDamage();
            double newDamage = oldDamage;

            if(level==1){
                newDamage *= 1.1;
            }
            if(level==2){
                newDamage *= 1.5;
                PotionEffect effect = new PotionEffect(PotionEffectType.SPEED,20,1);
                player.addPotionEffect(effect);
            }
            if(level==3){
                newDamage *= 2;
                PotionEffect effect = new PotionEffect(PotionEffectType.SPEED,40,1);
                player.addPotionEffect(effect);
            }

            event.setDamage(newDamage);

        }
    }

    public static void chiselingEvent1(BlockBreakEvent event){

        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Enchantment enchantment = SoulEnchants.chiseling;

        if(!tool.getEnchantments().containsKey(enchantment)){
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
            for(int x = centerX -1 ; x< centerX + 1; x++){
                for(int z= centerZ - 1; z<centerZ + 1 ; z++){

                    Location location = new Location(world,x,centerY,z);
                    Block nearByBlock = location.getBlock();
                    if(nearByBlock.getType()== Material.AIR){
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

            for(int x = centerX -1 ; x< centerX + 1; x++){
                for(int y= centerY - 1; y<centerY + 1 ; y++){

                    Location location = new Location(world,x,y,centerZ);
                    Block nearByBlock = location.getBlock();
                    if(nearByBlock.getType()== Material.AIR){
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

            for(int z = centerZ -1 ; z< centerZ + 1; z++){
                for(int y= centerY - 1; y<centerY + 1 ; y++){

                    Location location = new Location(world,centerX,y,z);
                    Block nearByBlock = location.getBlock();
                    if(nearByBlock.getType()== Material.AIR){
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

}
