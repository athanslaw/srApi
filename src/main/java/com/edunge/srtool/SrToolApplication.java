package com.edunge.srtool;

import com.edunge.srtool.config.FileConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileConfigurationProperties.class)
public class SrToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrToolApplication.class, args);
	}

}
