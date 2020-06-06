package aquarius0715.toolbreakwarningplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.Objects;

public class ScoreBoard {

    ToolBreakWarningPlugin plugin;

    public ScoreBoard(ToolBreakWarningPlugin plugin) {
        this.plugin = plugin;
    }

    public void createScoreBoard(Player player) {
        plugin.scoreboardManager = Bukkit.getScoreboardManager();
        plugin.scoreboard = Objects.requireNonNull(plugin.scoreboardManager).getNewScoreboard();
        plugin.objective = plugin.scoreboard.registerNewObjective(ChatColor.BOLD + "Man10Server", "Dummy");
        plugin.objective.setDisplayName(ChatColor.BOLD + "Man10ServerNetWork" + ChatColor.GREEN + "" + ChatColor.BOLD + " : ResourceServer");
        plugin.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        plugin.playerName = plugin.objective.getScore(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + player.getDisplayName() + ChatColor.WHITE + "" + ChatColor.BOLD + "さんこんにちは！");
        plugin.playerName.setScore(14);
        plugin.rank = plugin.objective.getScore(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "あなたのランクは" + ChatColor.YELLOW + "" + ChatColor.BOLD + "Miner" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "です");

        player.setScoreboard(plugin.scoreboard);
    }
}
