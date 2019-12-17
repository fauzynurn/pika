package com.example.tagihin.data.remote.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

class BillResponse<T>(
    override var status: Boolean,
    override var message: String,
    //Using PaidBill class since the response structure is same.
    override var data: T?
) : BaseResponse<T>

data class BaseBill(
    var id: String = "",
    var idpel: String = "",
    var nama: String = "",
    var alamat: String = "",
    var rbm: String = "",
    var total: String = "",
    var tanggal: String? = "",
    var keterangan: String = "",
    var tgl_pending: String? = "",
    var catatan: String? = "",
    var isSelected: Boolean = false
) : Serializable

fun PendingBill.toBase() : BaseBill{
    return BaseBill(
        this.id,
        this.idpel,
        this.nama,
        this.alamat,
        this.rbm,
        this.total,
        this.tanggal,
        this.keterangan,
        this.tgl_pending,
        this.catatan,
        this.isSelected
    )
}

fun UnpaidBill.toBase() : BaseBill{
    return BaseBill(
        this.id,
        this.idpel,
        this.nama,
        this.alamat,
        this.rbm,
        this.total,
        this.tanggal,
        this.keterangan,
        this.tgl_pending,
        this.catatan,
        this.isSelected
    )
}


fun PaidBill.toBase() : BaseBill{
    return BaseBill(
        this.id,
        this.idpel,
        this.nama,
        this.alamat,
        this.rbm,
        this.total,
        this.tanggal,
        this.keterangan,
        this.tgl_pending,
        this.catatan,
        this.isSelected
    )
}

fun PendingWorkOrder.toBase() : BaseBill{
    return BaseBill(
        this.id,
        this.idpel,
        this.nama,
        this.alamat,
        this.rbm,
        this.total,
        this.tanggal,
        this.keterangan,
        this.tgl_pending,
        this.catatan,
        this.isSelected
    )
}

fun UnpaidWorkOrder.toBase() : BaseBill{
    return BaseBill(
        this.id,
        this.idpel,
        this.nama,
        this.alamat,
        this.rbm,
        this.total,
        this.tanggal,
        this.keterangan,
        this.tgl_pending,
        this.catatan,
        this.isSelected
    )
}


@Entity(
    tableName = "paidbill",
    indices = [Index(value = ["id"], unique = true)]
)
data class PaidBill(
    @PrimaryKey
    var id: String = "",
    var idpel: String = "",
    var nama: String = "",
    var alamat: String = "",
    var rbm: String = "",
    var total: String = "",
    var tanggal: String? = "",
    var keterangan: String = "",
    var tgl_pending: String? = "",
    var index: Int? = null,
    var catatan: String? = "",
    var isSelected: Boolean = false
) : Serializable

@Entity(
    tableName = "pendingbill",
    indices = [Index(value = ["id"], unique = true)]
)
data class PendingBill(
    @PrimaryKey
    var id: String = "",
    var idpel: String = "",
    var nama: String = "",
    var alamat: String = "",
    var rbm: String = "",
    var total: String = "",
    var tanggal: String? = "",
    var keterangan: String = "",
    var tgl_pending: String? = "",
    var catatan: String? = "",
    var index: Int? = null,
    var status: Int? = null,
    var isSelected: Boolean = false
) : Serializable

@Entity(
    tableName = "unpaidworkorder",
    indices = [Index(value = ["id"], unique = true)]
)
data class UnpaidWorkOrder(
    @PrimaryKey
    var id: String = "",
    var idpel: String = "",
    var nama: String = "",
    var alamat: String = "",
    var rbm: String = "",
    var total: String = "",
    var tanggal: String? = "",
    var keterangan: String = "",
    var tgl_pending: String? = "",
    var catatan: String? = "",
    var isSelected: Boolean = false
) : Serializable

@Entity(
    tableName = "pendingworkorder",
    indices = [Index(value = ["id"], unique = true)]
)
data class PendingWorkOrder(
    @PrimaryKey
    var id: String = "",
    var idpel: String = "",
    var nama: String = "",
    var alamat: String = "",
    var rbm: String = "",
    var total: String = "",
    var tanggal: String? = "",
    var keterangan: String = "",
    var tgl_pending: String? = "",
    var catatan: String? = "",
    var isSelected: Boolean = false
) : Serializable

@Entity(
    tableName = "unpaidbill",
    indices = [Index(value = ["id"], unique = true)]
)
data class UnpaidBill(
    @PrimaryKey
    var id: String = "",
    var idpel: String = "",
    var nama: String = "",
    var alamat: String = "",
    var rbm: String = "",
    var total: String = "",
    var tanggal: String? = "",
    var keterangan: String = "",
    var tgl_pending: String? = "",
    var catatan: String? = "",
    var isSelected: Boolean = false,
    var index: Int? = null,
    var status: Int? = null
) : Serializable