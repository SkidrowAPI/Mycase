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
    private Map<Player, Integer> map;


    public EventHandler(Loader intance) {
        plugin = intance;
        lore.add(plugin.pluginPrefix);
    }



    @org.bukkit.event.EventHandler
    void onPlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.cancelTask(map.get(player));
        map.remove(player);
    }


    @org.bukkit.event.EventHandler
    void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        shedul(player);
    }

    void shedul(Player player) {
        long time =20L* plugin.getConfig().getInt("case.time");
        plugin.getLogger().info(String.valueOf(plugin.getConfig().getInt("case.time")));
        plugin.getLogger().info(String.valueOf(time));
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        this.id = scheduler.scheduleAsyncRepeatingTask(plugin, () -> {
            if (map.get(player) == null) {
                map.put(player, id);
            }
            reward(player);
        }, time, time);
    }

    private void reward(Player player) {
        final Random random = new Random();
        int c = random.nextInt(5);
        if (c == 0) c++;
        chest(player, c);
    }

    private void chest(Player player, int c) {
        int amount;
        String material;
        Inventory inventory = plugin.getServer().createInventory(new CaseHolder(), 3 * 9, plugin.pluginPrefix);
        player.sendMessage(plugin.pluginPrefix+plugin.getServer().getName());   //для себя
        inventory.clear();
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
            inventory.addItem(item);
        }
        player.openInventory(inventory);
    }
}
