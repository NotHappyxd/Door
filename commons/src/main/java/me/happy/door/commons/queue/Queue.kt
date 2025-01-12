package me.happy.door.commons.queue

import me.happy.door.commons.server.DoorServer
import java.util.PriorityQueue
import me.happy.door.commons.queue.QueuePlayer
import me.happy.door.commons.queue.QueuePlayerComparator

class Queue(val name: String) {

    var lastUpdate = 0L
    var lastSent = 0L
    var timeBetweenBatch = 5000L
    var perBatch = 1
    var isPaused = false
    var server: DoorServer? = null
    val players: PriorityQueue<QueuePlayer> = PriorityQueue(QueuePlayerComparator(this))

    val nextAvailablePlayer: QueuePlayer?
        get() {
            val playerIterator = players.iterator()
            while (playerIterator.hasNext()) {
                val next = playerIterator.next()
                if (!next.offline) {
                    playerIterator.remove()
                    return next
                }
            }
            return null
        }

    override fun toString(): String {
        return "Queue(name=$name, lastUpdate=$lastUpdate, lastSent=$lastSent, timeBetweenBatch=$timeBetweenBatch, perBatch=$perBatch, paused=$isPaused, server=$server, players=$players)"
    }
}