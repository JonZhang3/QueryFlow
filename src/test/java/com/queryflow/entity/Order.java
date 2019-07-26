package com.queryflow.entity;

import com.queryflow.annotation.Column;
import com.queryflow.annotation.Table;

@Table("orders")
public class Order {

    private long id;
    private String name;

    @Column(isDictionaryKey = true)
    private OrderStatus status;

    @Column(value = "settlementStatus", isDictionaryKey = true, dictionaryClass = SettlementStatus.class)
    private String settlementStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", status=" + status +
            ", settlementStatus='" + settlementStatus + '\'' +
            '}';
    }
}
