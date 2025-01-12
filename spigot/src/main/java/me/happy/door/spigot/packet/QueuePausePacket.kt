package me.happy.door.spigot.packet

import me.happy.door.commons.redis.Packet
import me.happy.door.spigot.Door
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class QueuePausePacket : Packet {

    private var name: String? = null
    private var paused: Boolean? = false

    constructor() {}

    constructor(name: String, paused: Boolean) {
        this.name = name
        this.paused = paused
    }

    override fun onReceive() {
        val queue = JavaPlugin.getPlugin(Door::class.java).queueManager.getQueueByName(name!!) ?: return

        queue.isPaused = paused!!

        Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}Queue $name pause status is set to $paused")

    }

    override fun getIdentifier(): String {
        return "QueuePausePacket"
    }
}