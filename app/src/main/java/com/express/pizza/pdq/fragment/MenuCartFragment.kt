package com.express.pizza.pdq.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.express.pizza.pdq.R
import com.express.pizza.pdq.activity.ItemDetailsActivity
import com.express.pizza.pdq.activity.OrderConfirmActivity
import com.express.pizza.pdq.adapter.CartItemAdapter
import com.express.pizza.pdq.callback.ItemContentClickListener
import com.express.pizza.pdq.callback.ItemCountClickListener
import com.express.pizza.pdq.business.entity.Pizza
import com.express.pizza.pdq.serialization.CartItem
import com.express.pizza.pdq.viewmodel.MenuViewModel
import kotlinx.android.synthetic.main.menu_cart_fragment.*

class MenuCartFragment : Fragment(), ItemCountClickListener, ItemContentClickListener {

    companion object {
        fun newInstance() = MenuCartFragment()
    }

    private lateinit var viewModel: MenuViewModel
    private lateinit var cartItemAdapter: CartItemAdapter
    private lateinit var footer: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.menu_cart_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        (activity as AppCompatActivity).let {
            it.setSupportActionBar(toolbarCart)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        cartItemAdapter = CartItemAdapter(context, LinkedHashMap())
        cartItemAdapter.itemCountClickListener = this
        cartItemAdapter.itemContentClickListener = this
        cartRecyclerView.adapter = cartItemAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(context)
        cartRecyclerView.itemAnimator = DefaultItemAnimator().apply {
            removeDuration = 100
        }
        footer = LayoutInflater.from(context).inflate(R.layout.item_cart_footer, cartRecyclerView, false)
        cartItemAdapter.footer = footer

        checkoutBtn.setOnClickListener {
            val intent = Intent(activity, OrderConfirmActivity::class.java)
            val cartItemMap = CartItem(viewModel.cartItemMap)
            intent.putExtra(OrderConfirmActivity.CART_ITEMS, cartItemMap)
            startActivity(intent)
        }
    }

    private fun refresh() {
        refreshAdapter()
        refreshState()
    }

    @SuppressLint("SetTextI18n")
    private fun refreshState() {
        if (viewModel.isCartEmpty()) {
            cartEmptyImg.visibility = View.VISIBLE
            cartEmptyText.visibility = View.VISIBLE
            checkoutBtn.visibility = View.GONE
            cartScrollView.visibility = View.GONE
        } else {
            cartEmptyImg.visibility = View.GONE
            cartEmptyText.visibility = View.GONE
            cartScrollView.visibility = View.VISIBLE
            checkoutBtn.visibility = View.VISIBLE
            cartCountText.text = "已选择 ${viewModel.cartItemMap.size} 种餐品，共 ${viewModel.getTotalCount()} 份"
            footer.findViewById<TextView>(R.id.totalPrice).text = "¥" + String.format("%.2f", viewModel.getTotalPrice())
        }
        activity?.invalidateOptionsMenu()
    }

    private fun refreshData() {
        cartItemAdapter.setMap(viewModel.cartItemMap)
    }

    private fun refreshAdapter() {
        refreshData()
        cartItemAdapter.notifyDataSetChanged()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this.parentFragment!!).get(MenuViewModel::class.java)
        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.cart_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.getItem(0)?.isEnabled = !viewModel.isCartEmpty()
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.remove_cart -> {
                showClearCartDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCountIncreaseClicked(pizza: Pizza, position: Int) {
        viewModel.cartItemMap.apply {
            put(pizza, get(pizza)!! + 1)
        }
        cartItemAdapter.notifyItemChanged(position)
        refreshState()
    }

    override fun onCountDecreaseClicked(pizza: Pizza, position: Int) {
        viewModel.cartItemMap.apply {
            // 这类餐品的数量
            val count = get(pizza)
            count?.apply {
                if (count == 1) {
                    remove(pizza)
                    refreshData()
                    Log.d("cart--", "$size $position")
                    cartItemAdapter.deleteItem(position)
                } else {
                    put(pizza, get(pizza)!! - 1)
                    cartItemAdapter.notifyItemChanged(position)
                }
            }
            refreshState()
        }
    }

    override fun onItemClicked(view: View, pizza: Pizza) {
        val intent = Intent(activity, ItemDetailsActivity::class.java)
        intent.apply {
            putExtra(ItemDetailsActivity.DETAILS_IMG, pizza.p_picture)
            putExtra(ItemDetailsActivity.DETAILS_NAME, pizza.p_name)
            putExtra(ItemDetailsActivity.DETAILS_SIZE, pizza.p_size)
            putExtra(ItemDetailsActivity.DETAILS_PRICE, pizza.price)
            putExtra(ItemDetailsActivity.DETAILS_RESOURCE, pizza.resource)
        }
        val participant = Pair(view, ViewCompat.getTransitionName(view))
        val transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity as Activity, participant
        )
        startActivity(intent, transitionActivityOptions.toBundle())
    }

    private fun showClearCartDialog() {
        val clearCartDialog = AlertDialog.Builder(context!!)
        clearCartDialog.setMessage("确定要清空购物车吗？")
        clearCartDialog.setPositiveButton(
            "确定"
        ) { _, _ ->
            viewModel.cartItemMap.clear()
            refresh()
        }
        clearCartDialog.setNegativeButton(
            "取消"
        ) { dialog, _ ->
            dialog.dismiss()
        }
        clearCartDialog.show()
    }
}
