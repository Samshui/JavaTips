import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class A {
	public void method() {
		System.out.println("here");
	}

	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		// 方法1
		Object a = Class.forName("A").newInstance();
		Method m = Class.forName("A").getMethod("method");
		m.invoke(a); // 输出：here

		// 方法2
		A b = (A) Class.forName("A").newInstance();
		b.method(); // 输出：here

		// 方法3
		Constructor<A> constructor = A.class.getConstructor();
		A c = constructor.newInstance();
		c.method(); // 输出：here
	}
}
