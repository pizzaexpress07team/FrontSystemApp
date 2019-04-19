package com.express.pizza.pdq.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import com.express.pizza.pdq.R
import com.express.pizza.pdq.business.entity.UserInfo
import com.express.pizza.pdq.fragment.MeFragment
import com.express.pizza.pdq.fragment.MenuFragment
import com.express.pizza.pdq.fragment.OrderFragment
import com.express.pizza.pdq.repository.UserInfoRepository
import com.express.pizza.pdq.utils.CanClickUtils
import com.express.pizza.pdq.utils.SavedKeyConst
import com.express.pizza.pdq.utils.SharedPrefsUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {

    companion object {
        const val SDK_PERMISSION_REQUEST = 8
    }
    private var currentFragment: Fragment? = null
    private val fragmentList: ArrayList<Fragment> = ArrayList()
    private var mExitTime = 0L
    private var userInfo: UserInfo? = null

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
//        checkPermissions()
        initData()
        initView()
    }

    private fun initData() {
        if (SharedPrefsUtils.getValue(this, SavedKeyConst.IS_SIGNED_IN, false)) {
            OkHttpUtils
                .get()
                .url(UserInfoRepository.URL_USER_INFO)
                .addParams("uid", SharedPrefsUtils.getValue(this, SavedKeyConst.SAVED_UID, "-1"))
                .build()
                .execute(object : StringCallback() {
                    override fun onResponse(response: String?, id: Int) {
                        val gson = Gson()
                        val jsonObject = gson.fromJson(response, JsonObject::class.java).asJsonObject
                        if (jsonObject.get("errorCode").asInt == 0) {
                            Log.d("User--", jsonObject.get("userInfo").asJsonObject.toString())
                            userInfo = gson.fromJson(jsonObject.get("userInfo").asJsonObject, UserInfo::class.java)
                            saveUserInfo()
                        }
                    }

                    override fun onError(call: Call?, e: java.lang.Exception?, id: Int) {
                    }
                })
        }
    }

    private fun saveUserInfo() {
        val data = Gson().toJson(userInfo)
        var writer: BufferedWriter? = null
        try {
            val out = openFileOutput("user_info", Context.MODE_PRIVATE)
            writer = BufferedWriter(OutputStreamWriter(out))
            writer.write(data)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
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


//    private fun checkPermissions() {
//        val permissions = ArrayList<String>()
//        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
//        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
//        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        permissions.add(Manifest.permission.READ_PHONE_STATE)
//        if (permissions.isNotEmpty()) {
//            requestPermissions(permissions.toArray(arrayOfNulls(permissions.size)), SDK_PERMISSION_REQUEST)
//        }
//    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        // 防止快速点击启动新Activity
        if (!CanClickUtils.canClick()) {
            return
        }
        super.startActivityForResult(intent, requestCode, options)
    }
}
