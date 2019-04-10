package com.express.pizza.pdq.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.express.pizza.pdq.R
import com.express.pizza.pdq.adapter.PizzaAdapter
import com.express.pizza.pdq.entity.Pizza
import com.express.pizza.pdq.viewmodel.MenuViewModel
import kotlinx.android.synthetic.main.menu_show_fragment.*


class MenuShowFragment : Fragment(), PizzaAdapter.AddClickListener {

    companion object {
        fun newInstance() = MenuShowFragment()
    }

    private lateinit var viewModel: MenuViewModel
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
        pizzaAdapter.addClickListener = this
        menuRecyclerView.adapter = pizzaAdapter
        menuRecyclerView.hasFooter = true
        menuCartFab.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.actionMenuShowToMenuCart)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("MenuShow--", "onActivityCreated")

        viewModel = ViewModelProviders.of(this.parentFragment!!).get(MenuViewModel::class.java)
        viewModel.pizzaList?.observe(this, Observer<ArrayList<Pizza>> {
            it?.apply {
                pizzaAdapter.list = this
                pizzaAdapter.footer =
                    LayoutInflater.from(context).inflate(R.layout.item_pizza_footer, menuRecyclerView, false)
                runLayoutAnimation(menuRecyclerView)
            }
        })
    }

    override fun onAddClicked(pizza: Pizza) {
        if (viewModel.cartItemMap.containsKey(pizza)) {
            viewModel.cartItemMap.apply {
                put(pizza, get(pizza)!! + 1)
            }
        } else {
            viewModel.cartItemMap[pizza] = 1
        }
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom)
        recyclerView.layoutAnimation = controller
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }
}
