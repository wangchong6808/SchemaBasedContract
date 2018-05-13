package org.springframework.boot.test.mock.mockito;

import com.jsonschema.aop.MyAspect;
import com.jsonschema.web.client.CustomerClient;
import org.mockito.Mockito;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.boot.test.mock.mockito.Definition;
import org.springframework.boot.test.mock.mockito.DefinitionsParser;
import org.springframework.boot.test.mock.mockito.MockitoPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;


public class SchemaTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        System.out.println("SchemaTestExecutionListener ");
        //super.prepareTestInstance(testContext);

        /*Field field = testInstance.getClass().getDeclaredField("customerClient");
        field.setAccessible(true);
        CustomerClient client = Mockito.mock(CustomerClient.class);
        AspectJProxyFactory factory = new AspectJProxyFactory(client);
        MyAspect aspect = new MyAspect();
        factory.addAspect(aspect);
        CustomerClient proxy = factory.getProxy();
        ReflectionTestUtils.setField(testInstance, "customerClient", proxy);*/
    }

    private void injectFields(TestContext testContext) {
        DefinitionsParser parser = new DefinitionsParser();
        parser.parse(testContext.getTestClass());
        if (!parser.getDefinitions().isEmpty()) {
            injectFields(testContext, parser);
        }
    }

    private void injectFields(TestContext testContext, DefinitionsParser parser) {
        ApplicationContext applicationContext = testContext.getApplicationContext();
        MockitoPostProcessor postProcessor = applicationContext
                .getBean(MockitoPostProcessor.class);
        for (Definition definition : parser.getDefinitions()) {
            Field field = parser.getField(definition);
            if (field != null) {
                postProcessor.inject(field, testContext.getTestInstance(), definition);
            }
        }
    }
}
