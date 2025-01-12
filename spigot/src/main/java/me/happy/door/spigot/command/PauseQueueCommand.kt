package me.happy.door.spigot.command

import me.happy.door.commons.redis.JedisManager
import me.happy.door.spigot.Door
import me.happy.door.spigot.packet.QueuePausePacket
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class PauseQueueCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("door.pausequeue")) {
            sender.sendMessage("${ChatColor.RED}No permissions.")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("${ChatColor.RED}Invalid usage. /pausequeue <queue>")
            return true
        }

        val queue = JavaPlugin.getPlugin(Door::class.java).queueManager.getQueueByName(args[0])

        if (queue == null) {
            sender.sendMessage("${ChatColor.RED}Queue does not exist")
            return true
        }

        queue.isPaused = !queue.isPaused
        JedisManager.writePacket(QueuePausePacket(queue.name, queue.isPaused))

        if (queue.isPaused) {
            sender.sendMessage("${ChatColor.GREEN}Queue '${queue.name}' is now paused")
            return true
        }

        sender.sendMessage("${ChatColor.GREEN}Queue '${queue.name}' is no longer paused")
        return true
    }
}