package com.scorpios.redis.controller;

import com.scorpios.redis.utils.RedisUtils;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class IndexController8 {

    public static final String REDIS_LOCK = "good_lock";

    @Autowired
    StringRedisTemplate template;

    @Autowired
    Redisson redisson;
    /**
     * 在第六种情况下，规定了谁上的锁，谁才能删除
     *
     * 1. 缓存续命
     *
     * 2. redis异步复制造成的锁丢失：主节点没来得及把刚刚set进来这条数据给从节点，就挂了
     *
     * Redisson
     */
    @RequestMapping("/buy8")
    public String index(){

        RLock lock = redisson.getLock(REDIS_LOCK);
        lock.lock();

        // 每个人进来先要进行加锁，key值为"good_lock"
        String value = UUID.randomUUID().toString().replace("-","");
        try{
            String result = template.opsForValue().get("goods:001");
            int total = result == null ? 0 : Integer.parseInt(result);
            if (total > 0) {
                // 如果在此处需要调用其他微服务，处理时间较长。。。
                int realTotal = total - 1;
                template.opsForValue().set("goods:001", String.valueOf(realTotal));
                System.out.println("购买商品成功，库存还剩：" + realTotal + "件， 服务端口为8001");
                return "购买商品成功，库存还剩：" + realTotal + "件， 服务端口为8001";
            } else {
                System.out.println("购买商品失败，服务端口为8001");
            }
            return "购买商品失败，服务端口为8001";
        }finally {
            if(lock.isLocked() && lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }

    }
}
