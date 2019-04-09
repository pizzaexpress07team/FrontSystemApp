package com.express.pizza.pdq.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.express.pizza.pdq.R
import com.express.pizza.pdq.viewmodel.OrderViewModel

class OrderFragment : Fragment() {

    companion object {
        fun newInstance() = OrderFragment()
        const val TAG = "OrderFragment"
    }

    private lateinit var viewModel: OrderViewModel

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
    }

}
