package com.sean.android.example.base.asynctask;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Seonil on 2017-03-09.
 */


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpUrlPath {

    String host() default "";

    String path() default "";
}
