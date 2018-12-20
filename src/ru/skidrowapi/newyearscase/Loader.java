package ru.skidrowapi.newyearscase;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.skidrowapi.newyearscase.events.EventHandler;

public class Loader extends JavaPlugin {
    Loader plugin=this;
    public String pluginPrefix= ChatColor.WHITE+"["+ChatColor.BLUE+"New Year's case"+ChatColor.WHITE+"] "+ChatColor.RESET;

    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(new EventHandler(this),this);
    }

    @Override
    public void onDisable(){

    }
}
