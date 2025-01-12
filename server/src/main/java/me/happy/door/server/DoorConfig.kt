package me.happy.door.server

import lombok.SneakyThrows
import java.io.File
import java.io.FileOutputStream
import java.io.FileInputStream
import java.util.*

class DoorConfig @SneakyThrows constructor() {
    private val properties: Properties

    init {
        val file = File("config.properties")
        properties = Properties()
        if (!file.exists()) {
            properties.setProperty("redis.host", "localhost")
            properties.setProperty("redis.port", "6379")
            properties.setProperty("redis.password", "paossword")
            file.createNewFile()
            properties.store(FileOutputStream(file), null)
        } else properties.load(FileInputStream(file))
    }

    fun getString(key: String?): String {
        return properties.getProperty(key)
    }

    fun getBoolean(key: String?): Boolean {
        return java.lang.Boolean.parseBoolean(properties.getProperty(key))
    }

    fun getInt(key: String?): Int {
        return properties.getProperty(key).toInt()
    }
}