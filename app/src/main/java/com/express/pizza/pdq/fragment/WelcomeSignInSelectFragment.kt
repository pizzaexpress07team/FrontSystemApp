package com.express.pizza.pdq.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.express.pizza.pdq.R
import com.express.pizza.pdq.viewmodel.SignInSelectViewModel
import kotlinx.android.synthetic.main.welcome_sign_in_select_fragment.*

class WelcomeSignInSelectFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() = WelcomeSignInSelectFragment()
    }

    private lateinit var viewModel: SignInSelectViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.welcome_sign_in_select_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        (activity as AppCompatActivity).let {
            it.setSupportActionBar(toolbarSignInSelect)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        signInByNameBtn.setOnClickListener(this)
        signInByCodeBtn.setOnClickListener(this)
        signInByPhoneBtn.setOnClickListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignInSelectViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.signInByNameBtn -> {
                Navigation.findNavController(p0).navigate(R.id.actionSignInSelectToSignInNameFragment)
            }
            R.id.signInByCodeBtn -> {
                Navigation.findNavController(p0).navigate(R.id.actionSignInSelectToSignInCodeFragment)
            }
            R.id.signInByPhoneBtn -> {
                Navigation.findNavController(p0).navigate(R.id.actionSignInSelectToSignInPhoneFragment)
            }
        }
    }
}
