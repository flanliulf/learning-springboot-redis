package com.fancyliu.learning.springboot.redis.schedule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.fancyliu.learning.springboot.redis.common.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 刷新热度的定时任务
 *
 * @author : Fancy Liu
 * @since : 2021/12/26 5:32 下午
 */
@Component
@Slf4j
public class RefreshTask {

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 每 5 秒定时刷新所有热门事件的热度值(模拟热门事件热度值的增长)
	 * <p>
	 * 这里简单使用 Spring Boot 内置的 job 实现, 实际线上环境需要采用 xxl-job 等分布式调度框架
	 */
	@Scheduled(fixedRate = 1000 * 10)
	public void refreshScore() {
		log.info("开始刷新热度值");

		// 刷新日榜单的热度统计值
		this.refreshHour();

		log.info("完成刷新热度值");
	}


	/**
	 * 每小时定时刷新日/周/月榜单统计值(即合并每个小时的热度值)
	 * <p>
	 * 这里简单使用 Spring Boot 内置的 job 实现, 实际线上环境需要采用 xxl-job 等分布式调度框架
	 */
	@Scheduled(fixedRate = 1000 * 60 * 60)
	public void refreshStatistics() {
		log.info("开始刷新日/周/月榜单统计值");

		// 刷新日榜单的热度统计值
		this.refreshDaily();

		// 刷新周榜单的热度统计值
		this.refreshWeekly();

		// 刷新月榜单的热度统计值
		this.refreshMonthly();

		log.info("完成刷新日/周/月榜单统计值");
	}

	/**
	 * 刷新小时榜的热度值(模拟所有热门事件热度值的随机增长)
	 */
	public void refreshHour() {
		log.info("小时榜单热度值刷新开始");

		// 当前时间取整到小时数
		long hour = System.currentTimeMillis() / (1000 * 60 * 60);
		String current = DateUtil.formatDateTime(DateUtil.date());
		log.info("小时榜单开始小时数:{}, 对应时间:{}", hour, current);

		// 刷新每个热门事件的的热度值(热度随机增长1~100)
		for (int i = 1; i <= 26; i++) {
			// 为每个热门事件(26 个字母模拟), 随机生成热度值(对应 redis  zincrby 命令)
			// 热门事件名称 类似 AAA, BBB, ... , ZZZ
			String hot = String.valueOf((char) (96 + i)).toUpperCase() + String.valueOf((char) (96 + i)).toUpperCase() + String.valueOf((char) (96 + i)).toUpperCase();

			this.redisTemplate.opsForZSet().incrementScore(
					Constants.HOUR_KEY_PREFIX + hour,
					hot,
					RandomUtil.randomInt(1, 100));
		}

		log.info("小时榜单热度值刷新完成");
	}

	/**
	 * 刷新日榜单的热度统计值
	 */
	public void refreshDaily() {
		log.info("日榜单刷新开始");

		// 当前时间取整到小时数
		long hour = System.currentTimeMillis() / (1000 * 60 * 60);
		String current = DateUtil.formatDateTime(DateUtil.date());
		log.info("日榜单开始小时数:{}, 对应时间:{}", hour, current);

		// 当日内的其他 23 小时的 key list
		List<String> otherKeyList = new ArrayList<>();
		// 从当前小时, 往前倒推 23 小时
		for (int i = 1; i < 23; i++) {
			String key = Constants.HOUR_KEY_PREFIX + (hour - i);
			otherKeyList.add(key);
		}

		// 将当前小时及往前倒推 23 小时, 总计 24 小时的每个小时数热度取并集(对应 redis zunionstore 命令), 放入日榜的 key 中
		this.redisTemplate.opsForZSet().unionAndStore(
				Constants.HOUR_KEY_PREFIX + hour,
				otherKeyList,
				Constants.DAILY_KEY_PREFIX);

		// 历史数据, 超过 45 天后过期, 减少存储空间
		for (int i = 0; i < 24; i++) {
			String key = Constants.HOUR_KEY_PREFIX + (hour - i);
			this.redisTemplate.expire(key, 45, TimeUnit.DAYS);
		}

		log.info("日榜单刷新完成");
	}

	/**
	 * 刷新周榜单的热度统计值
	 */
	public void refreshWeekly() {
		log.info("周榜单刷新开始");

		// 当前时间取整到小时数
		long hour = System.currentTimeMillis() / (1000 * 60 * 60);
		String current = DateUtil.formatDateTime(DateUtil.date());
		log.info("周榜单开始小时数:{}, 对应时间:{}", hour, current);

		// 7 天内其他的每一个小时的 key list
		List<String> otherKeyList = new ArrayList<>();
		// 从当前小时, 往前倒推 7*24 小时
		for (int i = 1; i < 24 * 7 - 1; i++) {
			String key = Constants.HOUR_KEY_PREFIX + (hour - i);
			otherKeyList.add(key);
		}

		// 将当前小时及往前倒推 7 天, 总计 7* 24 小时的每个小时数热度取并集(对应 redis zunionstore 命令), 放入月榜的 key 中
		this.redisTemplate.opsForZSet().unionAndStore(
				Constants.HOUR_KEY_PREFIX + hour,
				otherKeyList,
				Constants.WEEKLY_KEY_PREFIX);

		log.info("周榜单刷新完成");
	}

	/**
	 * 刷新月榜单的热度统计值
	 */
	public void refreshMonthly() {
		log.info("月榜单刷新开始");

		// 当前时间取整到小时数
		long hour = System.currentTimeMillis() / (1000 * 60 * 60);
		String current = DateUtil.formatDateTime(DateUtil.date());
		log.info("月榜单开始小时数:{}, 对应时间:{}", hour, current);

		// 30 天内其他的每一个小时的 key list
		List<String> otherKeyList = new ArrayList<>();
		// 从当前小时, 往前倒推 30*24 小时
		for (int i = 1; i < 24 * 30 - 1; i++) {
			String key = Constants.HOUR_KEY_PREFIX + (hour - i);
			otherKeyList.add(key);
		}

		// 将当前小时及往前倒推 30 天, 总计 30*24 小时的每个小时数热度取并集(对应 redis zunionstore 命令), 放入月榜的 key 中
		this.redisTemplate.opsForZSet().unionAndStore(
				Constants.HOUR_KEY_PREFIX + hour,
				otherKeyList,
				Constants.MONTHLY_KEY_PREFIX);

		log.info("月榜单刷新完成");
	}


}
