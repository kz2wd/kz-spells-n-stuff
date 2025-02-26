package com.cludivers.kz2wdprison.framework.commands

enum class MainCommandNames(val commandName: String) {
    SHARDS("shards");

    override fun toString(): String {
        return commandName
    }

}