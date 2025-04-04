package com.codringreen.farmloading.view.adapter;

import android.util.Log;

import java.lang.reflect.Method;

public class ReflectHelper {
    public static void invokeMethodIfExists(String methodName, Object target, Class<?>[] paramTypes, Object... params) {
        try {
            target.getClass().getMethod(methodName, paramTypes).invoke(target, params);
        } catch (Exception e) {
            Log.e("ReflectHelper", "Error in ReflectHelper", e);
        }
    }

    public static void invokeMethodIfExists(String methodName, Object target, Object[] params) {
        Method method;
        boolean z;
        Method[] methods = target.getClass().getMethods();
        int length = methods.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                method = null;
                break;
            }
            method = methods[i];
            if (method.getName().equals(methodName)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != params.length) {
                    continue;
                } else {
                    int length2 = params.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length2) {
                            z = true;
                            break;
                        } else {
                            if (!parameterTypes[i2].isInstance(params[i2])) {
                                z = false;
                                break;
                            }
                            i2++;
                        }
                    }
                    if (z) {
                        break;
                    }
                }
            }
            i++;
        }
        if (method != null) {
            try {
                method.invoke(target, params);
            } catch (Exception e) {
                Log.e("ReflectHelper", "Error in ReflectHelper", e);
            }
        }
    }
}