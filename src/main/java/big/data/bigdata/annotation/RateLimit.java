package big.data.bigdata.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {
    int capacity(); //令牌桶总容量
    int tokens(); //每次请求消耗令牌数
    int time();          // 时间窗口（秒）
    String key() default ""; // 可选的自定义key
}
