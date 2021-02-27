# Java泛型
`Java Genericity - JDK 1.5`

---

## 泛型的基本介绍

<span style="font-family: 'Fira Code'">
    简单来说，泛型就是指编写的代码可以被很多不同类型的对象所重复使用
</span>

```java
import java.util.ArrayList;

class Example {
    public static void main(String[] args) {
        ArrayList<String > list = new ArrayList<String>();
    }
}
```
<span style="font-family: 'Fira Code'">
    上面的代码段中，ArrayList用一个&lt;String&gt;来指定其所可以包含的数据类型只能为String，当然也可以使用对应的类型来指定list可存放的数据类型，也即不修改ArrayList的代码，通过识别&lt; &gt;中的类型就可以实现就可以实现任何类型的list，这便是泛型。
</span>

> 泛型包括许多类型：
> > <font face="Fira Code">
> >  ·泛型类：ArrayList,HashSet,HashMap<br>
> >  ·泛型方法：Collections.binarySearch,Arrays.sort<br>
> >  ·泛型接口：List,Iterator<br>
> > </font>
> 泛型的本质其实就是：参数化类型、避免类型的转换以及保证代码可以进行复用

```java
// 一个基本的泛型例子
public class ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
    // ...自行查看
}
```

## 设计（自定义）泛型

### 泛型类

> <font face="Fira Code">泛型类即指一个具有泛型变量的类，在类名后加上&lt;T&gt;表示引入类型。当然，也可以使用多个不同的字母表示引入多个类型，例如&lt;T, U&gt;。</font>

```java
class MyTClass<T> {
    private T myFirstT;
    private T mySecondT;
    
    public print(T first, T second) {
        System.out.println("first: " + first.toString() + ";" + "second: " +second.toString());
    }
    
    // 其余自定义的方法
}

class Example {
    public static void main(String[] args) {
        // 使用自定义泛型
        MyTClass<String> myTClass = new MyTClass<>();
        myTClass.print("one", "second");
    }
}
```

一些规定的常用的字母表示含义：

<span style="font-family: 'Fira Code'">

| 字母 | 常用含义 |
|:---: | :---: |
|E|常用于表示元素，如ArrayList<E>|
|K, V|K常用来表示Key，V表示Value|
|T|T表示Template，自定义泛型常用|

</span>

### 泛型方法

> 泛型方法是指具有泛型参数的方法，这种方法可以放在普通类中和泛型类中

```java
class Example {
    // 注意T的编写位置，<引入类型> + 返回类型 + 方法名 + 参数
    public static <T> T getMiddle(T... args) {
        return args[args.length / 2];
    }

    public static void main(String[] args) {
        // 规范写法
        int result_ = Example.<int>getMiddle(1, 2, 3, 4, 5);
        
        // 拓展写法（省去<int>，让java自行判断）
        int result = Example.getMiddle(1, 2, 3, 4, 5);
    }
}
```

### 泛型接口

> <font face="Fira Code">在类名后加&lt;T&gt;，在具体实现接口的时候，需要指定类型</font>

```java
public interface TInterface<T> {
    public T say(T param);
}

public class Example implements TInterface {
    public String say(String param) {
        return param;
    }
	
    public static void main(String[] args) {
        // do sth.
    }
}
```

> <font face="Fira Code">注意，T也可以本身就是一个泛型，所以在最终调用的时候，也许会出现&lt;T&lt;E&gt;&gt;这样的形式</font>

