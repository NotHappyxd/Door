package me.happy.door.server

import me.happy.door.commons.redis.JedisManager
import me.happy.door.commons.redis.Packet
import me.happy.door.server.lane.LaneManager
import me.happy.door.server.packet.*
import me.happy.door.server.player.QueuePlayerManager
import me.happy.door.server.queue.QueueManager
import me.happy.door.server.server.ServerManager
import me.happy.door.server.threads.QueueThread
import me.happy.door.server.threads.BroadcastThread

class Door {
    val queueManager: QueueManager
    val serverManager: ServerManager
    val laneManager: LaneManager
    val queuePlayerManager: QueuePlayerManager
    val config: DoorConfig

    init {
        INSTANCE = this
        config = DoorConfig()

        JedisManager.connect(
            config.getString("redis.host"),
            config.getInt("redis.port"),
            config.getString("redis.password")
        )

        listOf(
            QueueAddPlayerPacket(), QueuePlayerUpdatePacket(), QueuePlayerJoinPacket(), QueuePlayerMessagePacket(),
            QueuePlayerQuitPacket(), QueuePlayerSendPacket(), ServerUpdatePacket(), LaneUpdatePacket(), QueueSetUpdateTimePacket(),
            QueuePausePacket()
        ).forEach{ packet: Packet? -> JedisManager.registerPacket(packet) }

        serverManager = ServerManager()
        queuePlayerManager = QueuePlayerManager()
        laneManager = LaneManager()
        queueManager = QueueManager()

        QueueThread(this).start()
        BroadcastThread().start()
    }

    companion object {
        @JvmStatic
        lateinit var INSTANCE: Door
    }
}