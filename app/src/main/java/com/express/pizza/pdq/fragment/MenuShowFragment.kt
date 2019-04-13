package com.express.pizza.pdq.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.express.pizza.pdq.R
import com.express.pizza.pdq.activity.ItemDetailsActivity
import com.express.pizza.pdq.adapter.PizzaAdapter
import com.express.pizza.pdq.callback.ItemContentClickListener
import com.express.pizza.pdq.callback.ItemCountClickListener
import com.express.pizza.pdq.entity.Pizza
import com.express.pizza.pdq.viewmodel.MenuViewModel
import kotlinx.android.synthetic.main.menu_show_fragment.*
import java.math.BigDecimal

class MenuShowFragment : Fragment(), ItemCountClickListener, ItemContentClickListener {

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

        initView()
    }

    private fun initView() {
        pizzaAdapter = PizzaAdapter(context, ArrayList())
        pizzaAdapter.itemCountClickListener = this
        pizzaAdapter.itemContentClickListener = this
        menuRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = pizzaAdapter
            hasFooter = true
            itemAnimator?.changeDuration = 0
        }
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
                refreshFab()
            }
        })
    }

    override fun onCountIncreaseClicked(pizza: Pizza, position: Int) {
        if (viewModel.cartItemMap.containsKey(pizza)) {
            viewModel.cartItemMap.apply {
                put(pizza, get(pizza)!! + 1)
            }
        } else {
            viewModel.cartItemMap[pizza] = 1
        }
        pizzaAdapter.cartMap = viewModel.cartItemMap
        pizzaAdapter.notifyItemChanged(position)
        refreshFab()
    }

    override fun onCountDecreaseClicked(pizza: Pizza, position: Int) {
        viewModel.cartItemMap.apply {
            // 这类餐品的数量
            val count = get(pizza)
            count?.also {
                if (it == 1) {
                    remove(pizza)
                } else {
                    put(pizza, get(pizza)!! - 1)
                }
            }
        }
        pizzaAdapter.cartMap = viewModel.cartItemMap
        pizzaAdapter.notifyItemChanged(position)
        refreshFab()
    }

    override fun onItemClicked(view: View, pizza: Pizza) {
        val intent = Intent(activity, ItemDetailsActivity::class.java)
        intent.apply {
            putExtra(ItemDetailsActivity.DETAILS_IMG, pizza.p_picture)
            putExtra(ItemDetailsActivity.DETAILS_NAME, pizza.p_name)
            putExtra(ItemDetailsActivity.DETAILS_SIZE, pizza.p_size)
            putExtra(ItemDetailsActivity.DETAILS_PRICE, pizza.price)
        }
        val participant = Pair(view, ViewCompat.getTransitionName(view))
        val transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity as Activity, participant
        )
        startActivity(intent, transitionActivityOptions.toBundle())
    }

    override fun onResume() {
        super.onResume()
        pizzaAdapter.cartMap = viewModel.cartItemMap
        refreshFab()
        pizzaAdapter.notifyDataSetChanged()
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom)
        recyclerView.layoutAnimation = controller
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    @SuppressLint("SetTextI18n")
    private fun refreshFab() {
        totalCount.apply {
            visibility = if (viewModel.isCartEmpty()) View.GONE else View.VISIBLE
            text = viewModel.getTotalCount().toString()
        }
        totalPrice.apply {
            visibility = if (viewModel.isCartEmpty()) View.GONE else View.VISIBLE
            text = "¥${BigDecimal(viewModel.getTotalPrice()).stripTrailingZeros().toPlainString()}"
        }
    }

}
