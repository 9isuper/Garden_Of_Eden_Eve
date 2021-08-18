package com.isuper.eden.eve.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author admin
 */
public class RandomUtils extends RandomStringUtils {

  private static final int SHORT_UUID_LENGTH = 8;

  public static String[] chars =
      new String[] {
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
        "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
        "S", "T", "U", "V", "W", "X", "Y", "Z"
      };

  public static String Uuid() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  public static String shortUuid() {
    StringBuffer shortBuffer = new StringBuffer();
    String uuid = RandomUtils.Uuid();
    for (int i = 0; i < SHORT_UUID_LENGTH; i++) {
      String str = uuid.substring(i * 4, i * 4 + 4);
      int x = Integer.parseInt(str, 16);
      shortBuffer.append(chars[x % 0x3E]);
    }
    return shortBuffer.toString();
  }

  public static int getRandNum(int min, int max) {
    int randNum = min + (int) (Math.random() * ((max - min) + 1));
    return randNum;
  }

  /**
   * getStringRandom
   * <p>产生一定长度的随机字符串<br>
   * show 方产生一定长度的随机字符串
   * @param length 需要生成随机字符串的长度
   * @return 返回随机字符串
   */
  public static String getStringRandom(int length) {

    String val = "";
    SecureRandom random = new SecureRandom();
    // length为几位密码
    for (int i = 0; i < length; i++) {
      String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
      // 输出字母还是数字
      if ("char".equalsIgnoreCase(charOrNum)) {
        // 输出是大写字母还是小写字母
        int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
        val += (char) (random.nextInt(26) + temp);
      } else if ("num".equalsIgnoreCase(charOrNum)) {
        val += String.valueOf(random.nextInt(10));
      }
    }
    return val;
  }


  public static int card() {
    int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    SecureRandom rand = new SecureRandom();
    for (int i = 10; i > 1; i--) {
      int index = rand.nextInt(i);
      int tmp = array[index];
      array[index] = array[i - 1];
      array[i - 1] = tmp;
    }
    int result = 0;
    for (int i = 0; i < 6; i++) {
      result = result * 10 + array[i];
    }
    return result;
  }
}
