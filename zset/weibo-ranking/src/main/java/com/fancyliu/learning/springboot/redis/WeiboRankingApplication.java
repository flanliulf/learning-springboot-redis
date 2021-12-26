package com.fancyliu.learning.springboot.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 * @author : Fancy Liu
 * @since : 2021/12/26 10:13 下午
 */
@SpringBootApplication
@EnableScheduling
public class WeiboRankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeiboRankingApplication.class, args);
	}

}
