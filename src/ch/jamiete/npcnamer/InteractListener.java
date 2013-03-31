package ch.jamiete.npcnamer;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InteractListener implements Listener {
    private final NPCNamer plugin;

    public InteractListener(final NPCNamer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEntityEvent event) {
        final ItemStack stack = event.getPlayer().getItemInHand();
        final ItemMeta meta = stack.getItemMeta();

        if (!this.plugin.isItemARenamer(stack)) {
            return;
        }

        if (event.getRightClicked().getType() == EntityType.PLAYER) {
            event.getPlayer().sendMessage(ChatColor.RED + "You can't name players.");
            return;
        }

        if (this.plugin.doesItemHaveName(stack)) {
            if (!event.getPlayer().hasPermission("npcnamer.use")) {
                event.getPlayer().sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this action. Please contact the server administrators if you believe that this is in error.");
                return;
            }

            final String name = meta.getLore().get(0);

            final LivingEntity entity = (LivingEntity) event.getRightClicked();
            if (entity instanceof Tameable) {
                final Tameable tam = (Tameable) entity;
                if (tam.isTamed() && !tam.getOwner().getName().equals(event.getPlayer().getName())) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot rename tamed animals that aren't yours.");
                    return;
                }
            }

            entity.setCustomName(name);
            entity.setCustomNameVisible(true);

            if (stack.getAmount() == 1) {
                event.getPlayer().setItemInHand(null);
            } else {
                stack.setAmount(stack.getAmount() - 1);
            }

            this.plugin.smokeShower(entity);
        } else {
            event.getPlayer().sendMessage(ChatColor.RED + "You need to use /npcnamer <name> to add a name to this tag.");
        }
    }
}
