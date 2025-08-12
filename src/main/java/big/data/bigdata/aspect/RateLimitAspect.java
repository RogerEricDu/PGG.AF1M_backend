package big.data.bigdata.aspect;

import big.data.bigdata.annotation.RateLimit;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {
    private final StringRedisTemplate redisTemplate;

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        String key = buildRedisKey(joinPoint, rateLimit);
        String luaScript = buildLuaScript();

        Long result = redisTemplate.execute(
                new DefaultRedisScript<>(luaScript, Long.class),
                Collections.singletonList(key),
                String.valueOf(rateLimit.capacity()),
                String.valueOf(rateLimit.tokens()),
                String.valueOf(rateLimit.time()),
                String.valueOf(System.currentTimeMillis() / 1000)
        );

        if (result == null || result == 0) {
            throw new RuntimeException("Rate limit exceeded. Try again later.");
        }
        return joinPoint.proceed();
    }

    private String buildRedisKey(ProceedingJoinPoint joinPoint, RateLimit rateLimit) {
        if (!rateLimit.key().isEmpty()) {
            return "rate_limit:" + rateLimit.key();
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        String methodName = joinPoint.getSignature().toShortString();
        return "rate_limit:" + ip + ":" + methodName;
    }

    private String buildLuaScript() {
        return """
            local key = KEYS[1]
            local capacity = tonumber(ARGV[1])
            local tokens = tonumber(ARGV[2])
            local timeWindow = tonumber(ARGV[3])
            local currentTime = tonumber(ARGV[4])
            
            local lastRefillTime = redis.call('hget', key, 'time') or currentTime
            local availableTokens = tonumber(redis.call('hget', key, 'tokens') or capacity)
            
            local elapsed = currentTime - lastRefillTime
            local refillAmount = math.floor(elapsed / timeWindow) * tokens
            
            if refillAmount > 0 then
                availableTokens = math.min(availableTokens + refillAmount, capacity)
                redis.call('hset', key, 'time', currentTime)
            end
            
            if availableTokens >= tokens then
                redis.call('hset', key, 'tokens', availableTokens - tokens)
                return 1
            end
            return 0
            """;
    }
}
