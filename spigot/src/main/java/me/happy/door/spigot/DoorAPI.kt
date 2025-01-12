package me.happy.door.spigot

import me.happy.door.commons.queue.Queue
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class DoorAPI {

    companion object {
        @JvmStatic
        fun getQueuesForPlayer(uuid: UUID) : List<Queue> {
            val queues: MutableList<Queue> = mutableListOf()

            queueLoop@for (queue in JavaPlugin.getPlugin(Door::class.java).queueManager.queues) {
                for (player in queue.players) {
                    if (player.uuid.equals(uuid)) {
                        queues.add(queue)
                        continue@queueLoop
                    }
                }
            }

            return queues;
        }

        @JvmStatic
        fun getQueueByName(name: String) : Queue? {
            return JavaPlugin.getPlugin(Door::class.java).queueManager.getQueueByName(name)
        }
    }
}