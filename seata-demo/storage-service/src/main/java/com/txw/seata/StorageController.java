package com.txw.seata;

import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class StorageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageController.class);
    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";
    private final JdbcTemplate jdbcTemplate;
    public StorageController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    // 扣减库存结构
    @RequestMapping(value = "/storage/{commodityCode}/{count}", method = RequestMethod.GET, produces = "application/json")
    public String echo(@PathVariable String commodityCode, @PathVariable int count) {
        LOGGER.info("扣减库存业务【开始】 ... xid: " + RootContext.getXID());
        int result = jdbcTemplate.update(
                "update t_storage set count = count - ? where commodity_code = ?",
                new Object[]{count, commodityCode});
        LOGGER.info("扣减库存业务【结束】 ... ");
        if (result == 1) {
            return SUCCESS;
        }
        return FAIL;
    }
}