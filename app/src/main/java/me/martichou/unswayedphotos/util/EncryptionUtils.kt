package me.martichou.unswayedphotos.util

import android.content.Context
import android.security.keystore.KeyProperties
import android.security.keystore.KeyProtection
import me.martichou.unswayedphotos.data.model.room.ImageLocal
import java.io.File
import java.io.FileOutputStream
import java.security.Key
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
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

fun ImageLocal.encrypt(
    context: Context,
    secretKey: Key,
    associatedData: ByteArray? = null
): File? {

    if (this.imgUri == null) return null
    val fis = context.contentResolver.openInputStream(this.imgUri) ?: return null
    val tmpFile = File(context.filesDir.absolutePath + File.separator + this.getUploadName())
    val fos = FileOutputStream(tmpFile) // create a tmp file which will be uploaded later

    val secureRandom = SecureRandom()
    val iv = ByteArray(12) // new iv for each encryption
    secureRandom.nextBytes(iv)

    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    val parameterSpec = GCMParameterSpec(128, iv) // 128 bit auth tag length
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec)

    associatedData?.let { cipher.updateAAD(it) }

    val cos = CipherOutputStream(fos, cipher)
    var b: Int
    val d = ByteArray(8)
    while (fis.read(d).also { b = it } != -1) {
        cos.write(d, 0, b)
    }
    cos.flush()
    cos.close()
    fis.close()

    Arrays.fill(iv, 0.toByte()) // overwrite the content of iv with zeros

    return tmpFile

}