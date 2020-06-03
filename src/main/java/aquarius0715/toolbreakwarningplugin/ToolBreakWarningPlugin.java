package aquarius0715.toolbreakwarningplugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public final class ToolBreakWarningPlugin extends JavaPlugin implements Listener {

    Map<UUID, Boolean> settings = new HashMap<UUID, Boolean>();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        getCommand("tbw").setExecutor(this);
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("tbw")) {
            if (!(sender instanceof Player)) {
                Player player = (Player) sender;
                player.sendMessage("You cannot this");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage("===============ToolBreakWarningPlugin===============");
                sender.sendMessage("このプラグインはツールの耐久値があといくつなのか、");
                sender.sendMessage("壊れそうになると音で知らせてくれるプラグインです。");
                sender.sendMessage("デフォルトではonになっています。");
                sender.sendMessage("</tbw>: この説明画面を開きます。");
                sender.sendMessage("</tbw on>: このプラグインを使用します。");
                sender.sendMessage("</tbw off>: このプラグインの使用をやめます。");
                sender.sendMessage("===============ToolBreakWarningPlugin===============");
            }

            if (args.length == 1) {
                Player player = (Player) sender;

                if (args[0].equalsIgnoreCase("on")) {
                    settings.put(player.getUniqueId(), true);
                    player.sendMessage("通知機能をオンにしました。");
                } else if (args[0].equalsIgnoreCase("off")) {
                    settings.put(player.getUniqueId(), false);
                    player.sendMessage("通知機能をオフにしました。");
                } else {
                    sender.sendMessage("===============ToolBreakWarningPlugin===============");
                    sender.sendMessage("このプラグインはツールの耐久値があといくつなのか、");
                    sender.sendMessage("壊れそうになると音で知らせてくれるプラグインです。");
                    sender.sendMessage("デフォルトではonになっています。");
                    sender.sendMessage("</tbw>: この説明画面を開きます。");
                    sender.sendMessage("</tbw on>: このプラグインを使用します。");
                    sender.sendMessage("</tbw off>: このプラグインの使用をやめます。");
                    sender.sendMessage("===============ToolBreakWarningPlugin===============");
                }
            }
        }
        return false;
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        int maxDurability = event.getItem().getType().getMaxDurability();
        int nowDurability = (event.getItem().getType().getMaxDurability() - event.getItem().getDurability()) - 1;

        boolean contains = settings.containsKey(player.getUniqueId());
        if (!contains) {
            settings.put(player.getUniqueId(), true);
        }
        if (settings.get(player.getUniqueId()).equals(true)) {

            if (nowDurability <= maxDurability * 0.2 && nowDurability >= maxDurability * 0.05) {
                String message = ChatColor.GRAY + "" + ChatColor.BOLD + "(ツール名: " + event.getItem().getType() +
                        " / 最大耐久値: " + maxDurability +
                        " /" + ChatColor.YELLOW + "" + ChatColor.BOLD + "現在の耐久値: " + nowDurability + ChatColor.GRAY + ChatColor.BOLD + ")";
                TextComponent component = new TextComponent();
                component.setText(message);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);

            } else if (nowDurability <= maxDurability * 0.05) {
                String message = ChatColor.GRAY + "" + ChatColor.BOLD + "(ツール名: " + event.getItem().getType() +
                        " / 最大耐久値: " + maxDurability +
                        " /" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "現在の耐久値: " + nowDurability + ChatColor.GRAY + ChatColor.BOLD + ")";
                TextComponent component = new TextComponent();
                component.setText(message);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 8.0F);

                player.sendTitle(ChatColor.DARK_RED + "" + ChatColor.BOLD + "注意！", ChatColor.RED + "" + ChatColor.BOLD + "装備が壊れます。早急に修繕または使用をやめてください", 1, 20, 1);
            }
        }
    }
}