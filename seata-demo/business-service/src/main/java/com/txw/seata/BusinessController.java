package com.txw.seata;

import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
public class BusinessController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessController.class);
    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";
    private static final String USER_ID = "U100001";
    private static final String COMMODITY_CODE = "C00321";
    private static final int ORDER_COUNT = 2;
    private final RestTemplate restTemplate;
    private final BusinessApplication.OrderService orderService;
    private final BusinessApplication.StorageService storageService;
    public BusinessController(RestTemplate restTemplate, BusinessApplication.OrderService orderService,
                              BusinessApplication.StorageService storageService) {
        this.restTemplate = restTemplate;
        this.orderService = orderService;
        this.storageService = storageService;
    }
    @RequestMapping(value = "/seata/mockOrderSuccess", method = RequestMethod.GET, produces = "application/json")
    public String mockOrderSuccess() {
        // 调用库存服务，去扣减库存
        String result = storageService.storage(COMMODITY_CODE, ORDER_COUNT);
        if (!SUCCESS.equals(result)) {
            throw new RuntimeException();
        }
        result = orderService.order(USER_ID, COMMODITY_CODE, ORDER_COUNT, false);
        if (!SUCCESS.equals(result)) {
            throw new RuntimeException();
        }
        return SUCCESS;
    }
    @RequestMapping(value = "/seata/mockOrderFail", method = RequestMethod.GET, produces = "application/json")
    @GlobalTransactional(timeoutMills = 70000,rollbackFor = Exception.class)
    // @Transactional(rollbackFor = )
    public String mockOrderFail() {
        String result = storageService.storage(COMMODITY_CODE, ORDER_COUNT);
        if (!SUCCESS.equals(result)) {
            throw new RuntimeException();
        }
        result = orderService.order(USER_ID, COMMODITY_CODE, ORDER_COUNT, true);
        if (!SUCCESS.equals(result)) {
            throw new RuntimeException();
        }
        return SUCCESS;
    }
}