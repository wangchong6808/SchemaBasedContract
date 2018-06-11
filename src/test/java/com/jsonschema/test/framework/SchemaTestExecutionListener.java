package com.jsonschema.test.framework;

import com.jsonschema.test.framework.aop.MockRemoteBean;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;


public class SchemaTestExecutionListener extends AbstractTestExecutionListener {


    @Override
    public void prepareTestInstance(TestContext testContext) {
        Object testInstance = testContext.getTestInstance();

        Field[] fields = FieldUtils.getFieldsWithAnnotation(testContext.getTestClass(), MockRemoteBean.class);
        for (Field field : fields) {
            String fieldName = field.getName();
            Object client = testContext.getApplicationContext().getBean(field.getType());
            field.setAccessible(true);
            ReflectionTestUtils.setField(testInstance, fieldName, client);
        }

    }

    @Override
    public void beforeTestClass(TestContext testContext) {

        Field[] fields = FieldUtils.getFieldsWithAnnotation(testContext.getTestClass(), MockRemoteBean.class);
        for (Field field : fields) {
            ClientBeanPostProcessor.addBeanDefinition(field.getType());
        }
    }


}
