package frc.team4069.saturn.lib.mathematics

import edu.wpi.first.wpiutil.math.Matrix
import edu.wpi.first.wpiutil.math.Nat
import edu.wpi.first.wpiutil.math.Num
import edu.wpi.first.wpiutil.math.numbers.N0
import edu.wpi.first.wpiutil.math.numbers.N1
import org.ejml.dense.row.CommonOps_DDRM
import org.ejml.simple.SimpleMatrix

typealias Vector<D> = Matrix<D, N1>

operator fun <D: Num> Vector<D>.get(row: Int) = this[row, 0]
operator fun <D: Num> Vector<D>.set(row: Int, value: Double) {
    this[row, 0] = value
}

// Values
val `0` = Nat.N0()!!
val `1` = Nat.N1()!!
val `2` = Nat.N2()!!
val `3` = Nat.N3()!!
val `4` = Nat.N4()!!
val `5` = Nat.N5()!!
val `6` = Nat.N6()!!
val `7` = Nat.N7()!!
val `8` = Nat.N8()!!
val `9` = Nat.N9()!!
val `10` = Nat.N10()!!
val `11` = Nat.N11()!!
val `12` = Nat.N12()!!
val `13` = Nat.N13()!!
val `14` = Nat.N14()!!
val `15` = Nat.N15()!!
val `16` = Nat.N16()!!
val `17` = Nat.N17()!!
val `18` = Nat.N18()!!
val `19` = Nat.N19()!!
val `20` = Nat.N20()!!



fun <R: Num, C: Num> zeros(rows: Nat<R>, cols: Nat<C>): Matrix<R, C> = Matrix(SimpleMatrix(rows.num, cols.num))
fun <D: Num> zeros(size: Nat<D>): Matrix<D, N1> = Matrix(SimpleMatrix(size.num, 1))

fun <D: Num> eye(size: Nat<D>): Matrix<D, D> = Matrix(SimpleMatrix.identity(size.num))

fun <R: Num, C: Num> ones(rows: Nat<R>, cols: Nat<C>): Matrix<R, C> {
    val out = SimpleMatrix(rows.num, cols.num)
    CommonOps_DDRM.fill(out.ddrm, 1.0)
    return Matrix(out)
}
fun <D: Num> ones(size: Nat<D>): Matrix<D, N1> {
    val out = SimpleMatrix(size.num, 1)
    CommonOps_DDRM.fill(out.ddrm, 1.0)
    return Matrix(out)
}

fun <R : Num, C : Num> mat(rows: Nat<R>, cols: Nat<C>) = MatBuilder(rows, cols)

fun <D : Num> vec(dim: Nat<D>) = VecBuilder(dim)

class MatBuilder<R : Num, C : Num>(val rows: Nat<R>, val cols: Nat<C>) {
    fun fill(vararg data: Double): Matrix<R, C> {
        if (data.size != rows.num * cols.num) {
            throw IllegalArgumentException("Invalid matrix data provided. Wanted ${rows.num} x ${cols.num} matrix, but got ${data.size} elements")
        }

        val matData = data.toList().chunked(cols.num).map { it.toDoubleArray() }.toTypedArray()
        return Matrix(SimpleMatrix(matData))
    }

    fun fill(vararg data: Int): Matrix<R, C> {
        if (data.size != rows.num * cols.num) {
            throw IllegalArgumentException("Invalid matrix data provided. Wanted ${rows.num} x ${cols.num} matrix, but got ${data.size} elements")
        }

        val matData = data.map { it.toDouble() }.chunked(cols.num).map { it.toDoubleArray() }.toTypedArray()
        return Matrix(SimpleMatrix(matData))
    }
}

class VecBuilder<D : Num>(val dim: Nat<D>) {
    fun fill(vararg data: Double): Vector<D> {
        if (data.size != dim.num) {
            throw IllegalArgumentException("Invalid number of elements for ${dim.num}-dimensional vector. got ${data.size} elements")
        }

        return Matrix(SimpleMatrix(dim.num, 1, false, data))
    }

    fun fill(vararg data: Int): Vector<D> {
        if (data.size != dim.num) {
            throw IllegalArgumentException("Invalid number of elements for ${dim.num}-dimensional vector. got ${data.size} elements")
        }

        return Matrix(SimpleMatrix(dim.num, 1, false, data.map { it.toDouble() }.toDoubleArray()))
    }
}
