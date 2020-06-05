package aquarius0715.toolbreakwarningplugin;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public final class ToolBreakWarningPlugin extends JavaPlugin {

    Map<UUID, Boolean> settings = new HashMap<UUID, Boolean>();
    Map<UUID, Boolean> stopper_stats = new HashMap<UUID, Boolean>();
    String prefix = ChatColor.BOLD + "[" + ChatColor.GREEN + "ToolBreakWarning" + ChatColor.WHITE + "" + ChatColor.BOLD + "] ";
    boolean plugin_stats = false;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new Event(this), this);
        Objects.requireNonNull(getCommand("tbw")).setExecutor(new Command(this));
        plugin_stats = true;
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        settings.clear();
        stopper_stats.clear();
        plugin_stats = false;
        // Plugin shutdown logic
    }



}