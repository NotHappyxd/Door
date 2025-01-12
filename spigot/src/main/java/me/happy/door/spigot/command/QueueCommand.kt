package me.happy.door.spigot.command

import me.happy.door.spigot.Door
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class QueueCommand(private val door: Door) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("door.queue")) {
            sender.sendMessage("${ChatColor.RED}No permissions")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("${ChatColor.RED}Invalid usage.\n" +
                    "/queue list\n" +
                    " - Lists the queues\n" +
                    "/queue data [queue]\n" +
                    " - View the data of a queue")
            return true
        }

        when (args[0].lowercase()) {
            "list" -> {
                sender.sendMessage("${ChatColor.GREEN}Queue List")
                val stringBuilder: StringBuilder = StringBuilder()
                door.queueManager.queues.forEach{queue -> stringBuilder.append("${ChatColor.GREEN}${queue.name} (${queue.players.size})\n") }

                sender.sendMessage(stringBuilder.toString())
            }

            "data" -> {
                if (args.size < 2) {
                    sender.sendMessage("${ChatColor.RED}Invalid usage. /queue data [queue]")
                    return true
                }

                val queue = door.queueManager.getQueueByName(args[1])

                if (queue == null) {
                    sender.sendMessage("${ChatColor.RED}Queue '${args[1]}' does not exist.")
                    return true
                }

                sender.sendMessage("${ChatColor.GREEN}Queue data for ${queue.name}")
                sender.sendMessage("${ChatColor.GREEN}Players (${queue.players.size}): ${queue.players.joinToString(separator = "${ChatColor.GRAY}, ${ChatColor.GREEN}")}")
                sender.sendMessage("${ChatColor.GREEN}Paused: ${if (queue.isPaused) "${ChatColor.RED}Paused" else "Not Paused"}")
                sender.sendMessage("${ChatColor.GREEN}Time between Batch: ${queue.timeBetweenBatch}")
                sender.sendMessage("${ChatColor.GREEN}Per Batch: ${queue.perBatch}")
            }
        }

        return true
    }
}