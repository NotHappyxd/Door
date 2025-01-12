package me.happy.door.server.packet

import me.happy.door.commons.redis.Packet
import java.util.UUID

class QueuePlayerSendPacket : Packet {
    private var playerUuid: UUID? = null
    private var serverName: String? = null

    constructor(playerUuid: UUID?, serverName: String?) {
        this.playerUuid = playerUuid
        this.serverName = serverName
    }

    constructor() {}

    override fun onReceive() {}
    override fun getIdentifier(): String {
        return "QueuePlayerSend"
    }
}