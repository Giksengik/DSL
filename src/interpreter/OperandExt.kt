package interpreter

fun Operand.getIntValue(): Int = this.value.toInt()

fun Operand.getBooleanValue(): Boolean = this.value.toBooleanStrict()
