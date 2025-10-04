package org.company.chatapp.utils

object JpaDebug {
    fun printRow(row: Array<Any?>) {
        val values = row.joinToString(prefix = "[", postfix = "]") { it.toString() }
        val types  = row.joinToString(prefix = "[", postfix = "]") { it?.javaClass?.name ?: "null" }
        println("[VALUES] $values")
        println("[TYPES ] $types")
        println("----")
    }
}
