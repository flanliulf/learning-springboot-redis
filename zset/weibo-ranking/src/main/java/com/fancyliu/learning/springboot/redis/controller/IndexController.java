package com.fancyliu.learning.springboot.redis.controller;

import cn.hutool.core.date.DateUtil;
import com.fancyliu.learning.springboot.redis.common.constants.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * 微博热门榜单 controller
 *
 * @author : Fancy Liu
 * @since : 2021/12/26 9:55 下午
 */
@RestController
@Slf4j
@Api(tags = "排行榜")
public class IndexController {


	@Autowired
	private RedisTemplate redisTemplate;

	@ApiOperation("小时榜")
	@GetMapping("/hour")
	public Set queryHour() {
		log.info("查询小时榜");

		// 当前时间取整到小时数
		long hour = System.currentTimeMillis() / (1000 * 60 * 60);
		String current = DateUtil.formatDateTime(DateUtil.date());
		log.info("当前小时数:{}, 对应时间:{}", hour, current);

		// 查询前 50 名的热度榜单(实际只有 26 个热门时间, 采用 redis 的 zrevrange 命令实现)
		Set<ZSetOperations.TypedTuple> set = this.redisTemplate.opsForZSet().reverseRangeWithScores(Constants.HOUR_KEY_PREFIX + hour, 0, 50);

		log.info("查询小时榜 returned: {}", set);
		return set;
	}

	@ApiOperation("日榜")
	@GetMapping("/daily")
	public Set queryDaily() {
		log.info("查询日榜");

		// 查询前 50 名的热度榜单(实际只有 26 个热门时间, 采用 redis 的 zrevrange 命令实现)
		Set<ZSetOperations.TypedTuple> set = this.redisTemplate.opsForZSet().reverseRangeWithScores(Constants.DAILY_KEY_PREFIX, 0, 50);

		log.info("查询日榜 returned: {}", set);
		return set;
	}

	@ApiOperation("周榜")
	@GetMapping("/weekly")
	public Set queryWeekly() {
		log.info("查询周榜");

		// 查询前 50 名的热度榜单(实际只有 26 个热门时间, 采用 redis 的 zrevrange 命令实现)
		Set<ZSetOperations.TypedTuple> set = this.redisTemplate.opsForZSet().reverseRangeWithScores(Constants.WEEKLY_KEY_PREFIX, 0, 50);

		log.info("查询周榜 returned: {}", set);
		return set;
	}

	@ApiOperation("月榜")
	@GetMapping("/monthly")
	public Set queryMonthly() {
		log.info("查询月榜");

		// 查询前 50 名的热度榜单(实际只有 26 个热门时间, 采用 redis 的 zrevrange 命令实现)
		Set<ZSetOperations.TypedTuple> set = this.redisTemplate.opsForZSet().reverseRangeWithScores(Constants.MONTHLY_KEY_PREFIX, 0, 50);

		log.info("查询月榜 returned: {}", set);
		return set;
	}

}
