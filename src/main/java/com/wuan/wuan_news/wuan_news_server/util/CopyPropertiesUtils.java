package com.wuan.wuan_news.wuan_news_server.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: wuan_news_server
 * @description:
 * @author: Jingzhen
 * @create: 2024-07-01 16:57
 **/
public class CopyPropertiesUtils {
    public static void copyNonNullProperties(Object source, Object target) {
        BeanWrapper src = new BeanWrapperImpl(source);
        BeanWrapper trg = new BeanWrapperImpl(target);

        Set<String> targetPropertyNames = Stream.of(trg.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .collect(Collectors.toSet());

        Stream.of(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                // 仅保留目标对象中存在的属性
                .filter(targetPropertyNames::contains)
                // 排除 class 属性
                .filter(propertyName -> !"class".equals(propertyName))
                .filter(propertyName -> src.getPropertyValue(propertyName) != null)
                .forEach(propertyName -> trg.setPropertyValue(propertyName, src.getPropertyValue(propertyName)));
    }
}
