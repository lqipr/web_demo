package com.lqipr.util;

import com.lqipr.core.util.INIConfiguration;

import java.io.IOException;
import java.util.Map;

/**
 * Created by lqipr on 2015/12/29.
 */
public class INIConfigurationTest {
    public static void main(String[] args) {
        INIConfiguration configuration = new INIConfiguration();
        try {
            configuration.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("INIConfigurationTest.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> lqipr1 = configuration.getSection("lqipr1");

        System.out.println(lqipr1.get("name"));
        System.out.println(lqipr1.get("password"));
    }
}
