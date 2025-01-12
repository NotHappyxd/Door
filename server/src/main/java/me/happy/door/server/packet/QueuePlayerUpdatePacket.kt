package me.happy.door.server.packet

import me.happy.door.commons.queue.Queue
import java.util.UUID
import me.happy.door.commons.queue.QueuePlayer
import me.happy.door.commons.redis.Packet
import me.happy.door.server.Door

class QueuePlayerUpdatePacket : Packet {
    private var queueName: String? = null
    private var lane: String? = null
    private var currentServer: String? = null
    private var lastOnline: Long = 0
    private var playerUuid: UUID? = null
    private var queueInsertion: Map<String, Long>? = null

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
        val queue = Door.INSTANCE.queueManager.getQueueByName(queueName!!)
        var player = Door.INSTANCE.queuePlayerManager.playerMap[playerUuid]
        if (player == null) player = QueuePlayer(
            playerUuid!!, Door.INSTANCE.laneManager.getLaneByName(lane!!)!!, false,
            System.currentTimeMillis(), Door.INSTANCE.serverManager.getServerByName(currentServer!!)!!
        )

        player.lane = Door.INSTANCE.laneManager.getLaneByName(lane!!)!!
        player.offline = false
        player.currentServer = Door.INSTANCE.serverManager.getServerByName(currentServer!!)!!
        player.lastOnline = lastOnline
        player.insertedAtMap.clear()

        for ((key, value) in queueInsertion!!)
            player.insertedAtMap[Door.INSTANCE.queueManager.getQueueByName(key)!!] = value

        if (!inQueue(queue!!, playerUuid)) queue.players.add(player)
        Door.INSTANCE.queuePlayerManager.playerMap[playerUuid!!] = player
    }

    override fun getIdentifier(): String {
        return "QueuePlayerUpdate"
    }

    private fun inQueue(queue: Queue, uuid: UUID?): Boolean {
        for (player in queue.players) {
            if (player.uuid == uuid) return true
        }
        return false
    }
}