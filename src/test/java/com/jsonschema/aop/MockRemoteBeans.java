package com.jsonschema.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface MockRemoteBeans {

    MockRemoteBean[] value();
}
