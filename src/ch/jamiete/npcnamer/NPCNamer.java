package ch.jamiete.npcnamer;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class NPCNamer extends JavaPlugin {
    public static String ITEM_NAME = ChatColor.RESET + "NPC Renamer";
    public static List<String> ITEM_LORE = Arrays.asList(new String[] { "Use /npcnamer <name> to add a name to this tag." });

    public String combineSplit(final int startIndex, final String[] string, final String seperator) {
        final StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i < string.length; i++) {
            builder.append(string[i]);
            builder.append(seperator);
        }
        builder.deleteCharAt(builder.length() - seperator.length());
        return builder.toString();
    }

    public boolean doesItemHaveName(final ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().size() == 1 && !NPCNamer.ITEM_LORE.get(0).equals(item.getItemMeta().getLore().get(0));
    }

    public boolean isItemARenamer(final ItemStack item) {
        return item.hasItemMeta() && item.getType() == Material.PAPER && NPCNamer.ITEM_NAME.equals(item.getItemMeta().getDisplayName());
    }

    @Override
    public void onEnable() {
        // Register recipes
        for (final Material food : Material.values()) {
            if (food.isEdible()) {
                final ItemStack result = new ItemStack(Material.PAPER);
                final ItemMeta meta = result.getItemMeta();
                meta.setDisplayName(NPCNamer.ITEM_NAME);
                meta.setLore(NPCNamer.ITEM_LORE);
                result.setItemMeta(meta);
                final ShapelessRecipe recipe = new ShapelessRecipe(result);
                recipe.addIngredient(Material.PAPER);
                recipe.addIngredient(Material.STRING);
                recipe.addIngredient(food);
                this.getServer().addRecipe(recipe);
            }
        }

        // Register listeners
        this.getServer().getPluginManager().registerEvents(new InteractListener(this), this);

        // Register commands
        this.getCommand("npcnamer").setExecutor(new CommandHandler(this));
    }

    public void smokeShower(final Entity entity) {
        final Location loc = entity.getLocation();
        final Random random = new Random();

        for (int i = 0; i < 15; i++) {
            loc.getWorld().playEffect(loc, Effect.SMOKE, random.nextInt(9));
        }
    }
}
