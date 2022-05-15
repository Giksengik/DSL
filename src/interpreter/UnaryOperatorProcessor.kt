package interpreter

interface UnaryOperatorProcessor {

    fun processNot(operand: Operand): Operand
}
