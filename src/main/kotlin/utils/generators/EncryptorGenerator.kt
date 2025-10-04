package top.dedicado.utils.generators

import org.mindrot.jbcrypt.BCrypt

object EncryptorGenerator {
    fun generateHash(data: String): String {
        return BCrypt.hashpw(data, BCrypt.gensalt())
    }

    fun verifyHash(data: String, hash: String): Boolean {
        return BCrypt.checkpw(data, hash)
    }
}