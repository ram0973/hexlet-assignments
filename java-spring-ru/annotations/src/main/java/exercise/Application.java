package exercise;

import exercise.model.Address;
import exercise.annotation.Inspect;
import java.lang.reflect.Method;

public class Application {
    public static void main(String[] args) {
        var address = new Address("London", 12345678);

        // BEGIN
        for (Method method : Address.class.getMethods()) {
            if (method.isAnnotationPresent(Inspect.class)) {
                String methodName = method.getName();
                Class<?> methodClass = method.getReturnType();
                System.out.printf("Method %s returns a value of type %s\n.", methodName, methodClass.getSimpleName());
            }
        }
        // END
    }
}
