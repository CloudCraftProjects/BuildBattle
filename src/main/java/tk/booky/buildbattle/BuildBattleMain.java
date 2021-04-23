package tk.booky.buildbattle;

import com.plotsquared.bukkit.player.BukkitPlayer;
import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.api.PlotAPI;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public final class BuildBattleMain extends JavaPlugin implements Listener {

    private static final PlotAPI api = new PlotAPI();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        BukkitPlayer player = BukkitUtil.getPlayer(event.getPlayer());
        Set<Plot> plots = player.getPlots();

        if (plots.size() > 0) {
            plots.iterator().next().teleportPlayer(player, (result) -> {});
        } else {
            PlotArea area = api.getPlotSquared().getFirstPlotArea();
            if (area == null) return;
            Plot plot = area.getNextFreePlot(player, null);
            if (plot == null) return;

            plot.teleportPlayer(player, (result) -> {});
            plot.claim(player, false, null, true);
        }
    }
}