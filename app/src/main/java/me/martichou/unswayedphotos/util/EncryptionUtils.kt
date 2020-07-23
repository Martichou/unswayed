package me.martichou.unswayedphotos.util

import android.security.keystore.KeyProperties
import android.security.keystore.KeyProtection
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

fun generateAesKeyFromPassword(password: CharArray, salt: ByteArray, iter: Int): SecretKey {
    val factory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    val spec: KeySpec = PBEKeySpec(password, salt, iter, 256)
    return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
}

fun aesKeyProtection(): KeyProtection {
    return KeyProtection.Builder(KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT)
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build()
}