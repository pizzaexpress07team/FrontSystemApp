package com.express.pizza.pdq.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.express.pizza.pdq.R
import com.express.pizza.pdq.activity.MainActivity
import com.express.pizza.pdq.utils.SavedKeyConst
import com.express.pizza.pdq.utils.SharedPrefsUtils
import com.express.pizza.pdq.utils.StringUtils
import com.express.pizza.pdq.utils.UrlConst
import com.express.pizza.pdq.viewmodel.WelcomeSignInNameViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import kotlinx.android.synthetic.main.welcome_sign_in_name_fragment.*
import okhttp3.Call


class WelcomeSignInNameFragment : Fragment() {

    companion object {
        fun newInstance() = WelcomeSignInNameFragment()
        var URL_LOGIN = UrlConst.URL_PRE + "/user/login"
    }

    private lateinit var viewModel: WelcomeSignInNameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.welcome_sign_in_name_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        (activity as AppCompatActivity).let {
            it.setSupportActionBar(toolbarSignInName)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            signOkBtn.setOnClickListener(okClickListener)
        }
    }

    private val okClickListener = View.OnClickListener {
        when {
            nameEditText.length() < 6 -> {
                Toast.makeText(activity, "用户名至少为6位，请确认", Toast.LENGTH_LONG).show()
            }
            passwordEditText.length() < 8 -> {
                Toast.makeText(activity, "密码至少为8位，请确认", Toast.LENGTH_LONG).show()
            }
            else -> {
                okProgressBar.visibility = View.VISIBLE
                signOkBtn.isEnabled = false
                OkHttpUtils
                    .get()
                    .url(URL_LOGIN)
                    .addParams("username", nameEditText.text.toString())
                    .addParams("password", passwordEditText.text.toString())
                    .build()
                    .execute(object : StringCallback() {
                        override fun onResponse(response: String?, id: Int) {
                            val jsonObject = Gson().fromJson(response, JsonObject::class.java).asJsonObject
                            when (jsonObject.get("errorCode").asInt) {
                                0 -> {
                                    val username = StringUtils.getJsonString(jsonObject.get("username").toString())
                                    Toast.makeText(
                                        context,
                                        "欢迎回来，$username!", Toast.LENGTH_LONG
                                    ).show()
                                    SharedPrefsUtils.putValue(context, SavedKeyConst.IS_SIGNED_IN, true)
                                    SharedPrefsUtils.putValue(
                                        context,
                                        SavedKeyConst.SAVED_UID,
                                        StringUtils.getJsonString(jsonObject.get("uid").toString())
                                    )
                                    startActivity(Intent(activity, MainActivity::class.java))
                                    activity?.finish()
                                }
                                1 -> {
                                    Toast.makeText(context, "用户名错误，请重新输入", Toast.LENGTH_LONG).show()
                                    okProgressBar.visibility = View.GONE
                                    signOkBtn.isEnabled = true
                                }
                                2 -> {
                                    Toast.makeText(context, "密码错误，请重新输入", Toast.LENGTH_LONG).show()
                                    okProgressBar.visibility = View.GONE
                                    signOkBtn.isEnabled = true
                                }
                            }
                        }

                        override fun onError(call: Call?, e: java.lang.Exception?, id: Int) {
                            Toast.makeText(context, "网络异常", Toast.LENGTH_LONG).show()
                            okProgressBar.visibility = View.GONE
                            signOkBtn.isEnabled = true
                        }
                    })
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WelcomeSignInNameViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
