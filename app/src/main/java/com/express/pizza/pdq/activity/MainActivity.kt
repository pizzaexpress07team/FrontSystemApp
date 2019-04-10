package com.express.pizza.pdq.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import com.express.pizza.pdq.R
import com.express.pizza.pdq.fragment.MeFragment
import com.express.pizza.pdq.fragment.MenuFragment
import com.express.pizza.pdq.fragment.OrderFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var currentFragment: Fragment? = null
    private val fragmentList: ArrayList<Fragment> = ArrayList()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_menu -> {
                switchFragment(fragmentList[0]).commit()
            }
            R.id.navigation_order -> {
                switchFragment(fragmentList[1]).commit()
            }
            R.id.navigation_me -> {
                switchFragment(fragmentList[2]).commit()
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun switchFragment(targetFragment: Fragment): FragmentTransaction {
        val transaction = supportFragmentManager.beginTransaction()
        if (!targetFragment.isAdded) {
            currentFragment?.apply {
                transaction.hide(this)
            }
            transaction.add(R.id.main_container, targetFragment, targetFragment.tag)
        } else {
            transaction.hide(currentFragment!!).show(targetFragment)
        }
        currentFragment = targetFragment
        return transaction
    }

    private fun initView() {
        fragmentList.add(MenuFragment.newInstance())
        fragmentList.add(OrderFragment.newInstance())
        fragmentList.add(MeFragment.newInstance())
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        switchFragment(fragmentList[0]).commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        if (currentFragment == fragmentList[0]) {
            return Navigation.findNavController(this, R.id.menu_container).navigateUp()
        }
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (currentFragment == fragmentList[0]
            && Navigation.findNavController(this, R.id.menu_container).popBackStack()
        ) {
            return
        }
        super.onBackPressed()
    }
}
