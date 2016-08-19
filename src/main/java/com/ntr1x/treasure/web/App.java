package com.ntr1x.treasure.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan({
	"com.ntr1x.treasure.web",
})
public class App {

	public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }
}