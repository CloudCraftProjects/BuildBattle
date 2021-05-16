package tk.booky.buildbattle.commands;
// Created by booky10 in BuildBattle (19:23 16.05.21)

import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import com.plotsquared.core.plot.PlotId;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import tk.booky.buildbattle.utils.Constants;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TopCommand extends CommandAPICommand implements CommandExecutor {

    private final Plugin plugin;

    public TopCommand(Plugin plugin) {
        super("top");
        this.plugin = plugin;

        withPermission("buildbattle.command.top");
        executes(this);
    }

    @Override
    public void run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("rating");

        if (section != null) {
            Set<String> keys = section.getKeys(false);
            Map<String, Integer> ratings = new HashMap<>();

            if (keys.size() > 0) {
                for (String plot : keys) {
                    ConfigurationSection plotSection = section.getConfigurationSection(plot);
                    int rating = 0;

                    if (plotSection == null) continue;
                    Set<String> uuids = plotSection.getKeys(false);

                    for (String uuid : uuids) rating += plotSection.getInt(uuid);
                    ratings.put(plot, rating);
                }

                List<Map.Entry<String, Integer>> entries = new ArrayList<>(ratings.entrySet());

                entries.sort(Map.Entry.comparingByValue());
                Collections.reverse(entries);

                AtomicInteger count = new AtomicInteger(0);
                sender.sendMessage("Top 10 Rated:");

                PlotArea area = Constants.PLOTS.getPlotSquared().getFirstPlotArea();
                entries.stream().limit(10).map(entry -> {
                    PlotId id = PlotId.fromStringOrNull(entry.getKey());
                    if (id == null) {
                        return count.incrementAndGet() + ". " + entry.getKey() + " (UNKNOWN): " + entry.getValue();
                    } else {
                        Plot plot = Constants.PLOTS.getPlotSquared().getPlot(area, id);
                        String owners = Stream.concat(plot.getOwners().stream(), plot.getTrusted().stream()).map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.joining(", "));
                        return count.incrementAndGet() + ". " + plot.getId() + " (" + owners + "): " + entry.getValue();
                    }
                }).forEach(sender::sendMessage);
            } else {
                CommandAPI.fail("No ratings have been set yet!");
            }
        } else {
            CommandAPI.fail("No ratings have been set yet!");
        }
    }
}