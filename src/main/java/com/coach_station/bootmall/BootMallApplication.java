package com.coach_station.bootmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@ServletComponentScan
@EnableCaching
@SpringBootApplication
@EnableScheduling
public class BootMallApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootMallApplication.class, args);
	}

}
