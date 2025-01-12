package me.happy.door.spigot.packet

import org.bukkit.plugin.java.JavaPlugin
import me.happy.door.commons.lane.Lane
import me.happy.door.commons.redis.Packet
import me.happy.door.spigot.Door

class LaneUpdatePacket : Packet {
    private var name: String? = null
    private var permission: String? = null
    private var priority = 0

    @Transient
    private val door = JavaPlugin.getPlugin(Door::class.java)

    constructor() {}

    constructor(name: String?, permission: String?, priority: Int) {
        this.name = name
        this.permission = permission
        this.priority = priority
    }

    override fun onReceive() {
        var lane = door.laneManager.getLaneByName(name)

        if (lane == null) {
            lane = Lane(name!!, permission!!, priority)
            println("Creating lane")
            door.laneManager.lanes.add(lane)
            return
        }

        lane.priority = priority
        lane.permission = permission!!
    }

    override fun getIdentifier(): String {
        return "LaneUpdatePacket"
    }
}