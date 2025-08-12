package big.data.bigdata;

import big.data.bigdata.entity.SnpData;
import big.data.bigdata.mapper.SnpDataMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class MysqlTest {

    @Autowired SnpDataMapper snpDataMapper;

    @Test
    public void testQueryPerformance() {
        // 预热
        queryOnce();

        int runs = 10;
        List<Long> times = new ArrayList<>();

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            queryOnce();
            long end = System.nanoTime();
            times.add((end - start) / 1_000_000); // 转成毫秒
        }

        Collections.sort(times);
        // 去掉最高最低
        times = times.subList(1, times.size() - 1);

        long avg = times.stream().mapToLong(Long::longValue).sum() / times.size();
        System.out.println("查询平均耗时: " + avg + "ms");
    }

    private void queryOnce() {
        List<SnpData> result = snpDataMapper.findSnpData(
                1,              // chromosome
                null,     // rsId
                13273,           // position
                null,           // population
                null,           // variant
                0,              // offset
                10              // limit
        );
        // 如果想确认一下查到了什么，可以打印
        System.out.println("本次查询到记录数: " + result.size());
    }

}
