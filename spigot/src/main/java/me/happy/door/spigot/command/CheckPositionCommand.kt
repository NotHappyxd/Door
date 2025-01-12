package me.happy.door.spigot.command

import me.happy.door.commons.queue.Queue
import me.happy.door.commons.redis.JedisManager
import me.happy.door.spigot.Door
import me.happy.door.spigot.packet.QueueSetUpdateTimePacket
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class CheckPositionCommand(private val door: Door) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("door.checkposition")) {
            sender.sendMessage("${ChatColor.RED}No permissions.")
            return true
        }

        if (args.size < 1) {
            sender.sendMessage("${ChatColor.RED}Invalid usage. /$label <player>")
            return true
        }

        val offlinePlayer = Bukkit.getOfflinePlayer(args[0])

        val queueMap: MutableMap<Queue, Int> = hashMapOf()

        door.queueManager.queues.forEach { queue ->
            run {
                var count = 1

                queue.players.forEach { player ->
                    count++
                    if (player.uuid == offlinePlayer.uniqueId) {
                        queueMap[queue] = count
                        return@run
                    }
                }
            }
        }

        if (queueMap.isEmpty()) {
            sender.sendMessage("${ChatColor.RED}${offlinePlayer.name} is not in any queue")
            return true
        }

        sender.sendMessage("${ChatColor.GREEN}Player ${offlinePlayer.name} queue position(s)")

        queueMap.forEach{ entry -> sender.sendMessage("${ChatColor.GREEN}${entry.key.name}${ChatColor.GRAY}: ${ChatColor.RED}${entry.value}")}

        return true
    }
}