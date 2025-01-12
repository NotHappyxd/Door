package me.happy.door.server.packet

import java.util.UUID
import me.happy.door.commons.redis.Packet
import me.happy.door.server.Door

class QueuePlayerJoinPacket : Packet {
    private var playerUuid: UUID? = null

    constructor(playerUuid: UUID?) {
        this.playerUuid = playerUuid
    }

    constructor() {}

    override fun onReceive() {
        val player = Door.INSTANCE.queuePlayerManager.playerMap[playerUuid] ?: return
        player.offline = false
        player.lastOnline = System.currentTimeMillis()
    }

    override fun getIdentifier(): String {
        return "QueuePlayerJoin"
    }
}