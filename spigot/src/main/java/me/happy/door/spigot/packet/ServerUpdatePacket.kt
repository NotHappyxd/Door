package me.happy.door.spigot.packet

import me.happy.door.commons.queue.Queue
import me.happy.door.commons.redis.Packet
import me.happy.door.commons.server.ServerStatus
import org.bukkit.plugin.java.JavaPlugin
import me.happy.door.commons.server.DoorServer
import me.happy.door.spigot.Door
import java.util.concurrent.TimeUnit

class ServerUpdatePacket : Packet {
    private var name: String? = null
    private var onlinePlayers = 0
    private var maxPlayers = 0
    private var maxQueueSize = 0
    private var status: ServerStatus? = null

    @Transient
    private val door = JavaPlugin.getPlugin(Door::class.java)

    constructor() {}
    constructor(name: String?, onlinePlayers: Int, maxPlayers: Int, maxQueueSize: Int, status: ServerStatus?) {
        this.name = name
        this.onlinePlayers = onlinePlayers
        this.maxPlayers = maxPlayers
        this.maxQueueSize = maxQueueSize
        this.status = status
    }

    override fun onReceive() {
        var server = JavaPlugin.getPlugin(Door::class.java).serverManager.getServerByName(name!!)
        if (server == null) {
            server = DoorServer()
            server.name = name!!
        }
        server.onlinePlayers = onlinePlayers
        server.maxPlayers = maxPlayers
        server.maxQueueSize = maxQueueSize
        server.status = status
        server.lastUpdated = System.currentTimeMillis()

        var queue = door.queueManager.getQueueByName(server.name)
        val add = queue == null
        if (queue == null) {
            queue = Queue(name!!)
            println("Creating queue")
        }

        queue.lastUpdate = System.currentTimeMillis()
        queue.server = server
        queue.isPaused = false
        if (add) door.queueManager.queues.add(queue)

        door.queueManager.queues.removeIf { queue1: Queue ->
            queue1.server!!.status == ServerStatus.OFFLINE &&
                    System.currentTimeMillis() - queue1.server!!.lastUpdated > TimeUnit.SECONDS.toMillis(15)
        }
    }

    override fun getIdentifier(): String {
        return "ServerUpdatePacket"
    }
}