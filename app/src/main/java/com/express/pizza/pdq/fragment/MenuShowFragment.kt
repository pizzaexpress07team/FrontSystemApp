package com.express.pizza.pdq.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.express.pizza.pdq.R
import com.express.pizza.pdq.adapter.PizzaAdapter
import com.express.pizza.pdq.entity.Pizza
import com.express.pizza.pdq.viewmodel.MenuShowViewModel
import kotlinx.android.synthetic.main.menu_show_fragment.*
import java.util.*


class MenuShowFragment : Fragment() {

    companion object {
        fun newInstance() = MenuShowFragment()
    }

    private lateinit var viewModel: MenuShowViewModel
    private lateinit var pizzaAdapter: PizzaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.menu_show_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuRecyclerView.layoutManager = GridLayoutManager(context, 2)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MenuShowViewModel::class.java)
        viewModel.pizzaList?.observe(this, Observer<ArrayList<Pizza>> {
            it?.apply {
                pizzaAdapter = PizzaAdapter(this@MenuShowFragment.context, this)
                menuRecyclerView.adapter = pizzaAdapter
            }
        })

    }
}
