package me.happy.door.server.threads

import java.lang.Thread
import me.happy.door.commons.queue.QueuePlayer
import java.util.concurrent.TimeUnit
import me.happy.door.commons.server.DoorServer
import me.happy.door.commons.server.ServerStatus
import me.happy.door.commons.redis.JedisManager
import me.happy.door.server.Door
import me.happy.door.server.packet.QueuePlayerSendPacket
import java.lang.InterruptedException

class QueueThread(private val door: Door) : Thread() {

    @Synchronized
    override fun start() {
        super.start()
        println("Starting queue thread")
    }

    override fun run() {
        while (true) {
            try {
                for (queue in door.queueManager.queues) {
                    queue.players.removeIf { queuePlayer: QueuePlayer ->
                        queuePlayer.offline && System.currentTimeMillis() - queuePlayer.lastOnline >
                                TimeUnit.MINUTES.toMillis(5)
                    }

                    val server = queue.server
                    if (queue.isPaused || server == null || server.status != ServerStatus.ONLINE || server.onlinePlayers >= server.maxPlayers) continue
                    if (System.currentTimeMillis() - queue.lastSent <= queue.timeBetweenBatch) continue

                    for (i in 0 until queue.perBatch) {
                        val player = queue.nextAvailablePlayer ?: continue
                        JedisManager.writePacket("queue", QueuePlayerSendPacket(player.uuid, queue.name))
                    }

                    queue.lastSent = System.currentTimeMillis()
                }

                sleep(50L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

}