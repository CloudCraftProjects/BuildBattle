package tk.booky.buildbattle;

import dev.jorel.commandapi.CommandAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import tk.booky.buildbattle.commands.OtherGameModeCommand;
import tk.booky.buildbattle.commands.OwnGameModeCommand;
import tk.booky.buildbattle.commands.RateCommand;
import tk.booky.buildbattle.commands.TopCommand;
import tk.booky.buildbattle.listener.JoinListener;

public final class BuildBattleMain extends JavaPlugin {

    @Override
    public void onLoad() {
        // Unregister vanilla gamemode command for own implementation
        CommandAPI.unregister("gamemode", true);

        // Register custom gamemode command, for alias and more permission checking
        new OwnGameModeCommand().register();
        new OtherGameModeCommand().register();

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