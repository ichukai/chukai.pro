package com.wck.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wck.blog.enumerate.AuthorityEnum;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
public @interface Authority {
	AuthorityEnum[]authority() default AuthorityEnum.SUPERADMIN;
}
