package com.jsonschema.web.representation;

import com.jsonschema.aop.MyAspect;
import org.mockito.Mockito;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ClientBeanPostProcessor implements BeanFactoryPostProcessor {

    private static Map<String, Object> beanDefinitions = new HashMap<>();


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String className : beanDefinitions.keySet()) {
            beanFactory.registerSingleton(className, beanDefinitions.get(className));
        }
    }

    public static void addBeanDefinition(Class c) {
        Object o = Mockito.mock(c);
        AspectJProxyFactory factory = new AspectJProxyFactory(o);
        MyAspect aspect = new MyAspect();
        factory.addAspect(aspect);
        beanDefinitions.put(c.getName(), factory.getProxy());
    }

}
