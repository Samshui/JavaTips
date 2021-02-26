# Java语法糖
`Syntactic Sugar`

---

常见的Java语法糖：

> <font face="Fira Code">·[for-each](#for-each)</font><br>
> <font face="Fira Code">·[enum](#enum)</font><br>
> <font face="Fira Code">·[不定项参数](#params)</font><br>
> <font face="Fira Code">·[静态导入](#import)</font><br>
> <font face="Fira Code">·[自动装箱与拆箱](#box)</font><br>
> <font face="Fira Code">·[多异常并列](#execption)</font><br>
> <font face="Fira Code">·[数值赋值优化](#assume)</font><br>
> <font face="Fira Code">·[接口方法](#interface)</font><br>
> <font face="Fira Code">·[try-with-resource](#twr)</font><br>
> <font face="Fira Code">·[var类型](#var)</font><br>

---

## <a id="for-each"><font face="Fira Code">for-each</font></a>
```java
// this is an example for 'for-each'

```

## <a id="enum"><font face="Fira Code">enum</font></a>
```java
// this is an example for 'enum'
```

## <a id="params">不定项参数（可变参数）</a>
> 普通函数的参数要求是固定的个数、类型与顺序<br><br>
> **不定项参数**从`JDK5`开始

`使用`
```java
class SugarExample() { 
    // String(type)... sth.
    public static void print(String... args) {
        for (String arg : args) 
            System.out.println(arg);
    }
    
    /* ↓ */
    public static void main(String[] args) {
    	print("one");
    	print("one", "two");
    	print("one", "two", "three");
    }
}
```

**可变参数的本质就是数组**

`🚩注意`
> <font color="red">一个方法只能有一个不定项参数，且必须位与参数列表的最后</font><br>
> 重载时的优先级规则也发生了变化
> > 固定参数的方法比可变参数方法的优先级更高<br>
> > 如果调用时同时与两个带有可变参数的方法匹配，则报错

```java
class Example() { 
    public static void print(String... args) {
        for (String arg : args)
            System.out.println(arg + arg.length());
    }
    
    public static void print(String s) {
	    System.out.println(s);
    }
    
    // 错误示范
    /* public static void print(int... args_1, String... args_2) { } */
    /* public static void print(String s, String... args) {} -> 由于print的存在相矛盾 */
    
    public static void main(String[] args) {
        // 如果print调用时只有一个参数，则会优先选择固定参数方法
        print("one");       // one
        print("one", "two");// one3 two3
    }
}
```

## <a id="import">静态导入</a>
`import static - 便于简写`

<font face="Fira Code" color="#db7093">
    import导入程序所需要的类<br>
    import static导入一个雷的静态方法or静态变量
</font>

```java
// example
import static java.lang.Math.pow;
import static java.lang.System.out;

class Example() {
	public static void main(String[] args) {
        int c = pow(1, 2);
        out.println(c);
	}
}
```

`🚩注意`
> 建议在import时少使用*通配符，最好具体到需要导入的某个类的方法或静态变量<br>
> 如果重名时，需要加上类名

## <a id="box">自动装箱与拆箱</a>
`auto boxing | unboxing`

引入该功能的目的是为了简化基本类型与对象转换的写法

<span style="font-family: 'JetBrains Mono'">

| 基本类型（内容） | 对象（指针、内存） |
| :---: | :---: |
| boolean | Boolean |
| byte | Byte |
| char | Character |
| int | Integer |
| short | Short |
| long | Long |
| float | Float |
| double | Double |

</span>

`解释`
- 装箱：基本类型的值被封装成一个包装类对象
- 拆箱：一个包装类对象被拆开并获取相应的值

`示例`
```java
class Boxing() {
	public static void main(String[] args) {
        // 不使用装箱
        Integer i1 = Integer.valueOf(5);
        // 自动装箱
        Integer i2 = 5;
        
        // 不使用拆箱
        int i3 = i1.intValue();
        // 自动拆箱
        int i4 = i1;
	}
}
```

`🚩注意`
> 装箱和拆箱发生在**编译器**中，在class中已经添加转化，虚拟机不含有自动装箱和拆箱的语句<br><br>
> 基本类型没有null，但是对象有，需要注意转换时是否会产生`NullpointerException`

- 基础数据类型和封装的对象进行运算时，会先自动拆箱，然后对基础数据类型进行运算
- 一般来说，最好不要使用多个非同类的数值类对象进行运算

> 特别的是，进行`==`运算时，如果有至少一方是基本类型，则比较的是内容（数值），而非指针（内存），也即先进行拆箱<br>
> 如果两方都是对象的话，比较的则是指针是否指向同一个位置
> 
> 在使用`equals`方法是，由于要求参数为对象，且比较得到的结果是**类同+内容同**，因此会进行包装

## <a id="execption">多异常并列</a>
`JDK7`

即：将多个异常并列在一个catch中

```java
import java.io.IOException;
import java.sql.SQLException;

// 不使用多异常并列
class Example() {
	public static void main(String[] args) {
		try {
			// pass
		} catch (IOException ex) {
			// do sth.
		} catch (SQLException ex) {
			// do the same thing.
		}
	}
}

// 使用多异常并列 -> 管道符号的使用
class Example() {
	public static void main(String[] args) {
        try {
        	// pass
        } catch (IOException | SQLException ex) {
        	// do sth.
        }
	}
}
```

`🚩注意`
> 并列在一个catch中的多个异常之间**不能有直接/间接的基础关系**
> ```java
> // 错误展示
> class ErrorExample() {
>   public static void main() {
>       try {
>           // pass
>       } 
>       catch (IOException | FileNotFoundException ex) {
>        	/** @warning 该两种异常之间为继承的关系，不可以这样使用 **/
>       }
>   }
> }
> ```

## <a id="assume">数值赋值优化</a>
`JDK7 - 整数类型使用二进制数赋值`

目的：避免二进制计算，适用于<font color="red" face="Fira Code">**byte、short、int、long**</font>

`使用`
```java
class Example() {
	public static void main(String[] args) {
		// 0010-0001
		byte a1 = (byte) 0b00100001;
		// 1010-0001-0100-0101
        short a2 = (short) 0b1010000101000101;
        // 1010-0001-0100-0101-1010-0001-0100-0101
        int a3 = (int) 0b10100001010001011010000101000101;
	}
}
```
---
`下划线`

这个真的超好用！
```java
class Example() {
	public static void main(String[] args) {
        int a1 = 0b0111_1011_0001;
        long a2 = 9_999_999_999L;
	}
}
```

这种方法来赋值，增加了数字的可读性与纠错功能

- 使用范围：short、int、long、float、double
- 下划线只能出现在数字中间，也即前后必须都有数字
- 允许在2、8、10、16进制的数字中使用，下划线不可以分割开头

```java
// 正确使用：
class RightExample() {
	public static void main(String[] args) {
        int i1 = 0b0111_1011_0001;// 二进制，0b开头
        int i2 = 02_014;// 八进制，0开头
        int i3 = 0x7_B_1;// 十六进制，0x开头
        
        int i4 = 12345;
        int i5 = 123_45;
        int i6 = 123__45;
        
        float f1 = 3.14_159f;
        double d1 = 1.4_14159;
	}
}
```

## <a id="interface">接口方法</a>
`JDK8 plus lambda表达式`

> 在最初的java接口中，所有的接口都是没有实现的、公开的<br><br>
> 在`Java 8`时，推出的接口的默认方法/静态方法都是可带有实现的，而且开始支持Lambda表达式<br><br>\
> 在`Java 9`时，接口拥有了私有方法

```java
// JDK1~7
public interface Animal {
	public void move();
}

// JDK8~?
public interface NewAnmail {
	public default void move() {
		System.out.println("I can move");
    }
}
```

`🚩注意`

<span style="font-family: 'JetBrains Mono'">
    对于接口的默认方法：<br>
    1. 默认方法以<font color="#db7093">default</font>作为关键字的标注<br>
    2. 默认方法不允许重写Object中的方法，如equals、toString等<br>
    3. 具体实现类可以继承、重写父接口的默认方法<br>
    4. 接口可以继承、重写父接口的默认方法<br>
    5. 为了向前兼容，如果一个类的父类和父接口都有（同名同参数的）默认方法，子类会继承父类的默认方法<br>
    6. 如果子类实现了两个接口（均含有同名同参数的默认方法），则会导致编译失败，需要在子类中重写这个default方法
</span>

`默认方法-示例`

```java
public interface Animal {
	public void move();
}

public interface NewAnimal {
	public void move() {
		System.out.println("moving");
    }
}

public class Lion implements Animal, NewAnimal {
	public static void main(String[] args) {
        new Lion().move();
	}
	
	// 当实现的两个接口都含有同名方法，且至少有一个是默认方法时
    // 子类需要重写该方法，以免歧义
    public void move() {
		// 设置为默认方法的形式
	    NewAnimal.super.move();
    }
}
```

[![yqjfnf.png](https://s3.ax1x.com/2021/02/23/yqjfnf.png)](https://imgchr.com/i/yqjfnf)

`静态方法-注意`

> 静态方法属于接口，不属于子类或者子接口，也即子类/子接口没法继承该静态方法，只能通过所在的接口名称来调用

`私有方法`

> 为了解决多个默认方法/静态方法的内容重复的问题<br><br>
> 私有方法只属于本接口，只在本接口内使用，不属于子类/子接口；子类/子接口没有继承该私有方法，也无法调用<br><br>
> 静态私有方法可以被静态/默认方法调用，非静态私有方法被默认方法调用

## <a id="twr"><font face="Fira Code">try-with-resource</font></a>

`类似python的with语句`

> <font face="Fira code">try-with-resource语句简化了try-catch-finally</font>

<span style="font-family: 'Fira Code'">
    使用try-with-resource，可以保证在使用完资源后，将资源自动关闭
</span>

```java
import java.io.FileInputStream;

class Example() {
	public static void main(String[] args) {
		// 使用try-catch-finally
		FileInputStream fis = "**/**.json";
		try { 
			/* pass */ 
		} catch (Exception e) {
			/* do sth. */
		} finally {
			// 关闭资源
			if (fis != null) fis.close();
		}
		
		// 使用try-with-resource
        try(FileInputStream newFis = "**/**/txt") {
        	/* pass */
        } catch (Exception e) {
        	/* do sth. */
        }
	}
}
```

`🚩注意`

<span style="font-family: 'Fira Code'">
    <font color="#db7093">JDK7</font> 如果使用try-with-resource，则必须保证资源定义在try中。如果已经在外面定义，则需要一个本地变量。
</span>

```java
class Example() {
	public static void main(String[] args) {
		FileInputStream fis = "**/**.json";
		
		try(FileInputStream fis_2 = fis) {}
		catch (Exception e) {}
	}
}
```

<span style="font-family: 'Fira Code'">
    <font color="#db7093">JDK9</font> 不再要求定义临时变量，可以直接使用外部资源变量。
</span>

```java
class Example() {
	public static void main(String[] args) {
		FileInputStream fis = "**/**.json";
		
		try(fis) {}
		catch (Exception e) {}
	}
}
```

`🌊原理`
> <font face="Fira Code">
>   如果需要使用try-with-resource语句，则需要保证资源对象实现类AutoCloseable接口，也即实现close方法<br><br>
> <code style="color: palevioletred">public class FileInputStream extends InputStream</code><br>
> <code style="color: palevioletred">public abstract class InputStream implements Closeable</code>
> </font>

`使用`

```java
class Example {
	public static void main(String[] args) {
        try (MyResource m = new MyResource()) {
        	// do sth.
        } catch (Exception e) {
        	// then
        }
	}
}

class MyResource implements AutoCloseable {
	 public void doSomething() throws Exception {
		 System.out.println("do sth.");
     }
    
     public void close() throws Exception {
	     System.out.println("Close...");
     }
}
```

## <a id="var"><font face="Fira Code">var类型</font></a>
`Java 10`

> <div style="font-family: 'Fira Code'">
>   在java10之前，java都是一种强类型的程序语言
> </div>
```java
// 强类型指
class Example {
	int a = 1;
	double b = 1.0;
}
```

> <div style="font-family: 'Fira Code'">
>   在java10之后，推出局部变量推断，在一定程度上可以避免信息冗余<br><br>
>   不过本质上java还是强类型的语言，由编译器负责推断类型写入字节码文件，推断完成后不可更改
> </div>
```java
class Example {
	public static void main(String[] args) {
        var a = 5;
	}
}
```

`🚩注意`

<div style="font-family: 'Fira Code'">
1. var可以用在局部变量上，而非类成员变量
</div>