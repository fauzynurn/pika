package com.example.tagihin.view.home.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tagihin.R
import com.example.tagihin.data.remote.NetworkState
import com.example.tagihin.data.remote.Status
import com.example.tagihin.databinding.BillItemLayoutBinding
import com.example.tagihin.databinding.NetworkStateItemBinding

class NetworkStateViewHolder(
    val binding: NetworkStateItemBinding,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(networkState: NetworkState?) {
        with(binding) {
            progressBar.visibility = toVisibility(networkState?.status == Status.RUNNING)
            retryButton.visibility = toVisibility(networkState?.status == Status.FAILED)
            retryButton.setOnClickListener {
                retryCallback()
            }
            errorMsg.visibility = toVisibility(networkState?.msg != null)
            errorMsg.text = networkState?.msg
        }
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateViewHolder {
            val view = NetworkStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return NetworkStateViewHolder(view, retryCallback)
        }
        fun toVisibility(constraint: Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}