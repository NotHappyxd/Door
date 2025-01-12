package me.happy.door.server.threads

import java.lang.Thread
import lombok.SneakyThrows
import me.happy.door.commons.redis.JedisManager
import me.happy.door.server.Door
import me.happy.door.server.packet.QueuePlayerMessagePacket

class BroadcastThread : Thread() {

    @SneakyThrows
    override fun run() {
        while (true) {
            for (queue in Door.INSTANCE.queueManager.queues) {
                var count = 1

                for (player in queue.players) {
                    JedisManager.writePacket("queue", QueuePlayerMessagePacket(player.uuid, queue.name, count++, queue.players.size, count * (queue.timeBetweenBatch / queue.perBatch).toInt()))
                }
            }
            sleep(10000)
        }
    }
}