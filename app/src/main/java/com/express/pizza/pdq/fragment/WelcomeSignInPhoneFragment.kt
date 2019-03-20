package com.express.pizza.pdq.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.express.pizza.pdq.R
import com.express.pizza.pdq.viewmodel.WelcomeSignInPhoneViewModel
import kotlinx.android.synthetic.main.welcome_sign_in_phone_fragment.*

class WelcomeSignInPhoneFragment : Fragment() {

    companion object {
        fun newInstance() = WelcomeSignInPhoneFragment()
    }

    private lateinit var viewModel: WelcomeSignInPhoneViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.welcome_sign_in_phone_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        (activity as AppCompatActivity).let {
            it.setSupportActionBar(toolbarSignInPhone)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WelcomeSignInPhoneViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
