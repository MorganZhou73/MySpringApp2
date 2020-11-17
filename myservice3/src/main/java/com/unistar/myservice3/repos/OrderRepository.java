package com.unistar.myservice3.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unistar.myservice3.model.Order;


public interface OrderRepository extends JpaRepository<Order, Integer>{

}
