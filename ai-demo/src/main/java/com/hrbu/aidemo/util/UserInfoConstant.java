package com.hrbu.aidemo.util;

import java.util.regex.Pattern;

public class UserInfoConstant {
    // 正则：身份证、手机号、密钥、密码类敏感信息
    public static final Pattern ID_CARD = Pattern.compile("\\d{18}|\\d{17}[xX]");
    public static final Pattern PHONE = Pattern.compile("1[3-9]\\d{9}");
    public static final Pattern SECRET_KEY = Pattern.compile("sk-[a-zA-Z0-9]+");
    public static final Pattern PASSWORD = Pattern.compile("password\\s*[:=]\\s*[^\\s]+", Pattern.CASE_INSENSITIVE);
}
