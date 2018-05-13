package com.jsonschema.web.representation;

import com.jsonschema.aop.MyAspect;
import com.jsonschema.web.client.CustomerClient;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;

@Component
public class ClientBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements BeanFactoryPostProcessor {

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInstantiation : " + beanName);
        return true;
    }


    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInstantiation : " + beanName);
        return null;
    }

    @Override
    public PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        System.out.println("postProcessPropertyValues : " + beanName);
        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization : " + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization : " + beanName);
        return bean;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        CustomerClient client = beanFactory.getBean(CustomerClient.class);
        AspectJProxyFactory factory = new AspectJProxyFactory(client);
        MyAspect aspect = new MyAspect();
        factory.addAspect(aspect);
        CustomerClient proxy = factory.getProxy();
        beanFactory.destroyBean("com.jsonschema.web.client.CustomerClient", client);
        beanFactory.registerSingleton("com.jsonschema.web.client.CustomerClient1", proxy);
        System.out.println("postProcessBeanFactory");
    }
}
