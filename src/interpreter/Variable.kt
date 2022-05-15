package interpreter

import kotlin.reflect.KClass

class Variable(
    val type: VariableType,
    val value: String,
) {
    override fun toString(): String {
        return "$type $value"
    }
}

enum class VariableType(val classToCast: KClass<*>) {
    Number(Int::class),
    Logical(Boolean::class),
    Undefined(Nothing::class),
}
