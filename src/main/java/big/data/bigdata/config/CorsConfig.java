package big.data.bigdata.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(jwtRequestFilter)
                .addPathPatterns("/api/**") //配置拦截路径
                .excludePathPatterns("/api/user/register","/api/user/login"); //排除注册和登录路径
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //允许跨域访问的路径
                .allowedOriginPatterns("*")//允许跨域访问的源
                .allowedMethods("POST","GET","PUT","OPTIONS","DELETE")//允许请求方法
                .maxAge(168000) //预检间隔时间
                .allowedHeaders("*")//允许头部设置
                .allowCredentials(true);//是否发送cookie
    }

}
