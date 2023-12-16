package starshine.soulenchants;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length==1 && strings[0].equals("menu")){
            Player player = (Player) commandSender;
            Method.openMainMenu(player);
        }


        return false;
    }
}
