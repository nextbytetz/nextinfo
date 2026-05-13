package org.nextinfo.project

class Greeting {
    private val username = "Nancy"
    fun greet(): String {
        return "Hello, ${username}!"
    }

    private val platform: Platform = getPlatform()
    fun deviceName(): String {
        return "${platform.name}!"
    }
}