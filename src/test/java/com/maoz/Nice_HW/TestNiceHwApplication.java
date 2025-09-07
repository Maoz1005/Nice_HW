package com.maoz.Nice_HW;

import org.springframework.boot.SpringApplication;

public class TestNiceHwApplication {

	public static void main(String[] args) {
		SpringApplication.from(NiceHwApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
