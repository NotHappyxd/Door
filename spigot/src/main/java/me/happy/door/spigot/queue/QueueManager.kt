package me.happy.door.spigot.queue

import me.happy.door.commons.queue.Queue
import java.util.UUID
import me.happy.door.commons.queue.QueuePlayer
import java.util.ArrayList

class QueueManager {
    val queues: MutableList<Queue> = ArrayList()

    fun getQueueForPlayer(uuid: UUID): List<Queue?> {
        val toReturn: MutableList<Queue?> = ArrayList()
        outerLoop@ for (queue in queues) {
            for (player in queue.players) {
                if (player.uuid == uuid) {
                    toReturn.add(queue)
                    continue@outerLoop
                }
            }
        }
        return toReturn
    }

    fun getQueuePlayer(uuid: UUID): List<QueuePlayer> {
        val toReturn: MutableList<QueuePlayer> = ArrayList()
        outerLoop@ for (queue in queues) {
            for (player in queue.players) {
                if (player.uuid == uuid) {
                    toReturn.add(player)
                    continue@outerLoop
                }
            }
        }
        return toReturn
    }

    fun getQueueByServerName(name: String): Queue? {
        return queues.stream()
            .filter{queue -> queue.name.equals(name, true)}
            .findFirst()
            .orElse(null)
    }

    fun getQueueByName(name: String): Queue? {
        return queues.stream()
            .filter { queue: Queue? -> queue!!.name.equals(name, ignoreCase = true) }
            .findFirst()
            .orElse(null)
    }
}