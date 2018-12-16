package ru.skidrowapi.newyearscase.events;

import org.bukkit.Material;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventHandler implements Listener {
    int id;
    private Loader plugin;
    public EventHandler(Loader intance) {
        plugin = intance;
    }

    @org.bukkit.event.EventHandler
    void onPlayerQuitEvent(PlayerQuitEvent e){
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.cancelTask(this.id);
    }


    @org.bukkit.event.EventHandler
    void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        shed(p);
    }

    void shed(Player p){
        int time=plugin.getConfig().getInt("case.time");
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        this.id=scheduler.scheduleAsyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                reward(p);
            }
        }, 20L*time, 20L*time);
    }

    private void reward(Player p){
        final Random random = new Random();
        int c = random.nextInt(5);
        if (c==0){
            c = random.nextInt(5);
        }
        chest(p,c);
    }

    private void chest(Player p , int c){
        Inventory ch=plugin.getServer().createInventory(p,3*9,plugin.pluginPrefix);
        ch.clear();
        int num_case=c,amount,lvl,amont_item;
        String enchant,material;
        amont_item=plugin.getConfig().getInt("case."+c+"."+"amount_item");
        for (int i=1; i<=amont_item;i++){
            material=plugin.getConfig().getString("case."+c+"."+i+"."+"item");
            enchant=plugin.getConfig().getString("case."+c+"."+i+"."+"enchant");
            amount=plugin.getConfig().getInt("case."+c+"."+i+"."+"amount");
            lvl=plugin.getConfig().getInt("case."+c+"."+i+"."+"lvl");
            ItemStack item=new ItemStack(Material.matchMaterial(material),amount);
            if (enchant!=null){
                if(lvl==0){lvl=1;}
                item.addEnchantment(Enchantment.getByName(enchant),lvl);
            }
            ItemMeta item_m=item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(plugin.pluginPrefix);
            item_m.setLore(lore);
            item.setItemMeta(item_m);
            ch.addItem(item);
        }
        p.openInventory(ch);
    }



}