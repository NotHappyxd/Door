package me.happy.door.spigot.packet

import me.happy.door.commons.redis.Packet
import me.happy.door.spigot.Door
import java.util.UUID
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.util.stream.Collectors

class QueuePlayerMessagePacket : Packet {
    private var playerUuid: UUID? = null
    private var queueName: String? = null
    private var position = 0
    private var maxPosition = 0
    private var eta = 0

    constructor(playerUuid: UUID?, queueName: String?, position: Int, maxPosition: Int, eta: Int) {
        this.playerUuid = playerUuid
        this.queueName = queueName
        this.position = position
        this.maxPosition = maxPosition
        this.eta = eta
    }

    constructor() {}

    override fun onReceive() {
        val player = Bukkit.getPlayer(playerUuid) ?: return
        player.sendMessage(JavaPlugin.getPlugin(Door::class.java).config.getStringList("queue-message").stream()
            .map { s: String ->
                ChatColor.translateAlternateColorCodes(
                    '&', s
                        .replace("{queueName}", queueName!!)
                        .replace("{position}", position.toString())
                        .replace("{size}", maxPosition.toString())
                        .replace("{eta}", (eta / 1000L).toString())
                )
            }
            .collect(Collectors.joining("\n")))
    }

    override fun getIdentifier(): String {
        return "QueuePlayerMessagePacket"
    }
}