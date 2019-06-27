package com.dh.common.restful.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.dh.common.restful.annotion.ValidUrl;

/**
 * 
 * url格式校验器
 */
public class ValidUrlValidator implements ConstraintValidator<ValidUrl, String> {

	private String msg;
	
	String regex = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)*((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|localhost|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(\\:[0-9]+)*(/($|[a-zA-Z0-9\\.\\,\\?\\'\\\\\\+&amp;%\\$#\\=~_\\-]+))*$";

	@Override
	public void initialize(ValidUrl constraintAnnotation) {
		this.msg = constraintAnnotation.message();
	}
	
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        Pattern patten = Pattern.compile(regex);
    	Matcher matcher = patten.matcher(value);
    	if(!matcher.matches()) {
    		context.disableDefaultConstraintViolation();
    		context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
    		return false;
    	}
        
        
        return true;
    }

	
}
