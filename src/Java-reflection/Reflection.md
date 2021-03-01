# Java反射

`java reflection`

---

<span style="font-family: 'Fira Code'">
<strong>前景问题：</strong><br>
经常使用idea进行编程的程序员应该都知道，编辑器会进行智能联想，对于一个自己编写的类，也可以联想出相关的方法名称。那么，是如何实现这样智能联想的功能？所用到的技术就是Java反射。
</span>

`概念`
> <font face="Fira Code">
> 反射：指程序可以访问、检测和修改它本身的状态或行为的能力（自描述、自控制）<br><br>
> 反射使得java可以在运行时加载使用在编译期间完全未知的类，使得作为强类型语言的java有了动态语言的特性，不在变得死板
> </font>

`反射的功能`

<span style="font-family: 'Fira Code'">

1. 在运行中分析类
2. 在运行中查看和操作对象
  + 基于反射自由创建对象
  + 通过反射构建无法直接访问的类
    * 比如构造函数为private，一般情况下无法new出一个对象，但是通过反射可以实现创建对象
  + set或者get到无法访问的成员变量（private修饰）
  + 调用不可访问的方法（private修饰）
3. 实现通用的数组操作代码
  + java数组的长度在new时就确定了，通过反射可以重新修改数组的长度
4. 实现函数指针的类似功能

</span>

## Java创建对象的若干方法

### 静态编码 & 编译

> 最为普通的一种方法，通过new调用类的构造函数来实现创建新对象

```java
class Example {
    public static void main(String[] args) {
        Example example = new Example();
    }
}
```

### 克隆
`⛳需要实现Cloneable接口`

> <font face="Fira Code">克隆是一种比new出来之后对每个属性进行set更快的方法</font>

```java
public class Example implements Cloneable {
    public void method() {
        System.out.println("here");
    }
    
    // 实现接口方法
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public static void main(String[] args) {
        Example a = new Example();
        a.method();
        
        Example b = (Example) a.clone();
        b.method();
    }
}
```

### 序列化与反序列化
`serialization & deserialization`

`⛳需要实现Serializable接口`

> 序列化：将对象输出为文件流，变成一个文件，在需要创建对象的时候，将该文件读入，重新生成一个对象

```java
import java.io.*;

public class Example implements Serializable {
    // 只要求有这个，不要求实现什么方法
    private static final long serialVersionUID = 1L;
    
    public void methon() {
        System.out.println("here");
    }
    
    public static void main(String[] args) {
        Example a = new Example();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.obj"));
        out.writeObject(a);
        out.close();
    
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.obj"));
        Example b = (Example) in.readObject();
        in.close();
        
        b.methon();
    }
}
```

**注意：序列化会引起安全漏洞，将会被移除，谨慎使用！**

### 反射

```java
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
```

