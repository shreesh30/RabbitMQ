package com.engineering.rabbitmq.model;

public class Order {
    private int id;
    private String item;

    public Order(){}

    public Order(int id, String item){
        this.id = id;
        this.item=item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", item='" + item + '\'' +
                '}';
    }
}
