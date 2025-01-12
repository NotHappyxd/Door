package me.happy.door.server.packet

import me.happy.door.commons.queue.Queue
import me.happy.door.commons.redis.Packet
import me.happy.door.commons.server.ServerStatus
import me.happy.door.commons.server.DoorServer
import me.happy.door.server.Door
import java.util.concurrent.TimeUnit

class ServerUpdatePacket : Packet {
    private var name: String? = null
    private var onlinePlayers = 0
    private var maxPlayers = 0
    private var maxQueueSize = 0
    private var status: ServerStatus? = null

    constructor(name: String?, onlinePlayers: Int, maxPlayers: Int, maxQueueSize: Int, status: ServerStatus?) {
        this.name = name
        this.onlinePlayers = onlinePlayers
        this.maxPlayers = maxPlayers
        this.maxQueueSize = maxQueueSize
        this.status = status
    }

    constructor() {}

    override fun onReceive() {
        var server = Door.INSTANCE.serverManager.getServerByName(name!!)
        var add = server == null

        if (server == null) {
            server = DoorServer()
            server.name = name!!
            println("Creating new server object")
        }

        server.onlinePlayers = onlinePlayers
        server.maxPlayers = maxPlayers
        server.maxQueueSize = maxQueueSize
        server.status = status
        server.lastUpdated = System.currentTimeMillis()

        if (add) Door.INSTANCE.serverManager.servers.add(server)

        var queue = Door.INSTANCE.queueManager.getQueueByName(server.name)
        add = queue == null

        if (queue == null) {
            queue = Queue(name!!)
            println("Creating queue")
        }

        queue.lastUpdate = System.currentTimeMillis()
        queue.server = server
        queue.isPaused = false

        if (add) Door.INSTANCE.queueManager.queues.add(queue)

        Door.INSTANCE.queueManager.queues.removeIf { queue1: Queue ->
            queue1.server!!.status == ServerStatus.OFFLINE &&
                    System.currentTimeMillis() - queue1.server!!.lastUpdated > TimeUnit.SECONDS.toMillis(15)
        }
    }

    override fun getIdentifier(): String {
        return "ServerUpdatePacket"
    }
}