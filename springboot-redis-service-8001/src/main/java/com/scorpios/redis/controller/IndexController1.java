package com.scorpios.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController1 {

    /**
     * 最简单的情况，没有加任何的考虑，
     * 即使是单体应用，并发情况下数据一致性都有问题
     * @return
     */
    @Autowired
    StringRedisTemplate template;

    @RequestMapping("/buy1")
    public String index(){
        String result = template.opsForValue().get("goods:001");
        int total = result == null ? 0 : Integer.parseInt(result);
        if(total > 0 ){
            int realTotal = total -1;
            template.opsForValue().set("goods:001",String.valueOf(realTotal));
            System.out.println("购买商品成功，库存还剩："+realTotal +"件， 服务端口为8001");
            return "购买商品成功，库存还剩："+realTotal +"件， 服务端口为8001";
        }else{
            System.out.println("购买商品失败，服务端口为8001");
        }
        return "购买商品失败，服务端口为8001";
    }
}
