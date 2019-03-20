package com.express.pizza.pdq.fragment

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.express.pizza.pdq.R
import com.express.pizza.pdq.activity.MainActivity
import com.express.pizza.pdq.viewmodel.WelcomeFirstViewModel
import kotlinx.android.synthetic.main.welcome_first_fragment.*

class WelcomeFirstFragment : Fragment(), View.OnClickListener{


    companion object {
        fun newInstance() = WelcomeFirstFragment()
    }

    private lateinit var viewModel: WelcomeFirstViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.welcome_first_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        skipBtn.setOnClickListener(this)
        signUpBtn.setOnClickListener(this)
        signInBtn.setOnClickListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WelcomeFirstViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.skipBtn -> {
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }
            R.id.signUpBtn -> {
                Navigation.findNavController(p0).navigate(R.id.actionFirstToSignUpPhoneFragment)
            }
            R.id.signInBtn -> {
                Navigation.findNavController(p0).navigate(R.id.actionFirstToSignInSelectFragment)
            }
        }
    }

}
