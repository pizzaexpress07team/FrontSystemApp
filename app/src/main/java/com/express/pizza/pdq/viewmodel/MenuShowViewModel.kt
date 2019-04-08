package com.express.pizza.pdq.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.express.pizza.pdq.entity.Pizza
import com.express.pizza.pdq.repository.PizzaRepository

class MenuShowViewModel : ViewModel() {
    private val pizzaRepository = PizzaRepository()
    var pizzaList: LiveData<ArrayList<Pizza>>? = null

    init {
        pizzaList = pizzaRepository.pizzaList
    }

}
