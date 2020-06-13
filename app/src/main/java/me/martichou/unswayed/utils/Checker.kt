package me.martichou.unswayed.utils

import java.util.regex.Pattern

fun isEmailValid(email: String): Boolean {
    return Pattern.compile(
        "^([a-z0-9_+]([a-z0-9_+.]*[a-z0-9_+])?)@([a-z0-9]+([\\-.][a-z0-9]+)*\\.[a-z]{2,6})"
    ).matcher(email).matches()
}