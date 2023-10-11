# kotlin-abi-inline-lambda

This simple reproducer attempt to showcase a problem with determining the ABI fingerprint of a class like the following. 

```kotlin
class InlineLambda {

    inline fun foo() {
        val aggregate: (Int, Int) -> Int = { x, y -> x + y }
        println("foo = " + aggregate(2, 2))
    }

}
```

More specifically: if the `aggregate` lambda is changed in the `foo()` function, the ABI fingerprint of the class should also change.
The reasoning is that other classes using the `foo()` function will need to be recompiled, due to `foo()` being an inline function.

To demonstrate run the reproducer (`./gradlew run --rerun-tasks`), you get:

```
ABI fingerprint of com/gradle/abi/InlineLambda$foo$aggregate$1.class is: INACCESSIBLE
ABI fingerprint of com/gradle/abi/InlineLambda.class is: -5224924079613941
Calling the function yields: foo = 4
```

Edit the `aggregate` function in `InlineLambda` (project `target`), replace `x + y` with `x - y`.

Run the reproducer again (`./gradlew run --rerun-tasks`), you get:

```
ABI fingerprint of com/gradle/abi/InlineLambda$foo$aggregate$1.class is: INACCESSIBLE
ABI fingerprint of com/gradle/abi/InlineLambda.class is: -5224924079613941
Calling the function yields: foo = 0
```

> **_NOTE:_** One weird thing I noticed is that if I don't just edit the aggregate function, but use comments, 
> then the result is different.
>
> So switching this
> ```kotlin
> val aggregate: (Int, Int) -> Int = { x, y -> x + y }
> // val aggregate: (Int, Int) -> Int = { x, y -> x - y }
> ```
> to
> ```kotlin
> // val aggregate: (Int, Int) -> Int = { x, y -> x + y }
> val aggregate: (Int, Int) -> Int = { x, y -> x - y }
> ```
> behaves differently than editing. That can't be good...