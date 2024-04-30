package com.penguin.website.annotation;

import java.lang.annotation.*;

/**
 * 不校验token
 * @see
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoVerificationToken {

}
