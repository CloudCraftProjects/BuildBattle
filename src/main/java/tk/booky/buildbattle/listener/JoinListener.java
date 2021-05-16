package tk.booky.buildbattle.listener;
// Created by booky10 in BuildBattle (19:35 16.05.21)

import com.plotsquared.bukkit.player.BukkitPlayer;
import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tk.booky.buildbattle.utils.Constants;

import java.util.Set;
import java.util.function.Consumer;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // Get the bukkit instance for the joining player
        BukkitPlayer player = BukkitUtil.getPlayer(event.getPlayer());

        // Get the plots of the joining player
        Set<Plot> plots = player.getPlots();

        // Make one instance of the consumer, which is called after teleportation,
        // so it doesn't has to be created twice
        Consumer<Boolean> teleport = success -> {
            // Check if the teleportation process was successful
            if (success) {
                // Send the player a message, that it was
                player.sendMessage(Constants.PREFIX + "§aDu wurdest erfolgreich zu deinem Plot teleportiert!");
            } else {
                // Send the player a message, that it wasn't
                player.sendMessage(Constants.PREFIX + "§cEs gab einen Fehler, während versucht wurde dich zu deinem Plot zu teleportieren!");
            }
        };

        // Check if the player already has one plot at least
        if (plots.size() > 0) {
            // Teleport the player to the first plot he owns
            plots.iterator().next().teleportPlayer(player, teleport);
        } else {
            // Get the first existing plot area
            PlotArea area = Constants.PLOTS.getPlotSquared().getFirstPlotArea();

            // Check if one exists
            if (area == null) {
                // Inform the player that no current plot area exists
                player.sendMessage(Constants.PREFIX + "§cDa noch keine Plot-Welt existiert, konnte dir kein Plot zugewiesen werden!");
            } else {
                // Get the next free plot relative to 0 0
                Plot plot = area.getNextFreePlot(player, null);

                // Check if a free plot has been found
                if (plot == null) {
                    // Inform the player that no free plot could be found
                    player.sendMessage(Constants.PREFIX + "§cEs gab einen Fehler beim zuweisen eines Plots!");
                } else {
                    // Claim the free plot
                    plot.claim(player, false, null, true);

                    // Teleport the player to his first plot
                    plot.teleportPlayer(player, teleport);
                }
            }
        }
    }
}