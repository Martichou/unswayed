package me.martichou.unswayedphotos.data.model.recycler

import java.util.*

data class HomeDate(val date: Date) : HomeGeneral() {
    override val type: Int
        get() = TYPE_DATE
}