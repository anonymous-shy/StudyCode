#### Scala编程实战
Scala中不推荐使用 null ！使用 Option 代替
foreach 与 map 区别：foreach不返回任何值
##### 第一章，字符串
1.Scala使用 "==" 判断字符串相等
2.
##### 第二章，数值
数值都是对象
Byte，Char，Double，Float，Int，Long，Short，Boolean

```
<!-- https://mvnrepository.com/artifact/com.github.nscala-time/nscala-time_2.11 -->
 <dependency>
     <groupId>com.github.nscala-time</groupId>
     <artifactId>nscala-time_2.11</artifactId>
     <version>2.16.0</version>
 </dependency>
```

字符串到数值 toInt，toDouble...
##### 第一章，控制结构

#### 数组
> **使用边长数组**  要不带(),要不用new 
> val ab = ArrayBuffer[Int]()   
> val b = new ArrayBuffer[String]

```apple js
scala> val nums = new Array[Int](10)
nums: Array[Int] = Array(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

scala> val s = new Array[String](10)
s: Array[String] = Array(null, null, null, null, null, null, null, null, null, null)

scala> val s1 = Array("Hello", "World")
s1: Array[String] = Array(Hello, World)

scala> s1(0)
res0: String = Hello

scala> s1(0) = "Dilraba"

scala> s1
res2: Array[String] = Array(Dilraba, World)

scala> import collection.mutable.ArrayBuffer
import collection.mutable.ArrayBuffer

scala> val ab = ArrayBuffer[Int]()
ab: scala.collection.mutable.ArrayBuffer[Int] = ArrayBuffer()

scala> ab += 1
res3: ab.type = ArrayBuffer(1)

scala> ab += (3,5,7,9)
res4: ab.type = ArrayBuffer(1, 3, 5, 7, 9)

scala> ab
res5: scala.collection.mutable.ArrayBuffer[Int] = ArrayBuffer(1, 3, 5, 7, 9)

scala> val b = new ArrayBuffer[String]
b: scala.collection.mutable.ArrayBuffer[String] = ArrayBuffer()

scala> b += "Shy"
res6: b.type = ArrayBuffer(Shy)

scala> b ++= Array("Dilraba","Emma","Taylor")
res9: b.type = ArrayBuffer(Shy, Dilraba, Emma, Taylor)

scala> ab ++= Array(2,4,6,8,10)
res10: ab.type = ArrayBuffer(1, 3, 5, 7, 9, 2, 4, 6, 8, 10)

scala> ab
res11: scala.collection.mutable.ArrayBuffer[Int] = ArrayBuffer(1, 3, 5, 7, 9, 2, 4, 6, 8, 10)

scala> ab.trim
trimEnd   trimStart

scala> ab.trimEnd(5)

scala> ab
res13: scala.collection.mutable.ArrayBuffer[Int] = ArrayBuffer(1, 3, 5, 7, 9)

scala> b.insert(0,"AnonYmous")

scala> b
res16: scala.collection.mutable.ArrayBuffer[String] = ArrayBuffer(AnonYmous, Shy, Dilraba, Emma, Tay
lor)

scala> b.insert(2,"aa","bb","cc")

scala> b
res18: scala.collection.mutable.ArrayBuffer[String] = ArrayBuffer(AnonYmous, Shy, aa, bb, cc, Dilrab
a, Emma, Taylor)

scala> b.remove(2)
res19: String = aa

scala> b
res20: scala.collection.mutable.ArrayBuffer[String] = ArrayBuffer(AnonYmous, Shy, bb, cc, Dilraba, E
mma, Taylor)

scala> b.remove(2,3)

scala> b
res22: scala.collection.mutable.ArrayBuffer[String] = ArrayBuffer(AnonYmous, Shy, Emma, Taylor)
//有时需要构建一个Array,但不知道需要装多少个元素.先构造一个数组缓冲,然后toArray
scala> b.toArray 
res23: Array[String] = Array(AnonYmous, Shy, Emma, Taylor)
```

#### 映射&元祖
