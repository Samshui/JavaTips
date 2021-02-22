# Java语法糖
`Syntactic Sugar`

---

常见的Java语法糖：

> <font face="Jetbrains Mono">·[for-each](#for-each)</font><br>
> <font face="Jetbrains Mono">·[enum](#enum)</font><br>
> <font face="Jetbrains Mono">·[不定项参数](#params)</font><br>
> <font face="Jetbrains Mono">·[静态导入](#import)</font><br>
> <font face="Jetbrains Mono">·[自动装箱与拆箱](#box)</font><br>
> <font face="Jetbrains Mono">·[多异常并列](#execption)</font><br>
> <font face="Jetbrains Mono">·[数值赋值优化](#assume)</font><br>
> <font face="Jetbrains Mono">·[接口方法](#interface)</font><br>

---

## <a id="for-each"><font face="Jetbrains Mono">for-each</font></a>
```java
// this is an example for 'for-each'

```

## <a id="enum"><font face="Jetbrains Mono">enum</font></a>
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

<font face="Jetbrains Mono" color="#db7093">
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

目的：避免二进制计算，适用于<font color="red" face="Jetbrains Mono">**byte、short、int、long**</font>

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

-使用范围：short、int、long、float、double
-下划线只能出现在数字中间，也即前后必须都有数字
-允许在2、8、10、16进制的数字中使用，下划线不可以分割开头

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