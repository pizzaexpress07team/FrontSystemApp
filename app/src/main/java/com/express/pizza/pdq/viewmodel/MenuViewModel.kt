package com.express.pizza.pdq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.express.pizza.pdq.entity.Pizza
import com.express.pizza.pdq.repository.PizzaRepository
import java.util.*
import kotlin.collections.LinkedHashMap

class MenuViewModel : ViewModel() {
    private val pizzaRepository = PizzaRepository()
    var pizzaList: LiveData<ArrayList<Pizza>>? = null
    var cartItemMap = LinkedHashMap<Pizza, Int>()

    init {
        pizzaList = pizzaRepository.getPizzaList()
    }
}
