package me.happy.door.commons.queue

import me.happy.door.commons.lane.Lane
import java.util.UUID
import me.happy.door.commons.server.DoorServer
import java.util.HashMap

class QueuePlayer(
    val uuid: UUID,
    var lane: Lane,
    var offline: Boolean,
    var lastOnline: Long,
    var currentServer: DoorServer
) {

    val insertedAtMap: MutableMap<Queue, Long> = HashMap()

    fun getInsertedOn(queue: Queue): Long? {
        return insertedAtMap[queue]
    }
}