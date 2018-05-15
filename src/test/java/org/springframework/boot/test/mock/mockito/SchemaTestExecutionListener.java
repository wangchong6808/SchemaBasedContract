package org.springframework.boot.test.mock.mockito;

import com.jsonschema.aop.MockRemoteBean;
import com.jsonschema.web.client.CustomerClient;
import com.jsonschema.web.representation.ClientBeanPostProcessor;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;


public class SchemaTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        System.out.println("SchemaTestExecutionListener ");
        CustomerClient client = testContext.getApplicationContext().getBean(CustomerClient.class);
        //super.prepareTestInstance(testContext);
        Object testInstance = testContext.getTestInstance();
        Field field = testInstance.getClass().getDeclaredField("customerClient");
        field.setAccessible(true);

        ReflectionTestUtils.setField(testInstance, "customerClient", client);
    }

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        //ConfigurableListableBeanFactory factory = ((AnnotationConfigApplicationContext)testContext.getApplicationContext().getParentBeanFactory()).getBeanFactory();

        Field[] fields = FieldUtils.getFieldsWithAnnotation(testContext.getTestClass(), MockRemoteBean.class);
        for (Field field : fields) {
            ClientBeanPostProcessor.addBeanDefinition(field.getType());
        }
    }


}
