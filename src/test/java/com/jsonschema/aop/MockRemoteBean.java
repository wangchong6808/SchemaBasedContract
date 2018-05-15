package com.jsonschema.aop;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(MockRemoteBeans.class)
public @interface MockRemoteBean {
}
