package com.challenge.powerledger;

import com.challenge.powerledger.configuration.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@Import(SwaggerConfig.class)
public class TestCodePowerledgerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestCodePowerledgerApplication.class, args);
    }

}
