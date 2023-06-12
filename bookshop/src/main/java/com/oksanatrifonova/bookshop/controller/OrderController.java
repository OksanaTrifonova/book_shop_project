package com.oksanatrifonova.bookshop.controller;

import com.oksanatrifonova.bookshop.component.Cart;
import com.oksanatrifonova.bookshop.dto.AppUserDto;
import com.oksanatrifonova.bookshop.dto.ItemDto;

import com.oksanatrifonova.bookshop.dto.BookOrderDto;
import com.oksanatrifonova.bookshop.entity.Item;
import com.oksanatrifonova.bookshop.entity.BookOrder;
import com.oksanatrifonova.bookshop.entity.AppUser;
import com.oksanatrifonova.bookshop.mapper.ItemMapper;

import com.oksanatrifonova.bookshop.mapper.AppUserMapper;
import com.oksanatrifonova.bookshop.service.AppOrderService;
import com.oksanatrifonova.bookshop.service.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
public class OrderController {
    private final ItemMapper itemMapper;
    private final AppUserServiceImpl userService;
    private final Cart cart;
    private AppUserMapper userMapper;
    private AppOrderService orderService;


    @PostMapping("/place-order")
    public String placeOrder(Model model, Principal principal) {
        if (principal == null) {
            model.addAttribute("message", "You must be logged in to place an order");
            return "login";
        }
        String email = principal.getName();
        AppUser existingUser = userService.findUserByEmail(email);
        if (StringUtils.isEmpty(existingUser.getAddress()) || StringUtils.isEmpty(existingUser.getPhoneNumber())) {
            model.addAttribute("message", "Please fill in your address and phone number in your profile before placing an order.");
            AppUser user =userService.getCurrentUser();

            AppUserDto userDto = userMapper.mapToUserDto(user) ;
            model.addAttribute("personalDetails", userDto);
            return "personal-details";
        }
        List<ItemDto> orderItemsDTO = cart.getCart();
        List<Item> orderItems = itemMapper.convertToEntityList(orderItemsDTO);
        BookOrder order = orderService.createOrder(existingUser, orderItems, cart);
        cart.removeAllItems();
        model.addAttribute("order", order);
        return "order-confirmation";
    }


    @GetMapping("/account/orders")
    public String viewOrders(Model model, Principal principal) {
        String email = principal.getName();
        AppUser user = userService.findUserByEmail(email);
        List<BookOrderDto> orderDtoList = orderService.getOrdersForUser(user);
        model.addAttribute("orderDetailsList", orderDtoList);
        return "order-list";
    }

    @GetMapping("/account/order/{orderId}")
    public String viewOrderDetails(@PathVariable("orderId") Long orderId, Model model) {
        // Retrieve order details based on the orderId
        BookOrderDto orderDto = orderService.getOrderById(orderId);
        model.addAttribute("orderDto", orderDto);
        return "order-list-details";
    }

}

