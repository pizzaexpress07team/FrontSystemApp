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
import androidx.navigation.Navigation
import com.express.pizza.pdq.R
import com.express.pizza.pdq.activity.MainActivity
import com.express.pizza.pdq.viewmodel.WelcomeSignUpPhoneViewModel
import kotlinx.android.synthetic.main.welcome_sign_up_phone_fragment.*


class WelcomeSignUpPhoneFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = WelcomeSignUpPhoneFragment()
    }

    private lateinit var welcomeSignUpPhoneViewModel: WelcomeSignUpPhoneViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.welcome_sign_up_phone_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView() {
        (activity as AppCompatActivity).let {
            it.setSupportActionBar(toolbarSignUp)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        switchToSignUpNameBtn.setOnClickListener(this)
        signUpOkBtn.setOnClickListener(this)
        sendCodeBtn.setOnClickListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        welcomeSignUpPhoneViewModel = ViewModelProviders.of(this).get(WelcomeSignUpPhoneViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.switchToSignUpNameBtn -> {
                Navigation.findNavController(p0).navigate(R.id.actionPhoneToSignUpNameFragment)
            }
            R.id.signUpOkBtn -> {
                when {
                    phoneEditText.length() < 11 -> {
                        Toast.makeText(activity, "手机号必须为11位", Toast.LENGTH_LONG).show()
                        return
                    }
                    codeEditText.length() != 4 -> {
                        Toast.makeText(activity, "验证码为4位数字，请确认", Toast.LENGTH_LONG).show()
                        return
                    }
                    passwordEditText.length() < 8 -> {
                        Toast.makeText(activity, "密码至少为8位，请确认", Toast.LENGTH_LONG).show()
                        return
                    }
                    else -> {
                        startActivity(Intent(activity, MainActivity::class.java))
                        activity?.finish()
                    }
                }
            }
            R.id.sendCodeBtn -> {
                if (phoneEditText.length() < 11) {
                    Toast.makeText(activity, "手机号必须为11位，请确认", Toast.LENGTH_LONG).show()
                    return
                }
            }
        }
    }
}
