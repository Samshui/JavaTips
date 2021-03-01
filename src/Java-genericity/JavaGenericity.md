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

## 泛型类型限定

`对泛型的类型进行限制`

> <font face="Fira Code">在特定的场合下，一个泛型需要被限制，比如在进行比较时，需要实现compareTo，因此就需要限定泛型的类型：</font>

```java
class Example {
    public static <T extends Comparable> T getMin(T... args) {
        if (args == null || args.length <= 0) return null;
        
        T min = args[0];
        
        for (var i : args) {
            if (min.compareTo(args[i]) > 0) min = args[i];
        }
        
        return min;
    } 
}
```

> <font face="Fira Code">
>   &lt;T extends E&gt;表示T必须是E的子类<br>
>   当然，限定的时候可以让T extends多个接口，使用如下的方式：<br>
>   <font color="#db7093">extends可以限定多个接口，但是只能限定一个类，而且类必须放在第一位</font><br>
> </font>

```java
import java.io.File;

// 统一使用extends而不使用implement
class Example {
    public static <T extends File & Cloneable & Comparable> doSomething() {
        // pass
    }
}
```

## 泛型继承原则

<span style="font-family: 'Fira Code'">
    Example&lt;T&gt;和Example&lt;S&gt;之间不存在任何联系，无论S与T之间有什么关系<br>
    但是，泛型类是可以扩展或者实现其他类的，比如ArrayList&lt;T&gt;实现List&lt;T&gt;，因为两者的泛型类型相同<br><br>
    <font color="red">也就是说，如果泛型类型不同，则两者（泛型类）之间是不存在关系的</font>
</span>

为了弥补泛型关系之间的不足，推出了**泛型通配符类型**

---
### 泛型通配符类型

`上限界定符`

<span style="font-family: 'Fira Code'">
    <font color="#db7093">OneClass&lt;? extends S&gt;<br></font> 
    表示OneClass所能接受的泛型类型必须是S本身或者是S的子类。不过要注意的是，在此只能使用get方法，而不能使用set方法，是因为get一定可以保证获取到的数据一定可以转为S，但是set方法就充满了不确定性，所以在此处只能使用get方法获取对象
</span>

`下限界定符`

<span style="font-family: 'Fira Code'">
    <font color="#db7093">OneClass&lt;? super S&gt;<br></font>
    表示OneClass所可以接受的泛型类型必须是S本身或者S的超类。同样要注意的是，在下限界定符中，可以使用set而不可以使用get方法，理由和上限界定符类似（相反）
</span>

↓

综述上面的两种界定符，我们可以得出一个原则：`泛型PECS原则`

> <font face="Fira Code"><strong>PECS原则：</strong><br>
>   Producer Extends, Consumer Super<br>
> 为了获取信息而不写入信息，使用 ?extends，泛型为生产者 - 输出信息<br>
> 为了写入信息而不获取信息，使用 ?super，泛型为消费者 - 写入信息<br>
> </font> 

**如果既想写入又想写出，不要使用通配符！**

> <font face="Fira Code">无限定通配符：Pair&lt;?&gt;，表示任意类型，也因此无法set，get也只能get出Object类的</font>

