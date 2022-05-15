package interpreter

open class Operand(
    val value: String,
    val variableType: VariableType,
) {
    override fun toString(): String {
        return "value: $value classToCast: ${variableType.classToCast}"
    }
}

class VariableOperand(
    val name: String,
    value: String,
    variableType: VariableType,
    val isInitialized: Boolean,
) : Operand(value, variableType)
