package com.tinecommerce.core.web.controller;

import com.tinecommerce.core.cart.ArchivalProduct;
import com.tinecommerce.core.cart.Order;
import com.tinecommerce.core.cart.OrderItem;
import com.tinecommerce.core.cart.repo.OrderRepository;
import com.tinecommerce.core.catalog.model.MediaAsset;
import com.tinecommerce.core.catalog.model.Price;
import com.tinecommerce.core.catalog.model.Product;
import com.tinecommerce.core.catalog.repository.ProductRepository;
import com.tinecommerce.core.customer.model.Customer;
import com.tinecommerce.core.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class CartController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/customer/cart/show")
    public String showCart(Model model){
        String auth = getLoggedInUsername();
        Customer customer = customerRepository.findByUsernameOrEmail(auth,auth).get();
        Order order = orderRepository.findByCustomerAndOrderStatus(customer, "CART");
        if (order != null) {
            model.addAttribute("orderItems", order.getOrderItems());
            model.addAttribute("price",String.format("%10.2f", order.getOrderItems().stream()
                    .mapToDouble(orderItem -> orderItem.getArchivalProduct().getPrice() * orderItem.getQuantity()).sum()));
        } else {
            model.addAttribute("price", 0.0);
        }

        return "cart";
    }

    @GetMapping("/customer/cart/delete/{code}")
    @Transactional
    public String deleteFromCart(Model model, @PathVariable(name = "code") String code){
        String auth = getLoggedInUsername();
        Customer customer = customerRepository.findByUsernameOrEmail(auth,auth).get();
        Order order = orderRepository.findByCustomerAndOrderStatus(customer, "CART");
        order.getOrderItems()
                .stream()
                .filter(orderItem -> orderItem.getArchivalProduct().getAttributes().get("code").equals(code))
                .findFirst().ifPresent(orderItem -> {
                    orderItem.setOrder(null);
                    order.getOrderItems().remove(orderItem);
                });
        return "redirect:/customer/cart/show";
    }

    @GetMapping("/customer/cart/add/{code}")
    @Transactional
    public String addToCart(Model model, @PathVariable(name = "code") String code) {
        String auth = getLoggedInUsername();
        Customer customer = customerRepository.findByUsernameOrEmail(auth,auth).get();
        Order order = orderRepository.findByCustomerAndOrderStatus(customer, "CART");
        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
        if(order == null) {
            order = new Order();
            order.setCustomer(customer);
            order.setCode(timestamp);
            order.setOrderStatus("CART");
        }
        Product product = productRepository.findByCode(code);
        OrderItem foundOrderItem = order.getOrderItems().stream()
                .filter(orderItem -> orderItem.getArchivalProduct().getAttributes().get("code").split("_")[0].equals(code))
                .findFirst()
                .orElse(null);
        if(foundOrderItem != null) {
            int value = foundOrderItem.getQuantity().intValue();
            value = value + 1;
            foundOrderItem.setQuantity((long) value);
            entityManager.merge(foundOrderItem);
        } else {
            OrderItem newOrderItem = new OrderItem();
            ArchivalProduct archivalProduct = new ArchivalProduct();
            archivalProduct.getAttributes().put("code", code + "_" + timestamp);
            archivalProduct.getAttributes().put("photo", product.getMediaAssets().stream().findFirst().map(MediaAsset::getUrl).orElse(""));
            archivalProduct.getAttributes().put("currency", "PLN");
            archivalProduct.getAttributes().put("name", product.getName());
            archivalProduct.setPrice(product.getPrices().stream().findFirst().map(Price::getAmount).orElse(new BigDecimal(0L)).doubleValue());
            newOrderItem.setQuantity(1L);
            newOrderItem.setOrder(order);
            newOrderItem.setArchivalProduct(archivalProduct);
            entityManager.merge(newOrderItem);
        }
        return "redirect:/customer/cart/show";
    }

    @GetMapping("/customer/cart/buy")
    @Transactional
    public String acceptOrder(Model model) {
        String auth = getLoggedInUsername();
        Customer customer = customerRepository.findByUsernameOrEmail(auth,auth).get();
        Order order = orderRepository.findByCustomerAndOrderStatus(customer, "CART");
        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
        if(order != null) {
            order.setOrderStatus("CONFIRMED");
            order.setOrderNumber(timestamp);
            order.setSubmitDate(LocalDate.now());
            order.setFullAmount(new BigDecimal(order.getOrderItems().stream()
                    .mapToDouble(orderItem -> orderItem.getArchivalProduct().getPrice() * orderItem.getQuantity()).sum()));
        }
        return "redirect:/customer/cart/show";
    }

    public String getLoggedInUsername() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Principal::getName)
                .orElse(null);
    }
}
