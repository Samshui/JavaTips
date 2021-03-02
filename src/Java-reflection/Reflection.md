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

### <a id="invoke">反射</a>

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

## 反射关键类

> <font face="Fira Code">关键类Class：类型标识<br>
> JVM为每个对象都保留了其类型标识信息RTI（Running Type Identification）
> </font>

```java
// 获取标识信息的方法
public class Example {
    public static void main(String[] args) {
        String s = "abc";
    
        Class c1 = s.getClass();
        Class c2 = Class.forName("java.lang.String");
        Class c3 = String.class;
    }
}
```

> <font face="Fira Code">关键类Class的一些方法<br>
> 1- getMethods() / getDeclaredMethods()<br>
> 2- getFields() / getDeclaredFields()<br>
> 3- getInterface()<br>
> 4- getPackage()<br>
> 5- getConstructors()<br>
> 6- getModifiers()<br>
> 7- getSuperClass()<br>
> 8- getAnnotation()
> </font>

### 成员变量
`Field`

```java
import java.lang.reflect.Field;

public class A {
    public int age;
    private String name;
    
    public A(int age, String name) {
        this.age = age;
        this.name = name;
    }
    
    public static void main(String[] args) throws IllegalAccessException {
        A a = new A(20, "Man");
        Class c = a.getClass();
    
        // getFields 只负责获得本类与父类所有可获得的（public）字段
        Field[] fields = c.getFields();
        System.out.println(fields.length); // 1
        System.out.println(fields[0].getName() + " : " + fields[0].get(a));
    
        // getDeclaredFields 负责获取本类所有的字段
        // private属性会临时变为public
        Field[] allFileds = c.getDeclaredFields();
        System.out.println(allFileds.length);
        for (var i : allFileds) {
            System.out.println(i.getName() + " : " + i.get(a));
        }
    }
}
```

### 成员方法
`Method`

[查看“反射”处的代码](#invoke)，其中的<font face="Fira Code">m.invoke(obj, null)就是方法的调用</font>

### 构造函数
`Constructor`

```java
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class A {
    private int num;
    
    public A() {
    }
    
    public A(int num) {
        this.num = num;
    }
    
    public void method() {
        System.out.println(num);
    }
    
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        A a = new A();
        Class c = a.getClass();
    
        Constructor[] cs = c.getConstructors();
        for (var i : cs) {
            if (i.getParameterCount() > 0) {
                // 有参构造函数
                A a1 = (A) i.newInstance(20);
                a1.method();
            } else {
                A a2 = (A) i.newInstance();
                a2.method();
            }
        }
    }
}
```

### 父类/父接口
`Super`

## 反射的应用

- <font face="Fira Code">JDBC 数据库连接</font>
- 数组扩充
- 动态执行方法
- <font face="Fira Code">Json和Java对象的互转</font>

### 数组扩充
`非原地扩充`

> <font face="Fira Code">在Java中，如果给定了一个数组，则数组的长度就是确定的，不可以再进行修改<br><br>
> 如果需要扩充数组的话，必须编写扩充器，也即：新建一个更大的同类型数组，并把旧数组的内容拷贝进去
> </font>

```java
import java.lang.reflect.Array;

public class Example {
    // 扩充器
    public static Object arrayLarger(Object oldArray, int newLength) {
        // get old array's type
        Class c = oldArray.getClass();
    
        // get item's type
        Class ic = c.getComponentType();
    
        // get old array's len
        int oldLength = Array.getLength(oldArray);
        
        // new Array
        Object newArray = Array.newInstance(ic, newLength);
        
        // copy datas
        System.arraycopy(oldArray, 0, newArray, 0, oldLength);
        
        // return new Array
        return newArray;
    }
    
    public static void main(String[] args) {
        int[] a = {1,2,3,4,5};
        a = (int[]) arrayLarger(a, 10);
        
        for (var i : a) System.out.println(i);
    }
}
```

### 动态执行方法

> 动态执行方法：给定类名、方法名就可以执行，同时也可以加上定时器，完成定时的任务执行

```java
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

class Worker {
    public static void hello() {
        System.out.println("hello");
    }
}

public class MyTask extends TimerTask {
    @Override
    public void run() {
        try {
            // 反射调用Worker类中的hello方法
            Method m = Class.forName("Worker").getClass().getMethod("hello");
            // 静态方法不需要new对象，直接反射到类名就可以直接运行
            m.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        Timer timer = new Timer();
    
        Calendar now = Calendar.getInstance();
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) + 1);
    
        Date runDate = now.getTime();
        MyTask myTask = new MyTask();
    
        timer.scheduleAtFixedRate(myTask, runDate, 3000);
    }
}
```

### Json和Java对象的互转
`使用Google的库`