package com.scorpios.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class IndexController4 {

    public static final String REDIS_LOCK = "good_lock";

    @Autowired
    StringRedisTemplate template;

    /**
     * 下面使用redis的set命令来实现加锁
     * SET KEY VALUE [EX seconds] [PX milliseconds] [NX|XX]
     *
     * EX seconds − 设置指定的到期时间(以秒为单位)。
     * PX milliseconds - 设置指定的到期时间(以毫秒为单位)。
     * NX - 仅在键不存在时设置键。
     * XX - 只有在键已存在时才设置。
     */
    @RequestMapping("/buy4")
    public String index(){

        // 每个人进来先要进行加锁，key值为"good_lock"
        String value = UUID.randomUUID().toString().replace("-","");
        try{
            Boolean flag = template.opsForValue().setIfAbsent(REDIS_LOCK, value);
            // 加锁失败
            if(!flag){
                return "抢锁失败！";
            }
            System.out.println( value+ " 抢锁成功");
            String result = template.opsForValue().get("goods:001");
            int total = result == null ? 0 : Integer.parseInt(result);
            if (total > 0) {
                int realTotal = total - 1;
                template.opsForValue().set("goods:001", String.valueOf(realTotal));
                // 如果在抢到所之后，删除锁之前，发生了异常，锁就无法被释放，所以要在finally处理
//                template.delete(REDIS_LOCK);
                System.out.println("购买商品成功，库存还剩：" + realTotal + "件， 服务端口为8001");
                return "购买商品成功，库存还剩：" + realTotal + "件， 服务端口为8001";
            } else {
                System.out.println("购买商品失败，服务端口为8001");
            }
            return "购买商品失败，服务端口为8001";
        }finally {
            template.delete(REDIS_LOCK);
        }

    }
}
