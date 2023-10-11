package com.gradle.abi

class InlineLambda {

    inline fun foo() {
        val aggregate: (Int, Int) -> Int = { x, y -> x + y }
        println("foo = " + aggregate(2, 2))
    }

}