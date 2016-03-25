package com.lqipr.redis;

import com.lqipr.core.redis.RedisPool;
import com.lqipr.core.util.RedisUtil;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lqipr on 2015/10/9.
 */
public class RedisTest {

    /********key-value**********/
    @Test
    public void set(){
        RedisPool rp =  RedisUtil.getRedisPool("test");
        rp.set("test", "123456");
    }

    @Test
    public void get(){
        RedisPool rp =  RedisUtil.getRedisPool("test");
        System.out.println(rp.get("test"));
    }

    /***********list************/
    @Test
    public void setList(){
        RedisPool rp =  RedisUtil.getRedisPool("test");
        for(int i=0;i<100;i++){
            rp.rpush("listTest", String.valueOf(i)); //lpush
        }

    }

    @Test
    public void getList(){
        RedisPool rp =  RedisUtil.getRedisPool("test");
        System.out.println(rp.lrange("listTest", 0, 0));
    }

    @Test
    public void getList_1(){
        RedisPool rp =  RedisUtil.getRedisPool("test");
        System.out.println(rp.rpop("listTest"));
    }

    /***********sortSet************/
    @Test
    public void setSortSet(){
        RedisPool rp =  RedisUtil.getRedisPool("test");
        for(int i=0;i<100;i++){
            rp.zadd("testSortSet", Double.valueOf(i), String.valueOf(i));
        }

    }


    @Test
    public void getSortSet(){
        RedisPool rp =  RedisUtil.getRedisPool("test");
        System.out.println(rp.zrange("testSortSet", 0, 9));
        System.out.println(rp.zrangeByScore("testSortSet", "0", "1000", 0, 10));
        Set<Tuple> set = rp.zrangeByScoreWithScores("testSortSet", "0", "1000", 0, 10);
        for(Tuple t:set){
            System.out.println(t.getElement() + ":" + t.getScore());
        }

    }

    /***********hmap*******/
    @Test
    public void setHmap(){
        RedisPool rp =  RedisUtil.getRedisPool("test");
        rp.hset("testHmap", "col1", "value1");
        rp.hset("testHmap", "col2", "value2");
        rp.hset("testHmap", "col3", "value3");
        rp.hset("testHmap", "col4", "value4");

    }

    @Test
    public void getHmap(){
        RedisPool rp =  RedisUtil.getRedisPool("test");
        System.out.println(rp.hget("testHmap", "col4"));
        System.out.println(rp.hgetAll("testHmap"));

    }

    /***********pipeline-multi*******/
    @Test
    public void pipeline(){
        RedisPool rp = null;
        Jedis jedis = null;
        try{
            rp =  RedisUtil.getRedisPool("test");
            jedis = rp.getJedis();
            Pipeline p = jedis.pipelined();
            Response<List<String>> res_list = p.lrange("listTest", 0, 9);
            Response<Map<String, String>> res_hmap = p.hgetAll("testHmap");
            System.out.println(res_list.get());
            System.out.println(res_hmap.get());
            p.sync();
            System.out.println("-----------sync------------");
            System.out.println(res_list.get());
            System.out.println(res_hmap.get());
            rp.releaseJedisInstance(jedis);
        }catch(Exception e){
            e.printStackTrace();
            if(jedis!=null){
                rp.destoryJedisResource(jedis);
            }
        }
    }

    @Test
    public void getMu(){
        long stime = System.currentTimeMillis();
        List<TestThread> list = new ArrayList<>();
        for(int i=0;i<1;i++){
            TestThread tt = new TestThread();
            tt.run();
            list.add(tt);
        }
        boolean run = true;
        do{
            run = false;
            for(TestThread tt:list){
                if(!tt.end){
                    run = true;
                }
            }
        }while(run);

        long etime = System.currentTimeMillis();

        System.out.println(etime - stime);
    }


    class TestThread extends Thread{
        boolean end = false;
        @Override
        public void run() {
            RedisPool rp =  RedisUtil.getRedisPool("test");
            for(int i=0;i<1000;i++){
                rp.get("test");
            }
            end = true;
        }
    }

}
