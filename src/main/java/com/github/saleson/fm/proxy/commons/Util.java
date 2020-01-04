package com.github.saleson.fm.proxy.commons;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;


/**
 * @author saleson
 * @date 2020-01-03 18:44
 */
public class Util {

    public static boolean isDefault(Method method) {
        // Default methods are public non-abstract, non-synthetic, and non-static instance methods
        // declared in an interface.
        // method.isDefault() is not sufficient for our usage as it does not check
        // for synthetic methods. As a result, it picks up overridden methods as well as actual default
        // methods.
        final int SYNTHETIC = 0x00001000;
        return ((method.getModifiers()
                & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC | SYNTHETIC)) == Modifier.PUBLIC)
                && method.getDeclaringClass().isInterface();
    }

    public static void checkState(boolean expression,
                                  String errorMessageTemplate,
                                  Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalStateException(
                    String.format(errorMessageTemplate, errorMessageArgs));
        }
    }


    public static String generateMethodKey(Class targetType, Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(targetType.getSimpleName());
        builder.append('#').append(method.getName()).append('(');
        for (Type param : method.getGenericParameterTypes()) {
            param = Types.resolve(targetType, targetType, param);
            builder.append(Types.getRawType(param).getSimpleName()).append(',');
        }
        if (method.getParameterTypes().length > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.append(')').toString();
    }




}
