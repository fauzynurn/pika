package com.example.tagihin.utils

class Consts {
    companion object{

        const val PAID: String = "LUNAS"
        const val UNPAID : String = "BELUM LUNAS"
        const val UNPAID_SHORT : String = "BELUM"
        const val UNPAID_ENG : String = "PENDING"
        const val PENDING : String = "TERTUNDA"
        const val CONNECT_TIMEOUT: Long = 100000
        const val READ_TIMEOUT: Long = 100000
        const val WRITE_TIMEOUT: Long = 100000

        val OPTIONS = listOf("PENDING", "LUNAS")
    }
}