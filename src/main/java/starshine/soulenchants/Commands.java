package starshine.soulenchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length==1 && strings[0].equals("menu")){
            Player player = (Player) commandSender;
            Method.openMainMenu(player);
            return true;
        }

        if(strings.length==1 && strings[0].equals("handbook")){
            Player player = (Player) commandSender;
            Method.openEnchantMenu(player);
            return true;
        }

        if(strings.length==2 && strings[0].equals("handbook")){
            String playerName = strings[1];
            Player player = Bukkit.getPlayer(playerName);
            if(player==null){
                commandSender.sendMessage("该玩家不存在或离线");
                return false;
            }
            //sender.sendMessage("正在为玩家"+playerName+"开启附魔菜单");
            Method.openEnchantMenu(player);
            return true;

        }

        if(strings.length==1 && strings[0].equals("uuid")){
            Player player = (Player) commandSender;
            ItemStack handItem = player.getInventory().getItemInMainHand();
            String itemUUID = Method.getUUID(handItem);
            player.sendMessage(ChatColor.AQUA+itemUUID);
            return true;
        }

        if(strings.length==1 && strings[0].equals("furnace")){

            Player player = (Player) commandSender;
            player.getInventory().addItem(Method.getFurnace());
            return true;
        }

        if(strings.length==3 && strings[0].equals("bind")){
            Player player = (Player) commandSender;
            ItemStack handItem = player.getInventory().getItemInMainHand();

            String name =  strings[1];
            int level = Integer.parseInt(strings[2]);

            for(Enchantment enchantment : Method.enchantmentList()){

                if(enchantment.getName().equals(name)){

                    handItem.addUnsafeEnchantment(enchantment,level);
                    player.sendMessage("已为手上物品绑定 " + name + " "+ level);
                    break;

                }

            }
            return true;

        }

        if(strings.length==3 && strings[0].equals("item")&& strings[1].equals("setlevel")){

            int level = Integer.parseInt(strings[2]);

            Player player = (Player) commandSender;
            ItemStack handItem = player.getInventory().getItemInMainHand();

            Method.setUUIDLevel(handItem,level);

            player.sendMessage("请确认物品等级是否改变，物品没有等级时该指令不会有任何变化");
            return true;

        }

        if(strings.length==3 && strings[0].equals("item")&& strings[1].equals("setcharge")){

            int chargeValue = Integer.parseInt(strings[2]);

            Player player = (Player) commandSender;
            ItemStack handItem = player.getInventory().getItemInMainHand();

            Method.setUUIDChargeValue(handItem,chargeValue);

            player.sendMessage("请确认物品充能是否改变，物品没有充能时该指令不会有任何变化");
            return true;

        }


        commandSender.sendMessage(ChatColor.RED+"[SoulEnchants]请检查指令是否输入错误");

        return false;
    }
}
