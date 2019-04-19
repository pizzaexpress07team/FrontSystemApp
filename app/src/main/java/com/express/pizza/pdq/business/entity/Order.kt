package com.express.pizza.pdq.business.entity

class Order(
    var delivery_state: Int,
    var detail: String,
    var f_id: String,
    var o_create_time: Long,
    var o_delivery_addr: String,
    var o_id: String,
    var o_pay_state: Int,
    var o_pay_time: Long,
    var total_price: Double,
    var u_id: String
) {

}