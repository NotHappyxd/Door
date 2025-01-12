package me.happy.door.spigot.packet

import java.util.UUID
import org.bukkit.plugin.java.JavaPlugin
import me.happy.door.commons.redis.Packet
import me.happy.door.spigot.Door

class QueuePlayerQuitPacket : Packet {
    private var playerUuid: UUID? = null

    @Transient
    private val door = JavaPlugin.getPlugin(Door::class.java)

    constructor(playerUuid: UUID?) {
        this.playerUuid = playerUuid
    }

    constructor() {}

    override fun onReceive() {
        val player = door.queuePlayerManager.playerMap[playerUuid] ?: return

        player.offline = true
        player.lastOnline = System.currentTimeMillis()
    }

    override fun getIdentifier(): String {
        return "QueuePlayerQuit"
    }
}