package tk.booky.buildbattle.commands;
// Created by booky10 in BuildBattle (19:29 16.05.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.ResultingCommandExecutor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class OtherGameModeCommand extends CommandAPICommand implements ResultingCommandExecutor {

    public OtherGameModeCommand() {
        super("gamemode");

        withAliases("gm");
        withPermission("buildbattle.command.gamemode.other");
        withArguments(new StringArgument("gamemode").overrideSuggestions("adventure", "creative", "spectator", "survival"));
        withArguments(new EntitySelectorArgument("target", EntitySelectorArgument.EntitySelector.MANY_PLAYERS));

        executes(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public int run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException {
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

        if (sender.hasPermission("buildbattle.command.gamemode." + argument)) {
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
    }
}