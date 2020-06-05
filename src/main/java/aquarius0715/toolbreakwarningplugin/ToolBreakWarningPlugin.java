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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public final class ToolBreakWarningPlugin extends JavaPlugin implements Listener {

    Map<UUID, Boolean> settings = new HashMap<UUID, Boolean>();
    Map<UUID, Boolean> stopper_stats = new HashMap<UUID, Boolean>();
    String prefix = ChatColor.BOLD + "[" + ChatColor.GREEN + "ToolBreakWarning" + ChatColor.WHITE + "" + ChatColor.BOLD + "] ";
    boolean plugin_stats = false;
    boolean toggle = true;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(getCommand("tbw")).setExecutor(this);
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("tbw")) {
            if (!(sender instanceof Player)) {
                Player player = (Player) sender;
                player.sendMessage("You cannot this");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(prefix + "===============ToolBreakWarningPlugin===============");
                sender.sendMessage(prefix + "このプラグインはツールの耐久値があといくつなのか、");
                sender.sendMessage(prefix + "壊れそうになると音で知らせてくれるプラグインです。");
                sender.sendMessage(prefix + "デフォルトではonになっています。");
                sender.sendMessage(prefix + "</tbw>: この説明画面を開きます。");
                sender.sendMessage(prefix + "</tb stopper>: ストッパー機能の有効・無効を指定します。");
                sender.sendMessage(prefix + "</tbw on>: このプラグインを使用します。");
                sender.sendMessage(prefix + "</tbw off>: このプラグインの使用をやめます。");
                if (sender.hasPermission("admin")) {
                    sender.sendMessage(prefix + "</tbw enable>: このプラグインを有効化にします。");
                    sender.sendMessage(prefix + "</tbw disable>: このプラグインを無効化します。");
                    sender.sendMessage(prefix + "</tbw reload>: プラグインをリロードします。 ");
                }
                sender.sendMessage(prefix + "===============ToolBreakWarningPlugin===============");
            }

            if (args.length == 1) {
                Player player = (Player) sender;

                if (args[0].equalsIgnoreCase("on")) {
                    if (!plugin_stats) {
                        sender.sendMessage(prefix + "プラグインは停止しています。");
                        return false;
                    }
                    settings.put(player.getUniqueId(), true);
                    player.sendMessage(prefix + ChatColor.GREEN + "" + ChatColor.BOLD + "通知機能をオンにしました。");
                    return true;
                }
                if (args[0].equalsIgnoreCase("off")) {
                    if (!plugin_stats) {
                        sender.sendMessage(prefix + "プラグインは停止しています。");
                        return false;
                    }
                    settings.put(player.getUniqueId(), false);
                    player.sendMessage(prefix + ChatColor.RED + "" + ChatColor.BOLD + "通知機能をオフにしました。");
                }
                if (args[0].equalsIgnoreCase("disable")) {
                    if (!player.hasPermission("admin")) {
                        sender.sendMessage(prefix + "あなたはこのコマンドを使うことができません。");
                        return false;
                    }
                    if (!plugin_stats) {
                        sender.sendMessage(prefix + "プラグインはすでに無効にされています。");
                        return false;
                    } else {
                        onDisable();
                        sender.sendMessage(prefix + "プラグインを無効にしました。");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("enable")) {
                    if (plugin_stats && !player.hasPermission("admin")) {
                        sender.sendMessage(prefix + "あなたはこのコマンドを使うことができません。");
                        return false;
                    }
                    if (plugin_stats) {
                        sender.sendMessage(prefix + "プラグインはすでに有効にされています。");
                        return true;
                    }
                }
                if (!plugin_stats) {
                    onEnable();
                    sender.sendMessage(prefix + "プラグインを有効にしました。");
                    return true;
                }

                if (args[0].equalsIgnoreCase("stopper")) {
                    if (this.stopper_stats.containsValue(true)) {
                        sender.sendMessage(prefix + "ストッパー機能をオフにしました。");
                        this.stopper_stats.put(((Player) sender).getUniqueId(), false);
                    } else {
                        sender.sendMessage(prefix + "ストッパー機能をオンにしました。");
                        this.stopper_stats.put(((Player) sender).getUniqueId(), true);
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("reload")) {
                    if (!sender.hasPermission("admin")) {
                        sender.sendMessage(prefix + "あなたはこのコマンドを使うことができません。");
                        return false;
                    } else {
                        onDisable();
                        onEnable();
                        reloadConfig();
                        sender.sendMessage(prefix + "リロードしました。");
                    }
                    return true;
                }
            }
            return false;
        }
        return false;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) {
        Player player = event.getPlayer();
        int maxDurability = event.getItem().getType().getMaxDurability();
        int nowDurability = (event.getItem().getType().getMaxDurability() - event.getItem().getDurability()) - 1;

        if (!plugin_stats) {
            event.isCancelled();
            return;
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
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        boolean contains = stopper_stats.containsKey(player.getUniqueId());

        if (!plugin_stats || !contains) {
            event.isCancelled();
            return;
        }


        if (stopper_stats.get(player.getUniqueId()).equals(true)) {
            int nowDurability = (player.getItemInHand().getType().getMaxDurability() - player.getItemInHand().getDurability()) - 1;
            String message = ChatColor.RED + "" + ChatColor.BOLD + "ストッパーが作動しました！！！";
            TextComponent component = new TextComponent();
            component.setText(message);

            if (nowDurability == 0) {
                event.setCancelled(true);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin_stats) {
            player.sendMessage(prefix + "ToolBreakWarning は有効化されています。詳細は「/tbw」と入力して確認して下さい。");
        }
        settings.putIfAbsent(player.getUniqueId(), true);
        stopper_stats.putIfAbsent(player.getUniqueId(), true);
    }
}