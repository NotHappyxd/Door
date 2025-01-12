package me.happy.door.spigot.player

import me.happy.door.commons.redis.JedisManager
import me.happy.door.spigot.packet.QueuePlayerJoinPacket
import me.happy.door.spigot.packet.QueuePlayerQuitPacket
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent

class QueuePlayerListener : Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onJoin(event: PlayerJoinEvent) {
        JedisManager.writePacket(QueuePlayerJoinPacket(event.player.uniqueId))
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        JedisManager.writePacket(QueuePlayerQuitPacket(event.player.uniqueId))
    }

    @EventHandler
    fun onKick(event: PlayerKickEvent) {
        JedisManager.writePacket(QueuePlayerQuitPacket(event.player.uniqueId))
    }
}