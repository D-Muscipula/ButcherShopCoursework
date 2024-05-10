package edu.java.shop.scheduler;

import edu.java.shop.domain.entity.OrderStatus;
import edu.java.shop.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class Scheduler {
    private final OrderService orderService;
    @Scheduled(fixedDelayString = "100000")
    public void update() {
        log.info("it works");
        orderService.changeOldOrders(3000L, OrderStatus.NEW);
    }
}
