package LeetCode_sliding_window;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 李杰
 * @version 1.0
 * @Description 替换后的最长重复字符
 * @package
 * @file ${fileName.java}
 * @createTime: 创建时间: 2020/8/20 14:45
 * @title 标题: 替换后的最长重复字符
 * @module 模块: 模块名称
 * @reviewer 审核人
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 */
public class number424_2 {
    //s = "AABABBA", k = 1
    //关键部分是：求：滑动窗口内中出现次数最大的字符+k 的最大值
    public int characterReplacement(String s, int k) {
        //max用于存结果,cur用来统计历史字母出现最多的个数
        int max = 0, cur = 0;
        int left = 0, right = 0;
        //滑动窗口，用于统计字母出现的次数
        Map<Character, Integer> windows = new HashMap<>();
        while(right < s.length()){
            char rChar = s.charAt(right);
            windows.put(rChar, windows.getOrDefault(rChar, 0) + 1);
            cur = Math.max(cur, windows.get(rChar));
            right++;
            //表示窗口中能替换的次数不足
            while(right - left - cur > k){ //如果当前字符串中的出现次数最多的字母个数+K大于串长度，那么这个串就是满足条件的
                char lChar = s.charAt(left);
                windows.put(lChar, windows.get(lChar) - 1);
                left++;
            }
            max = Math.max(max, right - left);
        }
        return max;
    }

    public static void main(String[] args) {
        new number424_2().characterReplacement("AABABBA",  1);
    }
}
