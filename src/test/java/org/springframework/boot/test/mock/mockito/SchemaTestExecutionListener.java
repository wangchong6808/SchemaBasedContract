package org.springframework.boot.test.mock.mockito;

import com.jsonschema.web.client.CustomerClient;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;


public class SchemaTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        System.out.println("SchemaTestExecutionListener ");
        CustomerClient client = testContext.getApplicationContext().getBean(CustomerClient.class);

        Object testInstance = testContext.getTestInstance();
        Field field = testInstance.getClass().getDeclaredField("customerClient");
        field.setAccessible(true);

        ReflectionTestUtils.setField(testInstance, "customerClient", client);
    }

}
