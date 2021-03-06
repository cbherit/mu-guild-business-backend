package com.chars.muguildbusiness.model.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chars.muguildbusiness.dto.ItemResponse;
import com.chars.muguildbusiness.dto.OrderRequest;
import com.chars.muguildbusiness.dto.OrderResponse;
import com.chars.muguildbusiness.model.entity.Item;
import com.chars.muguildbusiness.model.entity.ItemCategory;
import com.chars.muguildbusiness.model.entity.Order;
import com.chars.muguildbusiness.model.entity.Usuario;
import com.chars.muguildbusiness.model.repository.IOrderRepository;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private IOrderRepository orderRepository;
	@Autowired
	private ItemService itemService;
	@Autowired
	private UserService userService;
	@Autowired
	private ItemCategoryService itemCategoryService;

	@Override
	@Transactional(readOnly = true)
	public OrderResponse findById(Long id) {
		Order order = orderRepository.findById(id).orElse(null);
		return mapToDto(order);
	}

	@Override
	@Transactional
	public void save(OrderRequest orderRequest, String username) {
		Usuario user = userService.findByUsername(username);	
		
		Order order = new Order();		
		order = mapToEntity(orderRequest, order);
		
		order.setCreated(Instant.now());
		order.setEnabled(true);
		order.setUser(user);
		
		orderRepository.save(order);		
	}

	@Override
	@Transactional
	public void edit(OrderRequest orderRequest, String username) {
		Usuario user = userService.findByUsername(username);		
		Order order = orderRepository.findById(orderRequest.getId()).orElse(null);
		
		if(user == order.getUser()) {
			orderRepository.save(mapToEntity(orderRequest, order));			
		}
	}
	
	private Order mapToEntity(OrderRequest orderRequest, Order order) {
		order.setItem_level(orderRequest.getItemLevel());
		order.setItem_options(orderRequest.getItemOption());
		order.setItem_type(orderRequest.getItemType());
		order.setObservation(orderRequest.getObservation());
		order.setOrder_id(orderRequest.getId());

		Item item = itemService.findById(orderRequest.getItemId());
		order.setItem(item);
		
		return order;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<OrderResponse> findAll(Pageable pageable) {	
		return orderRepository.findByEnabledTrueOrderByCreatedDesc(pageable)
				.map(this::mapToDto);		
	}
	
	private OrderResponse mapToDto(Order order) {
		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setId(order.getOrder_id());
		orderResponse.setItem(mapToDto(order.getItem()));
		orderResponse.setCreatedAt(order.getCreated());
		orderResponse.setItemLevel(order.getItem_level());
		orderResponse.setItemOption(order.getItem_options());
		orderResponse.setItemType(order.getItem_type());
		orderResponse.setNickname(order.getUser().getNickname());
		orderResponse.setObservation(order.getObservation());
		
		return orderResponse;
	}
	
	private ItemResponse mapToDto(Item item) {
		ItemResponse itemResponse = new ItemResponse();
		itemResponse.setId(item.getItem_id());
		itemResponse.setName(item.getName());
		
		return itemResponse;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<OrderResponse> findAllByItemName(String itemName, Pageable pageable) {
		Item item = itemService.findByName(itemName);
		
		return orderRepository.findByEnabledTrueAndItemOrderByCreatedDesc(item, pageable)
				.map(this::mapToDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<OrderResponse> findAllByItemCategoryName(String itemCategoryName, Pageable pageable) {
		ItemCategory itemCategory = itemCategoryService.findByName(itemCategoryName);
		
		return orderRepository.findByEnabledTrueAndItemItemCategoryOrderByCreatedDesc(itemCategory, pageable)
				.map(this::mapToDto);
	}

	@Override
	@Transactional
	public void delete(Long id, String username) {
		Usuario user = userService.findByUsername(username);
		Order order = orderRepository.findById(id).orElse(null);
		
		if (user == order.getUser()) {
			orderRepository.delete(order);			
		}
	}

}
