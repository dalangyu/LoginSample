package com.base.baselibrary.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by langlang on 2016/10/20.
 */


public class StringUtils {


    /**
     * 正则表达式:验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";
    public static final DecimalFormat df = new DecimalFormat("0.00");


    public static String formatDecimal(double val, int num) {
        StringBuilder sb = new StringBuilder("0.0");
        if (num > 1) {
            for (int i = 0; i < num - 1; i++) {
                sb.append("0");
            }
        }
        DecimalFormat df = new DecimalFormat(sb.toString());
        return df.format(val);
    }

    public static String formatDecimal(double val) {
        return formatDecimal(val, 2) + "元/ip";
    }

    /**
     * 判断给定字符串是否空白串。
     */
    public static boolean isEmpty(String obj) {
        return null == obj || obj.toString().trim().length() == 0|| equals("",obj);
    }

    /**
     * 判断给定字符串是否为17位的律师证号
     */
    public static boolean checkLawyerNo(String obj) {
        return null != obj &&  obj.toString().trim().length()== 17;
    }



    /**
     * 校验银行卡卡号
     * @param cardId
     * @return
     */
    public static boolean checkBankCard( String cardId) {
        if (cardId == null)
            return false;
        if (cardId.length() == 16 || cardId.length() == 19)
            return true;
        return false;
    }



    public static String getChannel(Context context) {
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                String fileName;
                int indexOf = entryName.lastIndexOf(File.separator);
                if (indexOf > 0) {
                    fileName = entryName.substring(indexOf + 1);
                } else {
                    fileName = entryName;
                }
                if (fileName.startsWith("channel")) {
                    ret = fileName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] split = ret.split("_");
        if (split.length >= 2) {
            return ret.substring(split[0].length() + 1);
        } else {
            return "";
        }
    }

    /**
     * 格式化小说内容。
     * <p/>
     * <li>小说的开头，缩进2格。在开始位置，加入2格空格。
     * <li>所有的段落，缩进2格。所有的\n,替换为2格空格。
     *
     * @param str
     * @return
     */
    public static String formatContent(String str) {
        str = str.replaceAll("[ ]*", "");//替换来自服务器上的，特殊空格
        str = str.replaceAll("[ ]*", "");//
        str = str.replace("\n\n", "\n");
        str = str.replace("\n", "\n" + getTwoSpaces());
        str = str.replace("<p>", "\n" + getTwoSpaces());
        str = str.replace("</p>", "");
        str = str.replace("&nbsp", "");
        str = getTwoSpaces() + str;
//        str = convertToSBC(str);
        return str;
    }

    /**
     * Return a String that only has two spaces.
     *
     * @return
     */
    public static String getTwoSpaces() {
        return "\u3000\u3000";
    }

    /**
     * 依次返回（汉字数、英文数、数字数、特殊字符）
     *
     * @param str
     * @return
     */
    public static int[] computeCharsLen(String str) {
        String E1 = "[\u4e00-\u9fa5]";// 中文
        String E2 = "[a-zA-Z]";// 英文
        String E3 = "[0-9]";// 数字

        int chineseCount = 0;
        int englishCount = 0;
        int numberCount = 0;

        String temp;
        for (int i = 0; i < str.length(); i++) {
            temp = String.valueOf(str.charAt(i));
            if (temp.matches(E1)) {
                chineseCount++;
            }
            if (temp.matches(E2)) {
                englishCount++;
            }
            if (temp.matches(E3)) {
                numberCount++;
            }
        }
        return new int[]{chineseCount, englishCount, numberCount, (str.length() - (chineseCount +
                englishCount + numberCount))};
    }


    public static int str2int(String value) {
        int response = 0;
        if (!isEmpty(value)) {
            try {
                response = Integer.valueOf(value);
            } catch (NumberFormatException exception) {
                return 0;
            }

        }
        return response;
    }

    public static double str2double(String value) {
        double response = 0;
        if (!isEmpty(value)) {
            try {
                response = Double.valueOf(value);
            } catch (NumberFormatException exception) {
                return 0;
            }

        }
        return response;
    }

    public static String str2doubleTwo(String value) {
        String response = "0.00";
        if (!isEmpty(value)) {
            try {
                df.setRoundingMode(RoundingMode.HALF_UP);
                response = df.format(Double.valueOf(value));
            } catch (NumberFormatException exception) {
                return "0.00";
            }

        }
        return response;
    }


    public static long str2long(String value) {
        long response = 0;
        if (!isEmpty(value)) {
            try {
                response = Long.valueOf(value);
            } catch (NumberFormatException exception) {
                return 0;
            }

        }
        return response;
    }


    /**
     * Returns true if a and b are equal, including if they are both null.
     * <p><i>Note: In platform versions 1.1 and earlier, this method only worked well if
     * both the arguments were instances of String.</i></p>
     *
     * @param a first CharSequence to check
     * @param b second CharSequence to check
     * @return true if a and b are equal
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }


    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }


    //邮箱验证
    public static boolean isEmail(String strEmail) {
//        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\" +
//                ".[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (StringUtils.isEmpty(REGEX_EMAIL)) {
            return false;
        } else {
            return strEmail.matches(REGEX_EMAIL);
        }
    }

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";



    public static boolean validatePhoneNum(String phone) {
        String phoneRule = "^((14[0-9])|(17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(phoneRule);
        Matcher match = p.matcher(phone);
        if (!match.matches()) {
            return false;
        }
        return true;
    }


    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

    }


}
