package com.express.pizza.pdq.activity

import android.Manifest
import android.os.Bundle
import android.widget.Toast
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

    companion object {
        const val SDK_PERMISSION_REQUEST = 8
    }
    private var currentFragment: Fragment? = null
    private val fragmentList: ArrayList<Fragment> = ArrayList()
    private var mExitTime = 0L


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
        checkPermissions()
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
        } else {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show()
                mExitTime = System.currentTimeMillis()
                return
            }
        }
        super.onBackPressed()
    }


    private fun checkPermissions() {
        val permissions = ArrayList<String>()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permissions.add(Manifest.permission.READ_PHONE_STATE)
        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(arrayOfNulls(permissions.size)), SDK_PERMISSION_REQUEST)
        }
    }
}
