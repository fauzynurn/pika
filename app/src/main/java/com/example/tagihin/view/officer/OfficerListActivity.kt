package com.example.tagihin.view.officer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagihin.R
import com.example.tagihin.base.BaseActivity
import com.example.tagihin.data.remote.model.Officer
import com.example.tagihin.databinding.ActivityOfficerListBinding

class OfficerListActivity : BaseActivity<OfficerListViewModel,ActivityOfficerListBinding>(OfficerListViewModel::class){
    private var officerListAdapter: OfficerListAdapter? = null
    override fun getLayoutRes(): Int = R.layout.activity_officer_list

    override fun showMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    override fun initView(savedInstanceState: Bundle?) {

        viewModel.networkState.observe(this, Observer {
            officerListAdapter?.setNetworkState(it)
        })

        officerListAdapter = OfficerListAdapter(
            this,
            object :
                OfficerListOnClickListener {
                override fun onClick(officer: Officer) {
                    val intent = Intent().putExtra(
                        "recipient", officer.username
                    )
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            },
            {
                viewModel.retry()
            },
            object : DiffUtil.ItemCallback<Officer>() {
                override fun areItemsTheSame(oldItem: Officer, newItem: Officer): Boolean =
                    oldItem.username == newItem.username

                override fun areContentsTheSame(
                    oldItem: Officer,
                    newItem: Officer
                ): Boolean = oldItem == newItem
            }

        )
        dataBinding.billRecycler.apply {
            layoutManager = LinearLayoutManager(context!!)
            adapter = officerListAdapter
        }
        viewModel.query.value = ""
        viewModel.searchResult.observe(this, Observer {
            officerListAdapter?.submitList(it) {
                // Workaround for an issue where RecyclerView incorrectly uses the loading / spinner
                // item added to the end of the list as an anchor during initial load.
                val layoutManager = (dataBinding.billRecycler.layoutManager as LinearLayoutManager)
                val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (position != RecyclerView.NO_POSITION) {
                    dataBinding.billRecycler.scrollToPosition(position)
                }
            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setupToolbar() {
        setSupportActionBar(dataBinding.toolbarMain)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
        dataBinding.appbarTitle.text = "Pilih petugas"
    }

}