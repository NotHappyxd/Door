package me.happy.door.server.packet

import java.util.UUID
import me.happy.door.commons.queue.QueuePlayer
import java.util.HashMap
import me.happy.door.commons.redis.JedisManager
import me.happy.door.commons.redis.Packet
import me.happy.door.server.Door

class QueueAddPlayerPacket : Packet {
    private var queueName: String? = null
    private var lane: String? = null
    private var currentServer: String? = null
    private var playerUuid: UUID? = null

    constructor(queueName: String?, lane: String?, currentServer: String?, playerUuid: UUID?) {
        this.queueName = queueName
        this.lane = lane
        this.currentServer = currentServer
        this.playerUuid = playerUuid
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
        player.lastOnline = System.currentTimeMillis()
        player.insertedAtMap[queue!!] = System.currentTimeMillis()

        queue.players.add(player)

        Door.INSTANCE.queuePlayerManager.playerMap[playerUuid!!] = player

        val stringLongMap: MutableMap<String, Long> = HashMap()

        for ((key, value) in player.insertedAtMap) stringLongMap[key.name] = value
        JedisManager.writePacket(
            QueuePlayerUpdatePacket(
                queueName, player.lane.name, player.currentServer!!.name,
                player.lastOnline, playerUuid, stringLongMap
            )
        )
    }

    override fun getIdentifier(): String {
        return "QueueAddPlayer"
    }
}