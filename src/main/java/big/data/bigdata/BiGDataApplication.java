package big.data.bigdata;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("big.data.bigdata.mapper")/*mapper注解*/
public class BiGDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiGDataApplication.class, args);
    }

}
