package me.happy.door.spigot.command

import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.entity.Player
import me.happy.door.commons.server.ServerStatus
import me.happy.door.commons.lane.Lane
import me.happy.door.commons.redis.JedisManager
import me.happy.door.spigot.Door
import me.happy.door.spigot.packet.QueueAddPlayerPacket
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import java.util.Comparator

class JoinQueueCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage(ChatColor.RED.toString() + "/joinqueue <queue>")
            return true
        }

        val queue = JavaPlugin.getPlugin(Door::class.java).queueManager.getQueueByName(args[0])
        val bukkitPlayer = sender as Player

        if (queue == null) {
            bukkitPlayer.sendMessage(ChatColor.RED.toString() + "That queue does not exist or is offline.")
            return true
        }

        for (player in queue.players) {
            if (player.uuid == bukkitPlayer.uniqueId) {
                sender.sendMessage(ChatColor.RED.toString() + "You are already in that queue!")
                return true
            }
        }

        if (queue.isPaused || queue.server == null || queue.server!!.status != ServerStatus.ONLINE) {
            bukkitPlayer.sendMessage(ChatColor.RED.toString() + "That queue does not exist or is offline.")
            return true
        }

        val lane = JavaPlugin.getPlugin(Door::class.java).laneManager.lanes.stream()
            .sorted(Comparator.comparingInt { obj: Lane -> obj.priority }.reversed())
            .filter { lane1: Lane -> bukkitPlayer.hasPermission(lane1.permission) }
            .findFirst()
            .orElse(null)

        JedisManager.writePacket(QueueAddPlayerPacket(queue.name, lane.name, JavaPlugin.getPlugin(Door::class.java).config.getString("server-name"),
                bukkitPlayer.uniqueId))

        bukkitPlayer.sendMessage(ChatColor.GREEN.toString() + "You have joined the queue.")
        return true
    }
}