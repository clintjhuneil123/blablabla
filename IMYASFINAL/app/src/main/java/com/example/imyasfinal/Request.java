package com.example.imyasfinal;

import java.util.List;

public class Request {
    private String contanct;
    private String name;
    private String address;
    private String total;
    private String status;
    private List<Order> details;

    public Request() {
    }

    public Request(String contanct, String name, String address, String total, List<Order> details) {
        this.contanct = contanct;
        this.name = name;
        this.address = address;
        this.total = total;
        this.details = details;
        this.status = "0";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContanct() {
        return contanct;
    }

    public void setContanct(String contanct) {
        this.contanct = contanct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getDetails() {
        return details;
    }

    public void setDetails(List<Order> details) {
        this.details = details;
    }
}
