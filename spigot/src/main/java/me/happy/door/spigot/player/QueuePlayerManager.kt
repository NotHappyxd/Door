package me.happy.door.spigot.player

import java.util.UUID
import me.happy.door.commons.queue.QueuePlayer
import java.util.concurrent.ConcurrentHashMap

class QueuePlayerManager {
    val playerMap: MutableMap<UUID, QueuePlayer> = ConcurrentHashMap()
}