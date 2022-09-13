package com.happysnaker

import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import java.io.File

fun setupWorkingDir() {
    // see: net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal
    System.setProperty("user.dir", File("debug-sandbox").absolutePath)
}
@OptIn(net.mamoe.mirai.console.util.ConsoleExperimentalApi::class)
suspend fun main() {
    setupWorkingDir()

    MiraiConsoleTerminalLoader.startAsDaemon()

    val pluginInstance = Main.INSTANCE

    pluginInstance.load() // 主动加载插件, Console 会调用 Main.onLoad
    pluginInstance.enable() // 主动启用插件, Console 会调用 Main.onEnable
    MiraiConsole.addBot(123456, "密码").alsoLogin() // 登录一个测试环境的 Bot
    MiraiConsole.job.join()
}