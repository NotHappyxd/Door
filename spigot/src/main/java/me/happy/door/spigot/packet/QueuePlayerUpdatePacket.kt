package me.happy.door.spigot.packet

import java.util.UUID
import org.bukkit.plugin.java.JavaPlugin
import me.happy.door.commons.queue.QueuePlayer
import me.happy.door.commons.redis.Packet
import me.happy.door.spigot.Door

class QueuePlayerUpdatePacket : Packet {
    private var queueName: String? = null
    private var lane: String? = null
    private var currentServer: String? = null
    private var lastOnline: Long = 0
    private var playerUuid: UUID? = null
    private var queueInsertion: Map<String, Long>? = null

    @Transient
    private val door = JavaPlugin.getPlugin(Door::class.java)

    constructor(
        queueName: String?,
        lane: String?,
        currentServer: String?,
        lastOnline: Long,
        playerUuid: UUID?,
        queueInsertion: Map<String, Long>?
    ) {
        this.queueName = queueName
        this.lane = lane
        this.currentServer = currentServer
        this.lastOnline = lastOnline
        this.playerUuid = playerUuid
        this.queueInsertion = queueInsertion
    }

    constructor() {}

    override fun onReceive() {
        val queue = door.queueManager.getQueueByName(queueName!!)
        var player = door.queuePlayerManager.playerMap[playerUuid]

        if (player == null)
            player = QueuePlayer(playerUuid!!, door.laneManager.getLaneByName(lane)!!, false,
            System.currentTimeMillis(), door.serverManager.getServerByName(currentServer!!)!!
        )

        player.lane = door.laneManager.getLaneByName(lane)!!
        player.offline = false
        player.currentServer = door.serverManager.getServerByName(currentServer!!)!!
        player.lastOnline = lastOnline
        player.insertedAtMap.clear()
        for ((key, value) in queueInsertion!!) player.insertedAtMap[door.queueManager.getQueueByName(key)!!] = value
        queue!!.players.add(player)
        door.queuePlayerManager.playerMap[playerUuid!!] = player
    }

    override fun getIdentifier(): String {
        return "QueuePlayerUpdate"
    }
}