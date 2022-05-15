package interpreter

class UnaryOperatorProcessorImpl : UnaryOperatorProcessor {

    override fun processNot(operand: Operand): Operand {
        return Operand(
            value = (!operand.getBooleanValue()).toString(),
            variableType = VariableType.Logical,
        )
    }
}
