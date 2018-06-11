package com.jsonschema.test.framework;

import com.jsonschema.test.framework.aop.MockBeanUtil;
import com.jsonschema.test.framework.aop.MockRemoteBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;

@Slf4j
public class JsonSchemaExtension implements TestInstancePostProcessor, BeforeAllCallback, AfterAllCallback,
        ParameterResolver, BeforeTestExecutionCallback {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {

        Field[] fields = FieldUtils.getFieldsWithAnnotation(testInstance.getClass(), MockRemoteBean.class);
        for (Field field : fields) {
            Object mock = MockBeanUtil.mockBean(field.getType());
            field.setAccessible(true);
            ReflectionTestUtils.setField(testInstance, field.getName(), mock);
        }
        log.info("testInstance is : " + testInstance);
        log.info("ExtensionContext is : " + context);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        log.info("after all callback");
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        log.info("before all callback");
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        log.info("support parameter " + parameterContext.toString());
        return false;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        log.info("resolve parameter " + parameterContext.toString());
        return null;
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        System.out.println("beforeTestExecution");

    }
}
