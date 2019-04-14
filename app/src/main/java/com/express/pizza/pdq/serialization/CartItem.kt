package com.express.pizza.pdq.serialization

import com.express.pizza.pdq.entity.Pizza
import java.io.Serializable

class CartItem(var map: LinkedHashMap<Pizza, Int>) : Serializable