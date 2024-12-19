package com.camc.proyecto_redesiii

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object Encriptacion {
    // Genera una clave AES
    public fun generarLlave(): SecretKey {
        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(256) // Usa 256 bits para máxima seguridad
        return keyGen.generateKey()
    }

    // Encripta datos
    public fun encriptar(json: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(json.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    // Desencripta datos
    public fun desencriptar(encryptedJson: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedBytes = Base64.decode(encryptedJson, Base64.DEFAULT)
        return String(cipher.doFinal(decodedBytes), Charsets.UTF_8)
    }

    // Convierte una clave AES a un string Base64 para compartir
    public fun claveToString(secretKey: SecretKey): String {
        return Base64.encodeToString(secretKey.encoded, Base64.DEFAULT)
    }

    // Uso
    /*
    val secretKey = generarLlave()
    val json = """{"nombre": "Juan", "edad": 30}"""
    val encryptedJson = encriptar(json, secretKey)
    Log.d("Encriptado:", "$encryptedJson")
    val decryptedJson = desencriptar(encryptedJson, secretKey)
    Log.d("Desencriptado:", "$decryptedJson")
     */

}