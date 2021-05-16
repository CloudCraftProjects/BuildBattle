package tk.booky.buildbattle.commands;
// Created by booky10 in BuildBattle (19:23 16.05.21)

import com.plotsquared.bukkit.player.BukkitPlayer;
import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.plot.Plot;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RateCommand extends CommandAPICommand implements PlayerCommandExecutor {

    private final Plugin plugin;

    public RateCommand(Plugin plugin) {
        super("rate");
        this.plugin = plugin;

        withPermission("buildbattle.command.rate");
        withArguments(new IntegerArgument("rating", 0, 10));

        executesPlayer(this);
    }

    @Override
    public void run(Player sender, Object[] args) throws WrapperCommandSyntaxException {
        int rating = (int) args[0];

        BukkitPlayer player = BukkitUtil.getPlayer(sender);
        Plot plot = player.getCurrentPlot();

        if (plot == null) {
            CommandAPI.fail("You are not standing on a plot currently!");
        } else {
            plugin.getConfig().set("rating." + plot.getId() + "." + player.getUUID(), rating);
            plugin.saveConfig();

            sender.sendMessage("Das Rating für Plot " + plot.getId() + " wurde auf " + rating + " geändert!");
        }
    }
}