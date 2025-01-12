package me.happy.door.commons.server

class DoorServer {
    lateinit var name: String
    var onlinePlayers = 0
    var maxPlayers = 0
    var maxQueueSize = 0
    var status: ServerStatus? = null
    var lastUpdated: Long = 0


    override fun toString(): String {
        return "DoorServer(name=" + name + ", onlinePlayers=" + onlinePlayers + ", maxPlayers=" + maxPlayers + ", maxQueueSize=" + maxQueueSize + ", status=" + status + ", lastUpdated=" + lastUpdated + ")"
    }
}