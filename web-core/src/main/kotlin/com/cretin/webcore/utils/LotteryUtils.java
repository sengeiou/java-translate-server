package com.cretin.webcore.utils;

import java.util.Random;

/**
 * <p>
 * Title: LotteryUtils
 * </p>
 * <p>
 * Description: 抽奖工具类
 * </p>
 * <p>
 * Company: www.cretin.com
 * </p>
 * 
 * @author cretin
 * @date 2017年12月28日
 */
public class LotteryUtils {
	/**
	 * 获取抽奖结果位置
	 * 
	 * @return
	 */
	public static Integer lotteryResultPosition() {
		// * 1 68乐豆 5%
		// * 2 5QB 0.1%
		// * 3 10元话费 0.1%
		// * 4 18乐豆 10%
		// * 5 谢谢参与 74.8%
		// * 6 LOL皮肤 0
		// * 7 38乐豆 10%
		// * 8 王者皮肤 0
		float gailv = new Random().nextFloat();
		if (gailv < 0.05) {
			return 1;
		} else if (gailv < 0.0501) {
			return 2;
		} else if (gailv < 0.0502) {
			return 3;
		} else if (gailv < 0.152) {
			return 4;
		} else if (gailv < 0.8) {
			return 5;
		} else {
			return 7;
		}
	}

	/**
	 * 获取抽奖结果描述
	 * 
	 * @param position
	 * @return
	 */
	public static String lotteryResultDes(int position) {
		// * 1 300乐豆 5%
		// * 2 5QB 0.1%
		// * 3 10元话费 0.1%
		// * 4 28乐豆 10%
		// * 5 谢谢参与 74.8%
		// * 6 LOL皮肤 0
		// * 7 168乐豆 10%
		// * 8 王者皮肤 0
		switch (position) {
		case 1:
			return "恭喜获得68乐豆,乐豆已经到账";
		case 2:
			return "恭喜获得5QB,请与客服联系领取";
		case 3:
			return "恭喜获得10元话费,请与客服联系领取";
		case 4:
			return "恭喜获得18乐豆,乐豆已经到账";
		case 5:
			return "再接再厉";
		case 6:
			return "恭喜获得LOL皮肤,请与客服联系领取";
		case 7:
			return "恭喜获得38乐豆,乐豆已经到账";
		case 8:
			return "恭喜获得王者皮肤,请与客服联系领取";
		}
		return "感谢您的参与";
	}

	/**
	 * 获取抽奖结果简单描述
	 * 
	 * @param position
	 * @return
	 */
	public static String lotteryResultEasyDes(int position) {
		// * 1 300乐豆 5%
		// * 2 5QB 0.1%
		// * 3 10元话费 0.1%
		// * 4 28乐豆 10%
		// * 5 谢谢参与 74.8%
		// * 6 LOL皮肤 0
		// * 7 168乐豆 10%
		// * 8 王者皮肤 0
		switch (position) {
		case 1:
			return "68乐豆";
		case 2:
			return "5QB";
		case 3:
			return "10元话费";
		case 4:
			return "18乐豆";
		case 5:
			return "再接再厉";
		case 6:
			return "LOL皮肤";
		case 7:
			return "38乐豆";
		case 8:
			return "王者皮肤";
		}
		return "感谢您的参与";
	}
}