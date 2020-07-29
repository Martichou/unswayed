package me.martichou.unswayedphotos.models.rv

abstract class HomeGeneral {
    abstract val type: Int

    companion object {
        const val TYPE_DATE = 2
        const val TYPE_GENERAL = 1
    }
}