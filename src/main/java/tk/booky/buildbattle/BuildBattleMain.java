package tk.booky.buildbattle;

import com.plotsquared.bukkit.player.BukkitPlayer;
import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.api.PlotAPI;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class BuildBattleMain extends JavaPlugin implements Listener {

    private static final PlotAPI api = new PlotAPI();

    @Override
    @SuppressWarnings("unchecked")
    public void onLoad() {
        CommandAPI.unregister("gamemode", true);

        new CommandAPICommand("gamemode")
                .withPermission("minecraft.command.gamemode")
                .withArguments(new StringArgument("gamemode").overrideSuggestions("adventure", "creative", "spectator", "survival"))
                .executesPlayer((sender, args) -> {
                    String argument = ((String) args[0]).toLowerCase();
                    GameMode mode;

                    switch (argument) {
                        case "adventure":
                            mode = GameMode.ADVENTURE;
                            break;
                        case "creative":
                            mode = GameMode.CREATIVE;
                            break;
                        case "spectator":
                            mode = GameMode.SPECTATOR;
                            break;
                        case "survival":
                            mode = GameMode.SURVIVAL;
                            break;
                        default:
                            CommandAPI.fail("Unknown gamemode!");
                            return 0;
                    }

                    if (sender.hasPermission("minecraft.command.gamemode." + argument)) {
                        if (sender.getGameMode().equals(mode)) {
                            return 0;
                        } else {
                            sender.setGameMode(mode);
                            sender.sendMessage("Set own game mode to " + StringUtils.capitalize(argument) + " Mode");
                            return 1;
                        }
                    } else {
                        CommandAPI.fail("Dazu hast du keine Rechte!");
                        return -1;
                    }
                })
                .register();

        new CommandAPICommand("gamemode")
                .withPermission("minecraft.command.gamemode.other")
                .withArguments(
                        new StringArgument("gamemode").overrideSuggestions("adventure", "creative", "spectator", "survival"),
                        new EntitySelectorArgument("target", EntitySelectorArgument.EntitySelector.MANY_PLAYERS)
                )
                .executes((sender, args) -> {
                    String argument = ((String) args[0]).toLowerCase();
                    List<Player> players = new ArrayList<>((Collection<Player>) args[1]);
                    GameMode mode;

                    switch (argument) {
                        case "adventure":
                            mode = GameMode.ADVENTURE;
                            break;
                        case "creative":
                            mode = GameMode.CREATIVE;
                            break;
                        case "spectator":
                            mode = GameMode.SPECTATOR;
                            break;
                        case "survival":
                            mode = GameMode.SURVIVAL;
                            break;
                        default:
                            CommandAPI.fail("Unknown gamemode!");
                            return -1;
                    }

                    if (sender.hasPermission("minecraft.command.gamemode." + argument)) {
                        players.removeIf(player -> player.getGameMode().equals(mode));

                        argument = StringUtils.capitalize(argument);
                        UUID uuid = sender instanceof Entity ? ((Entity) sender).getUniqueId() : null;

                        for (Player player : players) {
                            player.setGameMode(mode);
                            sender.sendMessage("Set " + (player.getUniqueId().equals(uuid) ? "own" : player.getName() + "'s") + " game mode to " + argument + " Mode");
                        }

                        return players.size();
                    } else {
                        CommandAPI.fail("Dazu hast du keine Rechte!");
                        return -1;
                    }
                })
                .register();
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        BukkitPlayer player = BukkitUtil.getPlayer(event.getPlayer());
        Set<Plot> plots = player.getPlots();

        if (plots.size() > 0) {
            plots.iterator().next().teleportPlayer(player, (result) -> {
            });
        } else {
            PlotArea area = api.getPlotSquared().getFirstPlotArea();
            if (area == null) return;
            Plot plot = area.getNextFreePlot(player, null);
            if (plot == null) return;

            plot.teleportPlayer(player, (result) -> {
            });
            plot.claim(player, false, null, true);
        }
    }
}