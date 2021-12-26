package com.fancyliu.learning.springboot.redis.common.constants;

/**
 * 微博热搜榜单 redis key 前缀
 *
 * @author : Fancy Liu
 * @since : 2021/12/26 5:16 下午
 */
public class Constants {

	/**
	 * 小时榜
	 */
	public static final String HOUR_KEY_PREFIX = "weibo:ranking:hour:";


	/**
	 * 日榜
	 */
	public static final String DAILY_KEY_PREFIX = "weibo:ranking:daily";

	/**
	 * 周榜
	 */
	public static final String WEEKLY_KEY_PREFIX = "weibo:ranking:weekly";

	/**
	 * 月榜
	 */
	public static final String MONTHLY_KEY_PREFIX = "weibo:ranking:monthly";
}
