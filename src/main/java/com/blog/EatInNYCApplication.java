package com.blog;

import com.blog.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class EatInNYCApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("successful...");
    }
}
