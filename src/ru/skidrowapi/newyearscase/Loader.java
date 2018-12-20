package ru.skidrowapi.newyearscase;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.skidrowapi.newyearscase.events.EventHandler;

import java.util.Map;

public class Loader extends JavaPlugin {
    private Map<Player, Integer> map;
    Loader plugin=this;
    public String pluginPrefix= ChatColor.WHITE+"["+ChatColor.BLUE+"New Year's case"+ChatColor.WHITE+"] "+ChatColor.RESET;

    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(new EventHandler(this),this);
    }

    @Override
    public void onDisable(){

    }

    public Map<Player, Integer> mapPlayerTask(){
        return map;
    }

    public void setPlayerTask(Player player, int id){
        map.put(player, id);
    }

    public void removePlayerTask(Player player){
        map.remove(player);
    }
}
