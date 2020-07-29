package me.martichou.unswayedphotos.models.rv

import java.util.*

data class HomeDate(val date: Date) : HomeGeneral() {
    override val type: Int
        get() = TYPE_DATE
}