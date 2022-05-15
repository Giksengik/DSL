package interpreter

interface BinaryOperatorsProcessor {
    fun processDegree(lOperand: Operand, rOperand: Operand): Operand
    fun processMultiply(lOperand: Operand, rOperand: Operand): Operand
    fun processDiv(lOperand: Operand, rOperand: Operand): Operand
    fun processPlus(lOperand: Operand, rOperand: Operand): Operand
    fun processMinus(lOperand: Operand, rOperand: Operand): Operand
    fun processLess(lOperand: Operand, rOperand: Operand): Operand
    fun processMore(lOperand: Operand, rOperand: Operand): Operand
    fun processLessOrEquals(lOperand: Operand, rOperand: Operand): Operand
    fun processMoreOrEquals(lOperand: Operand, rOperand: Operand): Operand
    fun processEquals(lOperand: Operand, rOperand: Operand): Operand
    fun processNotEquals(lOperand: Operand, rOperand: Operand): Operand
    fun processLogicalAnd(lOperand: Operand, rOperand: Operand): Operand
    fun processLogicalOr(lOperand: Operand, rOperand: Operand): Operand
}
