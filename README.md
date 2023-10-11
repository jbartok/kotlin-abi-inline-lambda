# kotlin-abi-before-after

Run the reproducer: `./gradlew run --rerun-tasks`, you get:

```
ABI fingerprint of com/gradle/abi/InlineLambda$foo$aggregate$1.class is: INACCESSIBLE
ABI fingerprint of com/gradle/abi/InlineLambda.class is: -5224924079613941
Calling the function yields: foo = 4
```

Edit line 6 in `InlineLambda` (project `target`), replace `x + y` with `x - y`.

Run the reproducer: `./gradlew run --rerun-tasks`, you get:

```
ABI fingerprint of com/gradle/abi/InlineLambda$foo$aggregate$1.class is: INACCESSIBLE
ABI fingerprint of com/gradle/abi/InlineLambda.class is: -5224924079613941
Calling the function yields: foo = 0
```

The problem is that the ABI fingerprint for the `InlineLambda` class is the same in both cases.
We believe it shouldn't be, it should be different, because classes using the inline function `foo()` from it need to be recompiled.
