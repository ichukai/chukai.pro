package com.chulung.common.util;

import org.apache.commons.lang3.ArrayUtils;

public class NumberUtil {

	/**
	 * 判断val 是否在[from , to] 中包含 from ,to
	 * 
	 * @param val
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean isRangeIn(int val, int from, int to) {
		return val >= from && val <= to;
	}

	/**
	 * 判断val 是否不在[from , to] 中包含 from ,to
	 * 
	 * @param val
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean isRangeNotIn(int val, int from, int to) {
		return !isRangeIn(val, from, to);
	}

	/**
	 * 线性函数归一化(Min-Max scaling)
	 * @param arr 将arr归一化
	 */
	public static void  normalization(double [] arr){
		if (ArrayUtils.isEmpty(arr)) return;
		double max=arr[0],min=arr[0];
		for (int i = 1; i < arr.length; i++) {
			max=arr[i]>max?arr[i]:max;
			min=arr[i]<min?arr[i]:min;
		}
		double v = max - min;
		for (int i = 0; i < arr.length; i++) {
			arr[i]=(arr[i]-min)/ v;
		}
	}
}