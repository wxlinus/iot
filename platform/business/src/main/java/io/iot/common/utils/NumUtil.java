package io.iot.common.utils;

import java.util.function.Function;

/**
 * @author xupeng
 *编号生成工具
 */
public class NumUtil {

	public static String getNum(String prefix, Function<String, String> function) {
		return function.apply(prefix);
	}
}
