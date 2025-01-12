package me.happy.door.server.packet

import me.happy.door.commons.redis.Packet
import me.happy.door.server.Door

class QueuePausePacket : Packet {

    private var name: String? = null
    private var paused: Boolean? = false

    constructor() {}

    constructor(name: String, paused: Boolean) {
        this.name = name
        this.paused = paused
    }

    override fun onReceive() {
        val queue = Door.INSTANCE.queueManager.getQueueByName(name!!) ?: return

        queue.isPaused = paused!!

        println("Queue $name pause status is set to $paused")

    }

    override fun getIdentifier(): String {
        return "QueuePausePacket"
    }
}