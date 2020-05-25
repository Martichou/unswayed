package me.martichou.unswayed.models

import java.util.Date

data class DateItem(val date: Date) : GeneralItem() {
    override val type: Int
        get() = TYPE_DATE
}