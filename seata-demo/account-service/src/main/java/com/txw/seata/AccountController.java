package com.txw.seata;

import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Random;
@RestController
public class AccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    private static final String SUCCESS = "SUCCESS";
    private static final String FAIL = "FAIL";
    private final JdbcTemplate jdbcTemplate;
    private Random random;
    public AccountController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.random = new Random();
    }
    @RequestMapping(value = "/account", method = RequestMethod.POST, produces = "application/json")
    public String account(String userId, int money) {
        LOGGER.info("扣款业务【开始】... xid: " + RootContext.getXID());
        int result = jdbcTemplate.update(
                "update t_account set money = money - ? where user_id = ?",
                new Object[]{money, userId});
        LOGGER.info("扣款业务【结束】 ... ");
        if (result == 1) {
            return SUCCESS;
        }
        return FAIL;
    }
}