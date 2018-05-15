package com.jsonschema.web.representation;

import com.jsonschema.aop.MockRemoteBean;
import com.jsonschema.aop.MyAspect;
import com.jsonschema.web.client.CustomerClient;
import org.mockito.Mockito;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.core.Conventions;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;

@Component
public class ClientBeanPostProcessor implements BeanClassLoaderAware, BeanFactoryPostProcessor {

    private Map<String, Object> beanDefinitions = new HashMap<>();

    private static final String CONFIGURATION_CLASS_ATTRIBUTE = Conventions
            .getQualifiedAttributeName(ConfigurationClassPostProcessor.class,
                    "configurationClass");

    private ClassLoader classLoader;


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //CustomerClient client = beanFactory.getBean(CustomerClient.class);
        CustomerClient client = Mockito.mock(CustomerClient.class);
        AspectJProxyFactory factory = new AspectJProxyFactory(client);
        MyAspect aspect = new MyAspect();
        factory.addAspect(aspect);
        CustomerClient proxy = factory.getProxy();
        //beanFactory.destroyBean("com.jsonschema.web.client.CustomerClient", client);
        beanFactory.registerSingleton("com.jsonschema.web.client.CustomerClient", proxy);
        System.out.println("postProcessBeanFactory");

        /*for (Class<?> configurationClass : getConfigurationClasses(beanFactory)) {
            for (Class cls : ClassUtils.getAllInterfacesAsSet(configurationClass)) {
                System.out.println("class name:" + cls.toString());
            }
            parseElement(configurationClass);
        }*/
    }

    private void parseElement(AnnotatedElement element) {
        for (MockRemoteBean annotation : AnnotationUtils.getRepeatableAnnotations(element,
                MockRemoteBean.class, MockRemoteBean.class)) {
            parseMockBeanAnnotation(annotation, element);
        }
    }

    private void parseMockBeanAnnotation(MockRemoteBean annotation, AnnotatedElement element) {
        Set<ResolvableType> typesToMock = getOrDeduceTypes(element);
        Assert.state(!typesToMock.isEmpty(),
                "Unable to deduce type to mock from " + element);

        for (ResolvableType typeToMock : typesToMock) {
            System.out.println(typeToMock.getInterfaces());
            /*MockDefinition definition = new MockDefinition(annotation.name(), typeToMock,
                    annotation.extraInterfaces(), annotation.answer(),
                    annotation.serializable(), annotation.reset(),
                    QualifierDefinition.forElement(element));
            addDefinition(element, definition, "mock");*/
        }
    }

    private Set<ResolvableType> getOrDeduceTypes(AnnotatedElement element) {
        Set<ResolvableType> types = new LinkedHashSet<ResolvableType>();
        if (types.isEmpty() && element instanceof Field) {
            types.add(ResolvableType.forField((Field) element));
        }
        return types;
    }



    public void addBeanDefinition(Class c) {
        Object o = Mockito.mock(c);
        AspectJProxyFactory factory = new AspectJProxyFactory(o);
        MyAspect aspect = new MyAspect();
        factory.addAspect(aspect);
        beanDefinitions.put(c.getName(), factory.getProxy());
    }

    private Set<Class<?>> getConfigurationClasses(
            ConfigurableListableBeanFactory beanFactory) {
        Set<Class<?>> configurationClasses = new LinkedHashSet<Class<?>>();
        for (BeanDefinition beanDefinition : getConfigurationBeanDefinitions(beanFactory)
                .values()) {
            configurationClasses.add(ClassUtils.resolveClassName(
                    beanDefinition.getBeanClassName(), this.classLoader));
        }
        return configurationClasses;
    }

    private Map<String, BeanDefinition> getConfigurationBeanDefinitions(
            ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanDefinition> definitions = new LinkedHashMap<String, BeanDefinition>();
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition definition = beanFactory.getBeanDefinition(beanName);
            if (definition.getAttribute(CONFIGURATION_CLASS_ATTRIBUTE) != null) {
                definitions.put(beanName, definition);
            }
        }
        return definitions;
    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
