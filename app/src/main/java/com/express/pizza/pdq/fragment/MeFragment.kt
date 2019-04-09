package com.express.pizza.pdq.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.express.pizza.pdq.R
import com.express.pizza.pdq.activity.WelcomeActivity
import com.express.pizza.pdq.entity.UserInfo
import com.express.pizza.pdq.utils.SavedKeyConst
import com.express.pizza.pdq.utils.SharedPrefsUtils
import com.express.pizza.pdq.viewmodel.MeViewModel
import kotlinx.android.synthetic.main.me_fragment.*

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

        if (userInfo == null) {
            meLogOutArea.visibility = View.GONE
        }
        meLogOutArea.setOnClickListener {
            showLogOutDialog()
        }
        meExitArea.setOnClickListener {
            showExitDialog()
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
