package com.qf.shop_car;

import com.qf.util.Loginaop;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShopCarApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopCarApplication.class, args);
	}
	@Bean//加此注解是为了让Spring管理起来(在不同的工程下面)
	public Loginaop toLoginaop(){
		return  new Loginaop();
	}
}
