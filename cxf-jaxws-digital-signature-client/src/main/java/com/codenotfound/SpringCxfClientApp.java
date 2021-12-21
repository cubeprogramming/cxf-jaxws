package com.codenotfound;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.codenotfound")
public class SpringCxfClientApp {

  public static void main(String[] args) {
    SpringApplication.run(SpringCxfClientApp.class, args);
  }
}
