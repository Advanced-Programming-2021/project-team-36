package YuGiOh.network.serializers;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodSerializer implements Serializable {
    private final String className;
    private final String methodName;
    private final String[] parameterClasses;

    public MethodSerializer(Method method) {
        className = method.getDeclaringClass().getCanonicalName();
        methodName = method.getName();
        parameterClasses = Arrays.stream(method.getParameterTypes()).map(Class::getCanonicalName).toArray(String[]::new);
    }
    public Method getMethod() {
        Class<?>[] parameters = Arrays.stream(parameterClasses).map(name -> {
            try {
                return Class.forName(name);
            } catch (Exception e) {
                throw new Error("error while deserializing method");
            }
        }).toArray(Class[]::new);
        try {
            return Class.forName(className).getDeclaredMethod(methodName, parameters);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("error while deserializing method");
        }
    }
    public Class<?> getInvokingClass() {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("error while deserializing method");
        }
    }
}
