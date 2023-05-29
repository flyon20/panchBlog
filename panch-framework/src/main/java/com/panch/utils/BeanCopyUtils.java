package com.panch.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    //拷贝对象，将source中的属性值拷贝到target中
    public static <T> T copyBean(Object source, Class<T> clzz) {
        T result = null;
        try {
            result = clzz.newInstance();
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //拷贝集合,将list中的对象属性值拷贝到clzz类型的对象中
    public static <O,V> List<V> copyBeanList(List<O> list,Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
}
