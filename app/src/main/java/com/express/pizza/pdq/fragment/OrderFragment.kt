package com.express.pizza.pdq.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.express.pizza.pdq.R
import com.express.pizza.pdq.activity.OrderDetailActivity
import com.express.pizza.pdq.adapter.OrderAdapter
import com.express.pizza.pdq.business.entity.Order
import com.express.pizza.pdq.callback.ItemClickListener
import com.express.pizza.pdq.utils.SavedKeyConst
import com.express.pizza.pdq.utils.SharedPrefsUtils
import com.express.pizza.pdq.utils.UrlConst
import com.express.pizza.pdq.viewmodel.OrderViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import kotlinx.android.synthetic.main.order_fragment.*
import okhttp3.Call

class OrderFragment : Fragment(), ItemClickListener {

    companion object {
        fun newInstance() = OrderFragment()
        const val TAG = "OrderFragment"
        var URL_VIEW_ALL_ORDER = UrlConst.URL_PRE + "/order/view"
    }

    private lateinit var viewModel: OrderViewModel
    private var orderList = ArrayList<Order>()
    lateinit var adapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OrderViewModel::class.java)
        // TODO: Use the ViewModel
        initView()
    }

    private fun initView() {
        adapter = OrderAdapter(context, orderList)
        orderRecyclerView.adapter = adapter
        orderRecyclerView.layoutManager = LinearLayoutManager(activity)
        refresh()
        adapter.itemClickListener = this

        orderSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.itemAdd, R.color.toolbarTitle)
        orderSwipeRefresh.setOnRefreshListener {
            refresh()
        }
    }

    private fun refresh() {
        OkHttpUtils
            .get()
            .url(URL_VIEW_ALL_ORDER)
            .addParams("uid", SharedPrefsUtils.getValue(activity, SavedKeyConst.SAVED_UID, "-1"))
            .build()
            .execute(object : StringCallback() {
                override fun onResponse(response: String?, id: Int) {
                    val gson = Gson()
                    val jsonObject = gson.fromJson(response, JsonObject::class.java).asJsonObject
                    val listType = object : TypeToken<ArrayList<Order>>() {}.type
                    orderList = gson.fromJson(jsonObject.get("list"), listType)
                    Log.d("order--", orderList.size.toString())
                    adapter.list = orderList
                    adapter.notifyDataSetChanged()
                    if (orderList.isEmpty()) {
                        orderEmptyImg.visibility = View.VISIBLE
                        orderEmptyText.visibility = View.VISIBLE
                        orderRecyclerView.visibility = View.GONE
                    } else {
                        orderEmptyImg.visibility = View.GONE
                        orderEmptyText.visibility = View.GONE
                        orderRecyclerView.visibility = View.VISIBLE
                    }
                    orderSwipeRefresh.isRefreshing = false
                }

                override fun onError(call: Call?, e: java.lang.Exception?, id: Int) {
                    Toast.makeText(activity, "网络异常，加载失败", Toast.LENGTH_LONG).show()
                    orderSwipeRefresh.isRefreshing = false
                }
            })
    }

    override fun onItemClicked(view: View, pos: Int) {
        val intent = Intent(activity, OrderDetailActivity::class.java)
        intent.putExtra(OrderDetailActivity.ORDER_ID, orderList[pos].o_id)
        startActivity(intent)
    }

}
