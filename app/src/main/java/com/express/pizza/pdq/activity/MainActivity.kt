package com.express.pizza.pdq.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.express.pizza.pdq.R
import com.express.pizza.pdq.fragment.MeFragment
import com.express.pizza.pdq.fragment.MenuFragment
import com.express.pizza.pdq.fragment.OrderFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fragmentList: ArrayList<Fragment> = ArrayList()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_menu -> {
                switchFragment(0)
            }
            R.id.navigation_order -> {
                switchFragment(1)
            }
            R.id.navigation_me -> {
                switchFragment(2)
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        fragmentList.add(MenuFragment.newInstance())
        fragmentList.add(OrderFragment.newInstance())
        fragmentList.add(MeFragment.newInstance())
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        switchFragment(0)
    }

    private fun switchFragment(index: Int) {
        supportFragmentManager.beginTransaction().replace(R.id.main_container, fragmentList[index]).commit()
    }
}
