package com.express.pizza.pdq.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.express.pizza.pdq.R
import com.express.pizza.pdq.adapter.CartItemAdapter
import com.express.pizza.pdq.entity.Pizza
import com.express.pizza.pdq.viewmodel.MenuViewModel
import kotlinx.android.synthetic.main.menu_cart_fragment.*

class MenuCartFragment : Fragment(), CartItemAdapter.CountClickListener {

    companion object {
        fun newInstance() = MenuCartFragment()
    }

    private lateinit var viewModel: MenuViewModel
    private lateinit var cartItemAdapter: CartItemAdapter

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
        cartItemAdapter.countClickListener = this
        cartRecyclerView.adapter = cartItemAdapter
        cartRecyclerView.layoutManager = LinearLayoutManager(context)
        cartRecyclerView.itemAnimator = DefaultItemAnimator().apply {
            removeDuration = 100
        }
    }

    private fun refresh() {
        refreshAdapter()
        refreshState()
    }

    @SuppressLint("SetTextI18n")
    private fun refreshState() {
        if (viewModel.cartItemMap.isEmpty()) {
            cartEmptyImg.visibility = View.VISIBLE
            cartEmptyText.visibility = View.VISIBLE
            checkoutBtn.visibility = View.GONE
            cartScrollView.visibility = View.GONE
        } else {
            cartEmptyImg.visibility = View.GONE
            cartEmptyText.visibility = View.GONE
            cartScrollView.visibility = View.VISIBLE
            checkoutBtn.visibility = View.VISIBLE
            cartCountText.text = "已选择 ${viewModel.cartItemMap.size} 种餐品，共 ${getTotalCount()} 份"
            totalPrice.text = "¥" + String.format("%.2f", getTotalPrice())
        }
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
                    refreshState()
                    Log.d("cart--", "$size $position")
                    cartItemAdapter.deleteItem(position)
                } else {
                    put(pizza, get(pizza)!! - 1)
                    cartItemAdapter.notifyItemChanged(position)
                    refreshState()
                }
            }
        }
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

    private fun getTotalPrice(): Double {
        var total = 0.0
        for (entry in viewModel.cartItemMap) {
            total += entry.key.price?.times(entry.value) ?: 0.0
        }
        return total
    }

    private fun getTotalCount(): Int {
        var count = 0
        for (entry in viewModel.cartItemMap) {
            count += entry.value
        }
        return count
    }
}
