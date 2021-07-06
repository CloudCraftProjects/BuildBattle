package tk.booky.buildbattle;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tk.booky.buildbattle.commands.RateCommand;
import tk.booky.buildbattle.commands.TopCommand;
import tk.booky.buildbattle.listener.JoinListener;

public final class BuildBattleMain extends JavaPlugin {

    public static BuildBattleMain main;

    @Override
    public void onLoad() {
        // Set plugin instance
        main = this;

        // Register commands for the rating system
        new RateCommand(this).register();
        new TopCommand(this).register();
    }

    @Override
    public void onEnable() {
        // Register join handling
        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
    }
}
