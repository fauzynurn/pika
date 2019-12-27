package com.example.tagihin.utils

class Consts {
    companion object{

        const val PAID: String = "LUNAS"
        const val UNPAID : String = "BELUM LUNAS"
        const val UNPAID_SHORT : String = "BELUM"
        const val PENDING_ENG : String = "PENDING"
        const val PENDING : String = "TERTUNDA"
        const val SIAGA = "SIAGA"
        const val RUSAK = "RUSAK"
        const val BANGUNAN = "BANGUNAN"
        const val CONNECT_TIMEOUT: Long = 100000
        const val READ_TIMEOUT: Long = 100000
        const val WRITE_TIMEOUT: Long = 100000

        const val REQUEST_IMAGE = 100
        val OPTIONS = listOf(PENDING_ENG, PAID)
    }
}