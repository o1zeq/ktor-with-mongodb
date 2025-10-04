package top.dedicado.utils.generators

import java.security.SecureRandom

object RandomStringGenerator {

    private const val CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz"
    private val CHAR_UPPER = CHAR_LOWER.uppercase()
    private const val NUMBER = "0123456789"
    private const val OTHERS = "!@#$%&*"

    private val PASSWORD_CHARS = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHERS
    private const val NUMERIC_CODE_CHARS = NUMBER
    private val CODE_CHARS = CHAR_LOWER + CHAR_UPPER + NUMBER

    private val random = SecureRandom()

    private fun generate(length: Int, characterSet: String): String {
        return (1..length)
            .map { random.nextInt(characterSet.length) }
            .map(characterSet::get)
            .joinToString("")
    }

    fun generatePassword(length: Int?): String = generate(length?: 8, PASSWORD_CHARS)
    fun generateNumericCode(length: Int?): String = generate(length?: 6, NUMERIC_CODE_CHARS)
    fun generateCode(length: Int?): String = generate(length?: 6, CODE_CHARS)
}