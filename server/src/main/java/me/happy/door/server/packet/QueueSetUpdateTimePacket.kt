package me.happy.door.server.packet

import me.happy.door.commons.queue.Queue
import me.happy.door.commons.redis.Packet
import me.happy.door.server.Door

class QueueSetUpdateTimePacket : Packet {

    var queueName: String? = null
    var size: Long? = -1

    constructor(queueName: String, size: Long) {
        this.queueName = queueName
        this.size = size
    }

    constructor()

    override fun onReceive() {
        val queue: Queue? = Door.INSTANCE.queueManager.getQueueByName(queueName!!)

        if (queue == null) {
            println("Cannot find queue by name $queueName while trying to set size")
            return
        }

        queue.timeBetweenBatch = size!!
        println("Changed time between batch on $queueName to ${queue.timeBetweenBatch}")
    }

    override fun getIdentifier(): String {
        return "QueueSetUpdateTimePacket"
    }
}