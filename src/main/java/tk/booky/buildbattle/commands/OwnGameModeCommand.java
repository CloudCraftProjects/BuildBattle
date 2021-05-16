package tk.booky.buildbattle.commands;
// Created by booky10 in BuildBattle (19:29 16.05.21)

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.PlayerResultingCommandExecutor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class OwnGameModeCommand extends CommandAPICommand implements PlayerResultingCommandExecutor {

    public OwnGameModeCommand() {
        super("gamemode");

        withAliases("gm");
        withPermission("buildbattle.command.gamemode");
        withArguments(new StringArgument("gamemode").overrideSuggestions("adventure", "creative", "spectator", "survival"));

        executesPlayer(this);
    }

    @Override
    public int run(Player sender, Object[] args) throws WrapperCommandSyntaxException {
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

        if (sender.hasPermission("buildbattle.command.gamemode." + argument)) {
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
    }
}