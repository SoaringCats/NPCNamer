package ch.jamiete.npcnamer;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandHandler implements CommandExecutor {
    private final NPCNamer plugin;

    public CommandHandler(final NPCNamer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            ItemStack stack = player.getItemInHand();
            ItemMeta meta = stack.getItemMeta();
            boolean doDrop = false;

            if (stack.getType() == Material.PAPER && this.plugin.isItemARenamer(stack)) {
                if (this.plugin.doesItemHaveName(stack)) {
                    player.sendMessage(ChatColor.RED + "This NPC Renamer already has a name attached.");
                } else {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.RED + "Oops. Usage is /" + label + " <name>");
                    } else {
                        String name = this.plugin.combineSplit(0, args, " ");
                        if (name.length() > 16 || name.length() < 1) {
                            player.sendMessage(ChatColor.RED + "Name must be at least one character and less than 16.");
                        } else {
                            player.sendMessage(ChatColor.GREEN + "Set name to " + name);
                            name = new StringBuilder().append(ChatColor.GRAY).append(ChatColor.ITALIC).append(name).toString();

                            if (stack.getAmount() != 1) {
                                stack.setAmount(stack.getAmount() - 1);
                                stack = stack.clone();
                                stack.setAmount(1);
                                meta = stack.getItemMeta();
                                doDrop = true;
                            }

                            meta.setLore(Arrays.asList(new String[] { name }));
                            stack.setItemMeta(meta);

                            if (doDrop) {
                                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), stack);
                            }
                        }
                    }
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Uh-oh. You need to be a player to use this command.");
        }
        return true;
    }
}
