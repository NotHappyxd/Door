package me.happy.door.spigot.packet

import java.util.UUID
import org.bukkit.entity.Player
import org.bukkit.Bukkit
import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import me.happy.door.commons.redis.Packet
import me.happy.door.spigot.Door
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Exception

class QueuePlayerSendPacket : Packet {
    private var playerUuid: UUID? = null
    private var serverName: String? = null

    constructor() {}

    constructor(playerUuid: UUID?, serverName: String?) {
        this.playerUuid = playerUuid
        this.serverName = serverName
    }

    override fun onReceive() {
        val player = Bukkit.getPlayer(playerUuid) ?: return
        player.sendMessage(ChatColor.GREEN.toString() + "Sending you to " + serverName)

        val queue = JavaPlugin.getPlugin(Door::class.java).queueManager.getQueueByServerName(serverName!!) ?: return

        queue.players.removeIf { queuePlayer -> queuePlayer.uuid.equals(player.uniqueId) } // todo better way

        try {
            val out = ByteStreams.newDataOutput()
            out.writeUTF("Connect")
            out.writeUTF(serverName)
            player.sendPluginMessage(JavaPlugin.getPlugin(Door::class.java), "BungeeCord", out.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getIdentifier(): String {
        return "QueuePlayerSend"
    }
}