package me.happy.door.server.packet

import java.util.UUID
import me.happy.door.commons.redis.Packet

class QueuePlayerMessagePacket : Packet {
    private var playerUuid: UUID? = null
    private var queueName: String? = null
    private var position = 0
    private var maxPosition = 0
    private var eta = 0

    constructor(playerUuid: UUID?, queueName: String?, position: Int, maxPosition: Int, eta: Int) {
        this.playerUuid = playerUuid
        this.queueName = queueName
        this.position = position
        this.maxPosition = maxPosition
        this.eta = eta
    }

    constructor() {}

    override fun onReceive() {}
    override fun getIdentifier(): String {
        return "QueuePlayerMessagePacket"
    }
}