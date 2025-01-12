package me.happy.door.spigot

import org.bukkit.plugin.java.JavaPlugin
import java.util.HashSet
import me.happy.door.commons.redis.JedisManager
import me.happy.door.commons.lane.Lane
import me.happy.door.commons.redis.Packet
import org.bukkit.Bukkit
import me.happy.door.commons.server.ServerStatus
import me.happy.door.spigot.command.*
import me.happy.door.spigot.lane.LaneManager
import me.happy.door.spigot.packet.*
import me.happy.door.spigot.player.QueuePlayerListener
import me.happy.door.spigot.player.QueuePlayerManager
import me.happy.door.spigot.queue.QueueManager
import me.happy.door.spigot.server.ServerManager

class Door : JavaPlugin() {

    lateinit var queuePlayerManager: QueuePlayerManager
    lateinit var queueManager: QueueManager
    lateinit var serverManager: ServerManager
    lateinit var laneManager: LaneManager
    private val taskIds: MutableSet<Int> = HashSet()

    override fun onEnable() {
        saveDefaultConfig()

        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")

        JedisManager.connect(config.getString("redis.host"), config.getInt("redis.port"), config.getString("redis.password"))

        listOf(QueueAddPlayerPacket(), QueuePlayerJoinPacket(), QueuePlayerMessagePacket(), QueuePlayerUpdatePacket(),
                QueuePlayerQuitPacket(), QueuePlayerSendPacket(), ServerUpdatePacket(), LaneUpdatePacket(),
                QueueSetUpdateTimePacket(), QueuePausePacket())
                .forEach { packet: Packet -> JedisManager.registerPacket(packet) }

        queuePlayerManager = QueuePlayerManager()
        queueManager = QueueManager()
        serverManager = ServerManager()
        laneManager = LaneManager()

        server.pluginManager.registerEvents(QueuePlayerListener(), this)

        for (key in config.getConfigurationSection("lanes").getKeys(false)) {
            laneManager.lanes.add(Lane(config.getString("lanes.$key.name"), config.getString("lanes.$key.permission"),
                    config.getInt("lanes.$key.priority")))
        }

        getCommand("joinqueue").executor = JoinQueueCommand()
        getCommand("setupdatetime").executor = SetUpdateTimeCommand()
        getCommand("pausequeue").executor = PauseQueueCommand()
        getCommand("queue").executor = QueueCommand(this)
        getCommand("checkposition").executor = CheckPositionCommand(this)

        taskIds.add(server.scheduler.runTaskTimerAsynchronously(this, {
            JedisManager.writePacket(ServerUpdatePacket(config.getString("server-name"), Bukkit.getOnlinePlayers().size,
                    Bukkit.getMaxPlayers(), 10000, ServerStatus.ONLINE))
        }, 20L * 5L, 20L * 5L).taskId)

        taskIds.add(server.scheduler.runTaskTimerAsynchronously(this, {
            for (lane in laneManager.lanes) {
                JedisManager.writePacket(LaneUpdatePacket(lane.name, lane.permission, lane.priority))
            }
        }, 20L * 5L, 20L * 5L).taskId)
    }

    override fun onDisable() {
        taskIds.forEach{ id: Int -> server.scheduler.cancelTask(id) }

        JedisManager.writePacket(ServerUpdatePacket(config.getString("server-name"), Bukkit.getOnlinePlayers().size,
                Bukkit.getMaxPlayers(), 10000, ServerStatus.OFFLINE))
    }
}