package com.example.tagihin.view.search

//class SearchActivity : BaseActivity<SearchViewModel,ActivityMainBinding>(SearchViewModel::class) {
//
//    override fun getLayoutRes(): Int = R.layout.activity_search
//
//    override fun showMessage(message: String) {
//        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
//    }
//
//    override fun initView(savedInstanceState: Bundle?) {
//        val adapter = FragmentPagerItemAdapter(
//            supportFragmentManager,
//            FragmentPagerItems.with(this)
//                .add(Consts.PAID,PaidFragment::class.java)
//                .add(Consts.PENDING,PendingFragment::class.java)
//                .add(Consts.UNPAID,UnpaidFragment::class.java)
//                .create()
//        )
//        dataBinding.viewPager.adapter = adapter
//        dataBinding.tabs.setViewPager(dataBinding.viewPager)
//        viewModel.getBillStat()
//        viewModel.billStat.observe(this, Observer {
//            with(dataBinding){
//                totalText.text = it.seluruh.toString()
//                paidText.text = it.lunas.toString()
//                unpaidText.text = it.belum.toString()
//                pendingText.text = it.pending.toString()
//            }
//            stopAllLoader()
//        })
//        dataBinding.hamburgerIcon.setOnClickListener{
//            dataBinding.drawerContainer.openDrawer(dataBinding.drawer)
//        }
//    }
//
//    fun stopAllLoader(){
//        with(dataBinding){
//            totalLoading.visibility = View.GONE
//            paidLoading.visibility = View.GONE
//            pendingLoading.visibility = View.GONE
//            unpaidLoading.visibility = View.GONE
//
//            totalText.visibility = View.VISIBLE
//            paidText.visibility = View.VISIBLE
//            pendingText.visibility = View.VISIBLE
//            unpaidText.visibility = View.VISIBLE
//
//        }
//    }
//    override fun setupToolbar() {}
//
//}