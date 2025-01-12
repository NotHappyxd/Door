package me.happy.door.spigot.server

import me.happy.door.commons.server.DoorServer
import java.util.ArrayList

class ServerManager {
    val servers: MutableList<DoorServer?> = ArrayList()

    fun getServerByName(name: String): DoorServer? {
        return servers.stream()
            .filter { server: DoorServer? -> server!!.name.equals(name, ignoreCase = true) }
            .findFirst()
            .orElse(null)
    }
}