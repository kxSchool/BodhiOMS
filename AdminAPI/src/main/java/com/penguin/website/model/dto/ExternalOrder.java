package com.penguin.website.model.dto;

import java.math.BigDecimal;
 /**
   * create by: mlshao
   * description: 订单接口请求体
   */
public class ExternalOrder {
    private String order_no;
    private BigDecimal saro_quantity;
    private String user_token;

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public BigDecimal getSaro_quantity() {
        return saro_quantity;
    }

    public void setSaro_quantity(BigDecimal saro_quantity) {
        this.saro_quantity = saro_quantity;
    }

     public String getUser_token() {
         return user_token;
     }

     public void setUser_token(String user_token) {
         this.user_token = user_token;
     }
 }
