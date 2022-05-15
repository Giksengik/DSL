package interpreter

import java.lang.IllegalArgumentException
import kotlin.math.pow

class BinaryOperatorProcessorImpl : BinaryOperatorsProcessor {

    override fun processDegree(lOperand: Operand, rOperand: Operand): Operand {
        val operands = (lOperand to rOperand)
        when {
            operands.checkOperandsTypes<Int, Int>() -> return Operand(
                value = lOperand.getIntValue().toDouble().pow(rOperand.getIntValue().toDouble()).toInt().toString(),
                variableType = VariableType.Number,
            )
            else -> operands.throwUnexpectedTypes("degree")
        }
    }

    override fun processMultiply(lOperand: Operand, rOperand: Operand): Operand {
        val operands = (lOperand to rOperand)
        when {
            operands.checkOperandsTypes<Int, Int>() -> return Operand(
                value = (lOperand.getIntValue() * rOperand.getIntValue()).toString(),
                variableType = VariableType.Number,
            )
            else -> operands.throwUnexpectedTypes("multiply")
        }
    }

    override fun processDiv(lOperand: Operand, rOperand: Operand): Operand {
        val operands = (lOperand to rOperand)
        when {
            operands.checkOperandsTypes<Int, Int>() -> return Operand(
                value = (lOperand.getIntValue() / rOperand.getIntValue()).toString(),
                variableType = VariableType.Number,
            )
            else -> operands.throwUnexpectedTypes("divide")
        }
    }

    override fun processPlus(lOperand: Operand, rOperand: Operand): Operand {
        val operands = (lOperand to rOperand)
        when {
            operands.checkOperandsTypes<Int, Int>() -> return Operand(
                value = (lOperand.getIntValue() + rOperand.getIntValue()).toString(),
                variableType = VariableType.Number,
            )
            else -> operands.throwUnexpectedTypes("plus")
        }
    }

    override fun processMinus(lOperand: Operand, rOperand: Operand): Operand {
        val operands = (lOperand to rOperand)
        when {
            operands.checkOperandsTypes<Int, Int>() -> return Operand(
                value = (lOperand.getIntValue() - rOperand.getIntValue()).toString(),
                variableType = VariableType.Number,
            )
            else -> operands.throwUnexpectedTypes("minus")
        }
    }

    override fun processLess(lOperand: Operand, rOperand: Operand): Operand {
        val operands = (lOperand to rOperand)
        when {
            operands.checkOperandsTypes<Int, Int>() -> return Operand(
                value = (lOperand.getIntValue() < rOperand.getIntValue()).toString(),
                variableType = VariableType.Logical,
            )
            else -> operands.throwUnexpectedTypes("less")
        }
    }

    override fun processMore(lOperand: Operand, rOperand: Operand): Operand {
        val operands = (lOperand to rOperand)
        when {
            operands.checkOperandsTypes<Int, Int>() -> return Operand(
                value = (lOperand.getIntValue() > rOperand.getIntValue()).toString(),
                variableType = VariableType.Logical,
            )
            else -> operands.throwUnexpectedTypes("more")
        }
    }

    override fun processLessOrEquals(lOperand: Operand, rOperand: Operand): Operand {
        val operands = (lOperand to rOperand)
        when {
            operands.checkOperandsTypes<Int, Int>() -> return Operand(
                value = (lOperand.getIntValue() <= rOperand.getIntValue()).toString(),
                variableType = VariableType.Logical,
            )
            else -> operands.throwUnexpectedTypes("less or equals")
        }
    }

    override fun processMoreOrEquals(lOperand: Operand, rOperand: Operand): Operand {
        val operands = (lOperand to rOperand)
        when {
            operands.checkOperandsTypes<Int, Int>() -> return Operand(
                value = (lOperand.getIntValue() >= rOperand.getIntValue()).toString(),
                variableType = VariableType.Logical,
            )
            else -> operands.throwUnexpectedTypes("more or equals")
        }
    }

    override fun processEquals(lOperand: Operand, rOperand: Operand): Operand {
        val operands = (lOperand to rOperand)
        return when {
            operands.checkOperandsTypes<Int, Int>() -> Operand(
                value = (lOperand.getIntValue() == rOperand.getIntValue()).toString(),
                variableType = VariableType.Logical,
            )
            else -> Operand(
                value = false.toString(),
                variableType = VariableType.Logical,
            )
        }
    }

    override fun processNotEquals(lOperand: Operand, rOperand: Operand): Operand {
        val operands = (lOperand to rOperand)
        return when {
            operands.checkOperandsTypes<Int, Int>() -> return Operand(
                value = (lOperand.getIntValue() != rOperand.getIntValue()).toString(),
                variableType = VariableType.Logical,
            )
            else -> Operand(
                value = true.toString(),
                variableType = VariableType.Logical,
            )
        }
    }

    override fun processLogicalAnd(lOperand: Operand, rOperand: Operand): Operand {
        val operands = (lOperand to rOperand)
        when {
            operands.checkOperandsTypes<Boolean, Boolean>() -> return Operand(
                value = (lOperand.getBooleanValue() && rOperand.getBooleanValue()).toString(),
                variableType = VariableType.Logical,
            )
            else -> operands.throwUnexpectedTypes("logical and")
        }
    }

    override fun processLogicalOr(lOperand: Operand, rOperand: Operand): Operand {
        val operands = (lOperand to rOperand)
        when {
            operands.checkOperandsTypes<Boolean, Boolean>() -> return Operand(
                value = (lOperand.getBooleanValue() || rOperand.getBooleanValue()).toString(),
                variableType = VariableType.Logical,
            )
            else -> operands.throwUnexpectedTypes("logical or")
        }
    }

    private inline fun <reified T : Any, reified R : Any> Pair<Operand, Operand>.checkOperandsTypes(): Boolean =
        this.first.variableType.classToCast == T::class && this.second.variableType.classToCast == R::class

    private fun Pair<Operand, Operand>.throwUnexpectedTypes(operation: String): Nothing =
        throw IllegalArgumentException("Unexpected types for operation $operation operands are: $this")
}
