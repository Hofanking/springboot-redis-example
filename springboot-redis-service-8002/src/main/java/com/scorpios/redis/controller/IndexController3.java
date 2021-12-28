package com.scorpios.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController3 {

    @Autowired
    StringRedisTemplate template;

    @RequestMapping("/buy3")
    public String index(){
        synchronized (this) {
            String result = template.opsForValue().get("goods:001");
            int total = result == null ? 0 : Integer.parseInt(result);
            if (total > 0) {
                int realTotal = total - 1;
                template.opsForValue().set("goods:001", String.valueOf(realTotal));
                System.out.println("购买商品成功，库存还剩：" + realTotal + "件， 服务端口为8002");
                return "购买商品成功，库存还剩：" + realTotal + "件， 服务端口为8002";
            } else {
                System.out.println("购买商品失败，服务端口为8002");
            }
            return "购买商品失败，服务端口为8002";
        }
    }
}