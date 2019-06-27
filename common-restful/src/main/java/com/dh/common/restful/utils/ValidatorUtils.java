package com.dh.common.restful.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;


/**
 * 
 * <p>参数验证工具类</p>
 * 
 */
public class ValidatorUtils {

    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    /**
     * 验证对象所有字段值，返回第一条错误信息
     * @param <T>                                参数类型
     * @param o                                  待校验的参数
     * @throws IllegalArgumentException          参数校验失败时抛出
     */
    public static <T> void validate(T o) throws IllegalArgumentException {
        Set<ConstraintViolation<T>> violations = factory.getValidator().validate(o);
        if (violations != null && violations.size() > 0) {
            ConstraintViolation<T> violation = violations.iterator().next();
            throw new IllegalArgumentException(violation.getMessage());
        }
    }

    /**
     * 校验对象所有字段值，返回所有错误信息
     * @param <T>                                参数类型
     * @param o                                  待校验的参数
     * @return                                   错误信息列表
     */
    public static <T> List<String> validateAll(T o) {
        List<String> errorList = new ArrayList<String>();
        Set<ConstraintViolation<T>> violations = factory.getValidator().validate(o);
        if (violations != null && violations.size() > 0) {
            for (ConstraintViolation<T> violation : violations) {
                errorList.add(violation.getMessage());
            }
        }

        return errorList;
    }
    
    public static String buildErrorMessage(String msg) {
        return String.format("%s:%s", "请求参数错误", msg);
    }
    
    /**
     * 正则表达式匹配
     * @param value
     * @param exp
     * @return
     */
    public static boolean matcher(String value,String exp){
    	Pattern pattern = Pattern.compile(exp);
    	Matcher matcher = pattern.matcher(value);
    	return matcher.find();
    }
}
