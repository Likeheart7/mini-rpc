package com.chenx.rpc.consumer;

import com.chenx.rpc.consumer.annotation.RpcReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chenx
 * @create 2023-08-23 15:02
 */
@Component
@Slf4j
public class RpcConsumerPostProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {
    private ApplicationContext applicationContext;
    private ClassLoader beanClassLoader;
    private final Map<String, BeanDefinition> rpcRefBeanDefinitions = new LinkedHashMap<>();

    /**
     * BeanFactoryPostProcessor#postProcessBeanFactory，在bean实例化前执行，可以修改beanDefinitionMap中的配置信息来达到修改bean配置的目的
     *
     * @param beanFactory beanFactory中有beanDefinitionMap、singletonObjects等
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
//            bean的定义信息
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
//            bean的类名
            String beanClassName = beanDefinition.getBeanClassName();

            if (beanClassName != null) {
//                通过类加载器，和类名加载该类的Class实例。
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.beanClassLoader);
//                doWithFields()作用是对类内的所有域调用传入的函数
                ReflectionUtils.doWithFields(clazz, this::parseRpcReference);
            }
        }
//        将rpcRefBeanDefinitions中所有的bean定义信息注册到beanDefinitionMap中(即容器存放bean定义的地方)
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        this.rpcRefBeanDefinitions.forEach((beanName, BeanDefinition) -> {
            if(applicationContext.containsBean(beanName)) {
                throw new IllegalArgumentException("spring context already has a bean named " + beanName);
            }
            registry.registerBeanDefinition(beanName, rpcRefBeanDefinitions.get(beanName));
            log.info("registered RpcReferenceBean [{}] success.", beanName);
        });
    }

    private void parseRpcReference(Field field) {
        RpcReference annotation = AnnotationUtils.getAnnotation(field, RpcReference.class);
//        如果一个域被@RpcReference修饰
        if (annotation != null) {
//        通过BeanDefinitionBuilder构造一个RpcReferenceBean的定义，并将它放到rpcRefBeanDefinitions中
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcReferenceBean.class);
            builder.setInitMethodName(RpcConstants.INIT_METHOD_NAME);
            builder.addPropertyValue("interfaceClass", field.getType());
            builder.addPropertyValue("serviceVersion", annotation.registryType());
            builder.addPropertyValue("registryType", annotation.registryType());
            builder.addPropertyValue("registryAddr", annotation.registryAddress());
            builder.addPropertyValue("timeout", annotation.timeout());
            BeanDefinition beanDefinition = builder.getBeanDefinition();
            rpcRefBeanDefinitions.put(field.getName(), beanDefinition);
        }

    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
