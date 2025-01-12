package me.happy.door.server.lane

import me.happy.door.commons.lane.Lane
import java.util.ArrayList

class LaneManager {
    val lanes: MutableList<Lane> = ArrayList()

    fun getLaneByName(name: String): Lane? {
        return lanes.stream()
            .filter { lane: Lane? -> lane!!.name.equals(name, ignoreCase = true) }
            .findFirst()
            .orElse(null)
    }

    fun getLaneForPermission(permission: String): Lane? {
        return lanes.stream()
            .filter { lane: Lane? -> lane!!.permission.equals(permission, ignoreCase = true) }
            .findFirst()
            .orElse(null)
    }
}