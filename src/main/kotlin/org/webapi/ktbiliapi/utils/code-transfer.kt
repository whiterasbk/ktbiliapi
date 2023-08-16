package org.webapi.ktbiliapi.utils

// 此程序非完全原创，改编自GH站内某大佬的Java程序，修改了部分代码，且转换为Kotlin
// 算法来源同上

private const val add = 8728348608L // 十进制时加减数 2
private const val xor = 177451812L // 二进制时加减数1
private const val table = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF"
private val magicArray = intArrayOf(11, 10, 3, 8, 4, 6, 2, 9, 5, 7) // 这里是由知乎大佬不知道用什么方法得出的转换用数字

// 也许这样会比底层那种用 double 的快吧
private fun power(a: Int, b: Int): Long {
    var power: Long = 1
    for (c in 0 until b) power *= a.toLong()
    return power
}

class AvConvertException(message: String?, val originException: Exception): Exception(message)

class BvConvertException(message: String?, val originException: Exception): Exception(message)

val String.av: Long
    get() {
        try {
            val map = HashMap<String, Int>()
            var r: Long = 0
            // 58进制转换
            for (i in 0..57)
                map[table.substring(i, i + 1)] = i

            for (i in 0..5)
                r += map[this.substring(magicArray[i], magicArray[i] + 1)]!! * power(58, i)

            //转换完成后，需要处理，带上两个随机数
            return r - add xor xor
        } catch (e: Exception) {
            // 包一层错误
            throw AvConvertException(e.message, e)
        }
    }

val Long.bv: String
    get() {
        try {
            val map = HashMap<Int, String>()
            val sb = StringBuffer("BV1  4 1 7  ")
            // 逆向思路，先将随机数还原
            val new = (this xor xor) + add
            // 58 进制转回
            for (i in 0..57)
                map[i] = table.substring(i, i + 1)

            for (i in 0..5)
                sb.replace(magicArray[i], magicArray[i] + 1, map[(new / power(58, i) % 58).toInt()]!!)

            return sb.toString()
        } catch (e: Exception) {
            // 包一层错误
            throw BvConvertException(e.message, e)
        }
    }