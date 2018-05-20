package com.jsonschema.config;

import com.jsonschema.aop.MockBeanUtil;
import com.jsonschema.aop.MockRemoteBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;

@Slf4j
public class JsonSchemaExtension implements TestInstancePostProcessor, BeforeAllCallback, AfterAllCallback,
        ParameterResolver, BeforeTestExecutionCallback {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        //Object testInstance = context.getTestInstance().get();
        /*Field field = testInstance.getClass().getDeclaredField("customerClient");
        field.setAccessible(true);
        CustomerClient client = Mockito.mock(CustomerClient.class);
        AspectJProxyFactory factory = new AspectJProxyFactory(client);
        MyAspect aspect = new MyAspect();
        factory.addAspect(aspect);
        CustomerClient proxy = factory.getProxy();
        ReflectionTestUtils.setField(testInstance, "customerClient", proxy);*/
        for (Field field : testInstance.getClass().getDeclaredFields()) {
            if (field.getAnnotation(MockRemoteBean.class) != null) {
                Object mock = MockBeanUtil.mockBean(field.getType());
                field.setAccessible(true);
                ReflectionTestUtils.setField(testInstance, field.getName(), mock);
                System.out.println("found mock");
            }
        }
        log.info("testInstance is : " + testInstance);
        log.info("ExtensionContext is : " + context);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        log.info("after all callback");
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
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
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        System.out.println("beforeTestExecution");

    }
}
