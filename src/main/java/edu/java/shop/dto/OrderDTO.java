package edu.java.shop.dto;

import java.util.Date;
import java.util.List;

public record OrderDTO (Long id, List<OrderItemDTO> orderItemDTOS, Date creationDate, Date lastModifiedDate, String status){
}
