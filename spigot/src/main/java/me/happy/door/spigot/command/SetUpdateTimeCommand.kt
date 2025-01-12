package me.happy.door.spigot.command

import me.happy.door.commons.queue.Queue
import me.happy.door.commons.redis.JedisManager
import me.happy.door.spigot.Door
import me.happy.door.spigot.packet.QueueSetUpdateTimePacket
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.lang.NumberFormatException

class SetUpdateTimeCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("door.setupdatetime")) {
            sender.sendMessage("${ChatColor.RED}No permissions.")
            return true
        }

        if (args.size < 2) {
            sender.sendMessage("${ChatColor.RED}Invalid usage. /$label <queue> <size>")
            return true
        }

        val queue: Queue? = JavaPlugin.getPlugin(Door::class.java).queueManager.getQueueByName(args[0])

        if (queue == null) {
            sender.sendMessage("${ChatColor.RED}Queue named '${args[0]}' cannot be found.")
            return true
        }


        try {
            val size: Long = args[1].toLong()

            JedisManager.writePacket(QueueSetUpdateTimePacket(queue.name, size))
            sender.sendMessage("${ChatColor.GREEN}Set update time to $size for ${queue.name}")
        }catch (exception: NumberFormatException) {
            sender.sendMessage("${ChatColor.RED}Text '${args[1]}' cannot be parsed as a number.")
            return true
        }
        return true
    }
}