package com.express.pizza.pdq.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.express.pizza.pdq.R
import com.express.pizza.pdq.viewmodel.MenuShowViewModel

class MenuShowFragment : Fragment() {

    companion object {
        fun newInstance() = MenuShowFragment()
    }

    private lateinit var viewModel: MenuShowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.menu_show_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MenuShowViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
