package starshine.soulenchants;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class FixedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length==1 && strings[0].equals("lore")){


            Player player = (Player) commandSender;
            ItemStack itemStack = player.getInventory().getItemInMainHand();

            Method.fixEnchantByLore(player,itemStack);

            return true;
        }







        return false;
    }
}
