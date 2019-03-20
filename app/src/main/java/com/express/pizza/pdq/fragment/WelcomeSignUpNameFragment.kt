package com.express.pizza.pdq.fragment

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation

import com.express.pizza.pdq.R
import com.express.pizza.pdq.activity.MainActivity
import com.express.pizza.pdq.viewmodel.WelcomeSignUpNameViewModel
import kotlinx.android.synthetic.main.welcome_sign_up_name_fragment.*

class WelcomeSignUpNameFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = WelcomeSignUpNameFragment()
    }

    private lateinit var viewModel: WelcomeSignUpNameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.welcome_sign_up_name_fragment, container, false)
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
        switchToSignUpPhoneBtn.setOnClickListener(this)
        signUpOkBtn.setOnClickListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WelcomeSignUpNameViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.switchToSignUpPhoneBtn -> {
                Navigation.findNavController(p0).navigate(R.id.actionNameToSignUpPhoneFragment)
            }
            R.id.signUpOkBtn -> {
                when {
                    nameEditText.length() < 6 -> {
                        Toast.makeText(activity, "用户名至少为6位，请确认", Toast.LENGTH_LONG).show()
                        return
                    }
                    passwordEditText.length() < 8 -> {
                        Toast.makeText(activity, "密码至少为8位，请确认", Toast.LENGTH_LONG).show()
                        return
                    }
                    passwordAgainEditText.text != passwordEditText.text -> {
                        Toast.makeText(activity, "两次密码输入不一致，请确认", Toast.LENGTH_LONG).show()
                        return
                    }
                    else -> {
                        startActivity(Intent(activity, MainActivity::class.java))
                        activity?.finish()
                    }
                }
            }
        }
    }

}
