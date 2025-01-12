package me.happy.door.spigot.packet

import me.happy.door.commons.queue.Queue
import me.happy.door.commons.redis.Packet
import me.happy.door.spigot.Door
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class QueueSetUpdateTimePacket : Packet {

    var queueName: String? = null
    var size: Long? = -1

    constructor(queueName: String, size: Long) {
        this.queueName = queueName
        this.size = size
    }

    constructor()

    @Transient
    val door: Door = JavaPlugin.getPlugin(Door::class.java)

    override fun onReceive() {
        val queue: Queue? = door.queueManager.getQueueByName(queueName!!)

        if (queue == null) {
            Bukkit.broadcast("${ChatColor.RED}Cannot find queue by name $queueName while trying to set size", "lithium.staff")
            return
        }

        queue.timeBetweenBatch = size!!
        Bukkit.broadcast("${ChatColor.RED}Changed time between batch on $queueName to ${queue.timeBetweenBatch}", "lithium.staff")
    }

    override fun getIdentifier(): String {
        return "QueueSetUpdateTimePacket"
    }
}