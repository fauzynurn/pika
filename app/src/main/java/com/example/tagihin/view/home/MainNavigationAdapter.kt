package com.example.tagihin.view.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import com.example.tagihin.utils.Consts
import com.example.tagihin.view.home.fragments.regularbill.RegularBillFragment
import com.example.tagihin.view.home.fragments.regularbill.fragments.PaidFragment
import com.example.tagihin.view.home.fragments.regularbill.fragments.PendingFragment
import com.example.tagihin.view.home.fragments.regularbill.fragments.UnpaidFragment
import com.example.tagihin.view.home.fragments.workorder.WorkOrderFragment

class MainNavigationAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {
//
//    val fragments: MutableList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PaidFragment()
            1 -> PendingFragment()
            2 -> UnpaidFragment()
            else -> RegularBillFragment()
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> Consts.PAID
            1 -> Consts.PENDING
            2 -> Consts.UNPAID
            else -> "UNDEFINED"
        }
    }
}