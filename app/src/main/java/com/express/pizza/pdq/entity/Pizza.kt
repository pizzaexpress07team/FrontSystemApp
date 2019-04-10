package com.express.pizza.pdq.entity

open class Pizza {
    open var f_id: String? = null
    open var is_empty: Boolean? = null
    open var p_id: String? = null
    open var p_name: String? = null
    open var p_picture: String? = null
    open var p_size: String? = null
    open var p_type: String? = null
    open var price: Double? = null

    override fun hashCode(): Int {
        return p_id?.hashCode() ?: 0
    }

    override fun equals(other: Any?): Boolean {
        return other.hashCode() == hashCode()
    }
}