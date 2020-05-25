package me.martichou.unswayed.models

abstract class GeneralItem {
    abstract val type: Int

    companion object {
        const val TYPE_DATE = 2
        const val TYPE_GENERAL = 1
    }
}