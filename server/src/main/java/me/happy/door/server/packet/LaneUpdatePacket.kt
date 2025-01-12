package me.happy.door.server.packet

import me.happy.door.commons.lane.Lane
import java.util.UUID
import me.happy.door.commons.queue.QueuePlayer
import java.util.HashMap
import me.happy.door.commons.redis.JedisManager
import me.happy.door.commons.redis.Packet
import me.happy.door.server.Door

class LaneUpdatePacket : Packet {
    private var name: String? = null
    private var permission: String? = null
    private var priority = 0

    constructor(name: String?, permission: String?, priority: Int) {
        this.name = name
        this.permission = permission
        this.priority = priority
    }

    constructor() {}

    override fun onReceive() {
        var lane = Door.INSTANCE.laneManager.getLaneByName(name!!)

        if (lane == null) {
            lane = Lane(name!!, permission!!, priority)
            println("Creating lane")
            Door.INSTANCE.laneManager.lanes.add(lane)
            return
        }

        lane.priority = priority
        lane.permission = permission!!
    }

    override fun getIdentifier(): String {
        return "LaneUpdatePacket"
    }
}