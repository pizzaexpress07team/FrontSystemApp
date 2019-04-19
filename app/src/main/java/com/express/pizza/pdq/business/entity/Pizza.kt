package com.express.pizza.pdq.business.entity

import java.io.Serializable

class Pizza : Serializable {
    var f_id: String? = null
    var is_empty: Boolean? = null
    var p_id: String? = null
    var p_name: String? = null
    var p_picture: String? = null
    var p_size: String? = null
    var p_type: String? = null
    var price: Double? = null
    var resource: String? = null


    override fun hashCode(): Int {
        return p_id?.hashCode() ?: 0
    }

    override fun equals(other: Any?): Boolean {
        return other.hashCode() == hashCode()
    }
}