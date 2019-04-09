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
        Log.d("MenuShow--", "onCreateView")
        return inflater.inflate(R.layout.menu_show_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MenuShow--", "onViewCreated")

        menuRecyclerView.layoutManager = GridLayoutManager(context, 2)
        pizzaAdapter = PizzaAdapter(context, ArrayList())
        menuRecyclerView.adapter = pizzaAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("MenuShow--", "onActivityCreated")

        viewModel = ViewModelProviders.of(this).get(MenuShowViewModel::class.java)
        viewModel.pizzaList?.observe(this, Observer<ArrayList<Pizza>> {
            it?.apply {
                pizzaAdapter.list = this
                pizzaAdapter.notifyDataSetChanged()
                pizzaAdapter.footer =
                    LayoutInflater.from(context).inflate(R.layout.item_pizza_footer, menuRecyclerView, false)
            }
        })

    }
}
