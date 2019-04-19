package com.express.pizza.pdq.serialization

import com.express.pizza.pdq.business.entity.Pizza
import java.io.Serializable

class CartItem(var map: LinkedHashMap<Pizza, Int>) : Serializable