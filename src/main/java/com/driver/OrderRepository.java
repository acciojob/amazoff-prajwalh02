package com.driver;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderDetails = new HashMap<>();
    HashMap<String, DeliveryPartner> partnerDetails = new HashMap<>();
    HashMap<String, String> orderToPartner = new HashMap<>();   //contains order and partner
    HashMap<String, List<String>> partnerToOrder = new HashMap<>();  //contains list of all order that partner have


    public void addOrder(Order order) {
        orderDetails.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        partnerDetails.put(partnerId, new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {

        if (!partnerToOrder.containsKey(partnerId)) {     //if partner is not present then add to the map
            partnerToOrder.put(partnerId, new ArrayList<>());
        }
        partnerToOrder.get(partnerId).add(orderId);     //Assign an order to a partner
        partnerDetails.get(partnerId).setNumberOfOrders(partnerDetails.get(partnerId).getNumberOfOrders() + 1);
        orderToPartner.put(orderId, partnerId);
    }

    public Order getOrderById(String orderId) {
        return orderDetails.get(orderId);
    }
    public DeliveryPartner getPartnerById(String partnerId) {
        return partnerDetails.get(partnerId);
    }
    public Integer getOrderCountByPartnerId(String partnerId) {
        return partnerDetails.get(partnerId).getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerToOrder.get(partnerId);
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orderDetails.keySet());
    }


    public Integer getCountOfUnassignedOrders() {
        return orderDetails.size() - orderToPartner.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int intTime, String partnerId) {
        int count = 0;

        List<String> ordersToCheck = partnerToOrder.get(partnerId);
        for(String orderID : ordersToCheck) {
            if(orderDetails.get(orderID).getDeliveryTime() > intTime) {
                count++;
            }
        }
        return count;
    }

    public int getLastDeliveryTimeByPartnerId(String partnerId) {
        int lastOrderTime = 0;

        List<String> ordersOfPartner = partnerToOrder.get(partnerId);
        for(String orderId : ordersOfPartner) {
            if(orderDetails.get(orderId).getDeliveryTime()>lastOrderTime){
                lastOrderTime = orderDetails.get(orderId).getDeliveryTime();
            }
        }

        return lastOrderTime;
    }

    public void deletePartnerById(String partnerId) {
        List<String> ordersOFPartner = partnerToOrder.get(partnerId);
        for(String orderID : ordersOFPartner) {
            orderToPartner.remove(orderID);
        }
        partnerToOrder.remove(partnerId);
        partnerDetails.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {

        String partnerID = orderToPartner.get(orderId);
        partnerToOrder.get(partnerID).remove(orderId);
        partnerDetails.get(partnerID).setNumberOfOrders(partnerDetails.get(partnerID).getNumberOfOrders() - 1);
        orderToPartner.remove(orderId);
        orderDetails.remove(orderId);
    }
}
