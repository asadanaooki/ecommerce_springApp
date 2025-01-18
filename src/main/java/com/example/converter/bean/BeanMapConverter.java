package com.example.converter.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * JavaBeanをMapやMultiValueMapに変換するクラス
 */
public class BeanMapConverter {

    /**
     * 指定されたJavaBeanのプロパティをMapに変換します。
     * 
     * @param bean 変換対象のJavaBeanオブジェクト
     * @return プロパティ名をキー、プロパティ値をバリューとするMap
     */
    public static Map<String, Object> beanToMap(Object bean) {
        Map<String, Object> map = new HashMap<>();

        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            Set<Map.Entry<String, Object>> entries = beanMap.entrySet();
            entries.stream().forEach(e -> map.put(e.getKey(), e.getValue()));
        }
        return map;
    }

    /**
     * 指定されたJavaBeanのプロパティをMultiValueMapに変換します。
     *
     * @param bean 変換対象のJavaBeanオブジェクト
     * @return プロパティ名をキー、プロパティ値を文字列として追加したMultiValueMap
     */
    public static MultiValueMap<String, String> beanToMultiValueMap(Object bean) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            beanMap.forEach((key, value) -> {
                multiValueMap.add(key.toString(), value.toString());
            });
        }
        return multiValueMap;
    }
}
