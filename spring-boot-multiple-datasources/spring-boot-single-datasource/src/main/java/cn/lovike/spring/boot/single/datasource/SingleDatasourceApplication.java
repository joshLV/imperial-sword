package cn.lovike.spring.boot.single.datasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("cn.lovike.spring.boot.single.datasource.mapper")
public class SingleDatasourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SingleDatasourceApplication.class, args);
    }

}
