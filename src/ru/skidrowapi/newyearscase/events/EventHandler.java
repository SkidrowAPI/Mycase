package ru.skidrowapi.newyearscase.events;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import ru.skidrowapi.newyearscase.Loader;
import ru.skidrowapi.newyearscase.holder.CaseHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EventHandler implements Listener {
    int id;
    private Loader plugin;
    private List<String> lore = new ArrayList<>();


    public EventHandler(Loader intance) {
        plugin = intance;
        lore.add(plugin.pluginPrefix);
    }

    private Map<Player, Integer> map;

    @org.bukkit.event.EventHandler
    void onPlayerQuitEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.cancelTask(map.get(p));
        map.remove(p);
    }


    @org.bukkit.event.EventHandler
    void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        shed(p);
    }

    void shed(Player p) {
        int time = plugin.getConfig().getInt("case.time");
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        this.id = scheduler.scheduleAsyncRepeatingTask(plugin, () -> {
            if (map.get(p) != null) {
                map.put(p, id);
            }
            reward(p);
        }, 20L * time, 20L * time);
    }

    private void reward(Player p) {
        final Random random = new Random();
        int c = random.nextInt(5);
        if (c == 0) c++;
        chest(p, c);
    }

    private void chest(Player p, int c) {
        int amount;
        String material;
        Inventory ch = plugin.getServer().createInventory(new CaseHolder(), 3 * 9, plugin.pluginPrefix);
        p.sendMessage(plugin.pluginPrefix+plugin.getServer().getName());
        ch.clear();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("case." + c);
        for (String string : section.getKeys(false)) {
            ConfigurationSection section1 = section.getConfigurationSection(string);
            List<String> enchantments = new ArrayList();
            List<Integer> lvl = new ArrayList<>();
            material = section1.getString("item");
            enchantments = section1.getStringList("enchant");
            amount = section1.getInt("amount");
            lvl = section1.getIntegerList("lvl");
            ItemStack item = new ItemStack(Material.matchMaterial(material), amount);
            int i = 0;
            for (String en : enchantments) {
                item.addEnchantment(Enchantment.getByName(en), lvl.get(i));
                i++;
            }
            ItemMeta item_m = item.getItemMeta();
            item_m.setLore(lore);
            item.setItemMeta(item_m);
            ch.addItem(item);
        }
        p.openInventory(ch);
    }
}
