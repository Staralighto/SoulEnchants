package starshine.soulenchants;

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

        }


        return false;
    }
}
