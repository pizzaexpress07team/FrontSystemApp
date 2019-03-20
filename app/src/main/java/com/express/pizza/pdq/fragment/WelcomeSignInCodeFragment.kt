package com.express.pizza.pdq.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.express.pizza.pdq.R
import com.express.pizza.pdq.viewmodel.WelcomeSignInCodeViewModel
import kotlinx.android.synthetic.main.welcome_sign_in_code_fragment.*

class WelcomeSignInCodeFragment : Fragment() {

    companion object {
        fun newInstance() = WelcomeSignInCodeFragment()
    }

    private lateinit var viewModel: WelcomeSignInCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.welcome_sign_in_code_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        (activity as AppCompatActivity).let {
            it.setSupportActionBar(toolbarSignInCode)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WelcomeSignInCodeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
