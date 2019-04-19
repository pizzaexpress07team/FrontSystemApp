package com.express.pizza.pdq.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.express.pizza.pdq.R
import com.express.pizza.pdq.activity.AddressManagementActivity
import com.express.pizza.pdq.activity.WelcomeActivity
import com.express.pizza.pdq.business.entity.UserInfo
import com.express.pizza.pdq.utils.SavedKeyConst
import com.express.pizza.pdq.utils.SharedPrefsUtils
import com.express.pizza.pdq.viewmodel.MeViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.me_fragment.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class MeFragment : Fragment() {

    companion object {
        fun newInstance() = MeFragment()
        const val TAG = "MeFragment"
    }

    private lateinit var viewModel: MeViewModel
    private var userInfo: UserInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.me_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (SharedPrefsUtils.getValue(activity, SavedKeyConst.IS_SIGNED_IN, false)) {
            userInfo = Gson().fromJson(loadUserInfo(), UserInfo::class.java)
            Log.d("user--", loadUserInfo())
            meUserNameText.text = userInfo?.username
            mePhoneText.text = userInfo?.phone
            meLogOutArea.visibility = View.VISIBLE
        } else {
            meLogOutArea.visibility = View.GONE
        }
        meLogOutArea.setOnClickListener {
            showLogOutDialog()
        }
        meExitArea.setOnClickListener {
            showExitDialog()
        }
        meAddressArea.setOnClickListener {
            val intent = Intent(activity, AddressManagementActivity::class.java)
            intent.putExtra(AddressManagementActivity.ADDR_INFO, userInfo?.addr)
            startActivity(intent)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MeViewModel::class.java)
        // TODO: Use the ViewModel
        viewModel.getUserInfo(SharedPrefsUtils.getValue(context, SavedKeyConst.SAVED_UID, "-1"))
            .observe(this, Observer<UserInfo> {
                if (it != null) {
                    userInfo = it
                    meUserNameText.text = it.username
                    mePhoneText.text = it.phone
                    meLogOutArea.visibility = View.VISIBLE
                } else {
                    meLogOutArea.visibility = View.GONE
                }
            })
    }


    private fun loadUserInfo(): String {
        var reader: BufferedReader? = null
        val content = StringBuilder()
        try {
            val `in` = activity?.openFileInput("user_info")
            reader = BufferedReader(InputStreamReader(`in`))
            var line = reader.readLine()
            while (line != null) {
                content.append(line)
                line = reader.readLine()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return content.toString()
    }

    private fun showLogOutDialog() {
        val logOutDialog = AlertDialog.Builder(context!!)
        logOutDialog.setMessage("是否登出帐号并回到欢迎页面？")
        logOutDialog.setPositiveButton(
            "确定"
        ) { _, _ ->
            SharedPrefsUtils.putValue(context, SavedKeyConst.IS_SIGNED_IN, false)
            SharedPrefsUtils.putValue(context, SavedKeyConst.SAVED_UID, "-1")
            startActivity(Intent(activity, WelcomeActivity::class.java))
            activity?.finish()
        }
        logOutDialog.setNegativeButton(
            "取消"
        ) { dialog, _ ->
            dialog.dismiss()
        }
        logOutDialog.show()
    }

    private fun showExitDialog() {
        val logOutDialog = AlertDialog.Builder(context!!)
        logOutDialog.setMessage("确定要关闭应用吗？")
        logOutDialog.setPositiveButton(
            "确定"
        ) { _, _ ->
            activity?.finish()
        }
        logOutDialog.setNegativeButton(
            "取消"
        ) { dialog, _ ->
            dialog.dismiss()
        }
        logOutDialog.show()
    }

}
