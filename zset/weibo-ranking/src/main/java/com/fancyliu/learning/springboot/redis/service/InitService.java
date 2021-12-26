package com.fancyliu.learning.springboot.redis.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.fancyliu.learning.springboot.redis.common.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 初始化一个月微博热门事件 service
 *
 * @author : Fancy Liu
 * @since : 2021/12/26 4:56 下午
 */
@Service
@Slf4j
public class InitService {

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 初始化 30 天的历史数据(每小时热门事件数据)
	 */
	@PostConstruct
	public void init30days() {
		log.info("开始初始化 30 天的历史数据");

		// 当前时间取整到小时数, 作为基准数, 用于初始化 30 天的每个小时 key
		long hour = System.currentTimeMillis() / (1000 * 60 * 60);
		String current = DateUtil.formatDateTime(DateUtil.date());
		log.info("当前小时数:{}, 对应时间:{}", hour, current);

		// 构建 30 天的每小时
		for (int i = 0; i < 24 * 30; i++) {
			// 从当前时间 倒推 30 天的每小时
			String key = Constants.HOUR_KEY_PREFIX + (hour - i);
//			log.info(key);
			this.initHourScore(key);
		}

		log.info("完成初始化 30 天的历史数据");
	}

	/**
	 * 初始化每个热门事件的指定小时的热度值
	 *
	 * @param key 小时对应的 key 值
	 */
	private void initHourScore(String key) {
		log.info("开始初始化所有热门事件的指定小时key: {} 的热度值", key);

		// 使用 26 个英文字母来代表微博热门事件, 为每个热门事件生成一个随机数(1~100)来代表热度
		for (int i = 1; i <= 26; i++) {
			// 热门事件名称 类似 AAA, BBB, ... , ZZZ
			String hot = String.valueOf((char) (96 + i)).toUpperCase() + String.valueOf((char) (96 + i)).toUpperCase() + String.valueOf((char) (96 + i)).toUpperCase();
			int randomInt = RandomUtil.randomInt(1, 100);
//			log.info("开始初始化热门事件: {} 的热度值: {}", hot, randomInt);

			this.redisTemplate.opsForZSet().add(
					key,
					hot,
					randomInt);
		}

		log.info("完成初始化所有热门事件的指定小时:{}的热度值", key);
	}

}
