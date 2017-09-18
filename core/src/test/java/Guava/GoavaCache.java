package Guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jodd.datetime.JDateTime;
import java.util.concurrent.TimeUnit;

/*
* GoavaCache缓存测试类
* */
public class GoavaCache {
    static LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .refreshAfterWrite(3, TimeUnit.HOURS)
            .expireAfterAccess(3, TimeUnit.HOURS)
            .maximumSize(100).build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    String value = key + new JDateTime().toString("YYYY.MM.DD");
                    System.out.println("获取缓存，KEY："+ key + ", VALUE：" + value);
                    return value;
                }
            });

    public static void main(String[] args) throws Exception{
    }
}
