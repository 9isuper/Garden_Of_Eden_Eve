package com.isuper.eden.eve.boot.common.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PasswordUtil {

    private static final String lowStr = "abcdefghijklmnopqrstuvwxyz";
    private static final String specialStr = "~!@#$%^&*()_+/-=[]{};:'<>?.";
    private static final String numStr = "0123456789";

    /**
     * 随机获取字符串字符
     *
     * @param str
     * @return
     */
    private static char getRandomChar(String str) {
        SecureRandom random = new SecureRandom();
        return str.charAt(random.nextInt(str.length()));
    }

    /**
     * 随机获取小写字符
     *
     * @return
     */
    private static char getLowChar() {
        return getRandomChar(lowStr);
    }

    /**
     * 随机获取大写字符
     *
     * @return
     */
    private static char getUpperChar() {
        return Character.toUpperCase(getLowChar());
    }

    /**
     * 随机获取数字字符
     *
     * @return
     */
    private static char getNumChar() {
        return getRandomChar(numStr);
    }

    /**
     * 随机获取特殊字符
     *
     * @return
     */
    private static char getSpecialChar() {
        return getRandomChar(specialStr);
    }

    /**
     * 指定调用字符函数
     *
     * @param funNum
     * @return
     */
    private static char getRandomChar(int funNum) {
        switch (funNum) {
            case 0:
                return getLowChar();
            case 1:
                return getUpperChar();
            case 2:
                return getNumChar();
            default:
                return getSpecialChar();
        }
    }

    /**
     * 指定长度，随机生成复杂密码
     *
     * @param num
     * @return 字符串
     */
    public static String getRandomPwd(int num) {
        List<Character> list = new ArrayList<>(num);
        list.add(getLowChar());
        list.add(getUpperChar());
        list.add(getNumChar());
        list.add(getSpecialChar());

        for (int i = 0; i < num; i++) {
            SecureRandom random = new SecureRandom();
            int funNum = random.nextInt(4);
            list.add(getRandomChar(funNum));
        }
        // 打乱排序
        Collections.shuffle(list);
        StringBuilder stringBuilder = new StringBuilder(list.size());
        for (Character c : list) {
            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }


}
