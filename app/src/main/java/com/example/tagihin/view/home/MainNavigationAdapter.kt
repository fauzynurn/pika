//package com.example.tagihin.view.home
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentStatePagerAdapter
//import com.example.tagihin.utils.Consts
//import com.example.tagihin.view.home.fragments.regularbill.RegularBillFragment
//import com.example.tagihin.view.bill.paid.PaidActivity
//import com.example.tagihin.view.bill.pending.PendingActivity
//import com.example.tagihin.view.bill.unpaid.UnpaidActivity
//
//class MainNavigationAdapter(fragmentManager: FragmentManager) :
//    FragmentStatePagerAdapter(fragmentManager) {
////
////    val fragments: MutableList<Fragment> = ArrayList()
//
//    override fun getItem(position: Int): Fragment {
//        return when (position) {
//            0 -> PaidActivity()
//            1 -> PendingActivity()
//            2 -> UnpaidActivity()
//            else -> RegularBillFragment()
//        }
//    }
//
//    override fun getCount(): Int = 3
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return when(position){
//            0 -> Consts.PAID
//            1 -> Consts.PENDING
//            2 -> Consts.UNPAID
//            else -> "UNDEFINED"
//        }
//    }
//}