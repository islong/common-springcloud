package com.dh.common.restful.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.dh.common.restful.annotion.ValidIpList;

/**
 * 
 * ip格式校验器
 */
public class ValidIpListValidator implements ConstraintValidator<ValidIpList, String> {

	private String msg;
	
	String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

	@Override
	public void initialize(ValidIpList constraintAnnotation) {
		this.msg = constraintAnnotation.message();
	}
	
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String[] arrays = value.split(",");
        for(String ip : arrays) {
        	Pattern patten = Pattern.compile(regex);
        	Matcher matcher = patten.matcher(ip);
        	if(!matcher.matches()){
        		context.disableDefaultConstraintViolation();
        		context.buildConstraintViolationWithTemplate(msg+": "+ip).addConstraintViolation();
        		return false;
        	}
        }
        
        return true;
    }

	
}
