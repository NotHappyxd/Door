package me.happy.door.spigot.packet

import me.happy.door.commons.queue.Queue
import java.util.UUID
import org.bukkit.plugin.java.JavaPlugin
import me.happy.door.commons.queue.QueuePlayer
import me.happy.door.commons.redis.Packet
import me.happy.door.spigot.Door

class QueueAddPlayerPacket : Packet {

    private var queueName: String? = null
    private var lane: String? = null
    private var currentServer: String? = null
    private var playerUuid: UUID? = null

    @Transient
    private val door = JavaPlugin.getPlugin(Door::class.java)

    constructor(queueName: String?, lane: String?, currentServer: String?, playerUuid: UUID?) {
        this.queueName = queueName
        this.lane = lane
        this.currentServer = currentServer
        this.playerUuid = playerUuid
    }

    constructor() {}

    override fun onReceive() {
        val queue = door.queueManager.getQueueByName(queueName!!)
        var player = door.queuePlayerManager.playerMap[playerUuid]

        if (player == null)
            player = QueuePlayer(
                playerUuid!!, door.laneManager.getLaneByName(lane)!!, false,
                System.currentTimeMillis(), door.serverManager.getServerByName(currentServer!!)!!
            )

        player.lane = door.laneManager.getLaneByName(lane)!!
        player.offline = false
        player.currentServer = door.serverManager.getServerByName(currentServer!!)!!
        player.lastOnline = System.currentTimeMillis()
        player.insertedAtMap[queue!!] = System.currentTimeMillis()

        if (!inQueue(queue, playerUuid)) queue.players.add(player)

        door.queuePlayerManager.playerMap[playerUuid!!] = player
    }

    override fun getIdentifier(): String {
        return "QueueAddPlayer"
    }

    private fun inQueue(queue: Queue?, uuid: UUID?): Boolean {
        for (player in queue!!.players) {
            if (player.uuid == uuid) return true
        }

        return false
    }
}