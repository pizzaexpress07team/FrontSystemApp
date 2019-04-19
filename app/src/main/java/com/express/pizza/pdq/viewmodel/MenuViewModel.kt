package com.express.pizza.pdq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.express.pizza.pdq.business.entity.Pizza
import com.express.pizza.pdq.repository.PizzaRepository
import java.util.*
import kotlin.collections.LinkedHashMap

class MenuViewModel : ViewModel() {
    private val pizzaRepository = PizzaRepository()
    var pizzaList: LiveData<ArrayList<Pizza>>? = null
    var cartItemMap = LinkedHashMap<Pizza, Int>()
    var typeList = TreeSet<String>()

    init {
        refreshList()
    }

    fun refreshList() {
        pizzaList = pizzaRepository.getPizzaList()
        pizzaList?.value?.apply {
            if (!isCartEmpty()) {
                for (pizza in cartItemMap.keys) {
                    if (!contains(pizza)) {
                        cartItemMap.remove(pizza)
                    }
                }
            }
            //todo 分类
//            for (pizza in this) {
//
//            }

        }
    }

    fun getTotalPrice(): Double {
        var total = 0.0
        for (entry in cartItemMap) {
            total += entry.key.price?.times(entry.value) ?: 0.0
        }
        return total
    }

    fun getTotalCount(): Int {
        var count = 0
        for (entry in cartItemMap) {
            count += entry.value
        }
        return count
    }

    fun isCartEmpty(): Boolean {
        return cartItemMap.isEmpty()
    }
}
