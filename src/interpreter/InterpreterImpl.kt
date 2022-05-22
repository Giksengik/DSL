package interpreter

import lexer.Token
import lexer.TokenType
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.log

class InterpreterImpl : Interpreter {

    private val binaryOperatorsProcessor: BinaryOperatorsProcessor = BinaryOperatorProcessorImpl()
    private val unaryOperatorProcessor: UnaryOperatorProcessor = UnaryOperatorProcessorImpl()

    override fun interpret(postfix: List<Token>): Int {
        val vars = kotlin.collections.HashMap<String, Variable>()
        interpretInternal(postfix, postfix.indices, vars)
        print(vars)
        return 0
    }

    private fun interpretInternal(postfix: List<Token>, range: IntRange, vars: MutableMap<String, Variable> = HashMap()): Any {
        val operandStack: Stack<Operand> = Stack()
        var pointer = range.first
        while (pointer < range.last && pointer < postfix.size) {
            val nextToken = postfix[pointer]
            when (nextToken.type) {
                is TokenType.Vars -> {
                    operandStack.add(convertTokenToOperand(nextToken, vars))
                }
                is TokenType.Operator.Binary -> {
                    processBinaryOperator(nextToken, vars, operandStack)
                }
                is TokenType.Operator.Unary -> {
                    processUnaryOperator(nextToken, operandStack)
                }
                is TokenType.Operator.Service -> {
                    pointer = processServiceAndReturnPointer(nextToken.type, postfix, pointer, vars)
                    continue
                }
            }
            pointer++
        }

        return try {
            operandStack.pop()
        } catch (_: java.lang.Exception) {
        }
    }

    private fun processServiceAndReturnPointer(tokenType: TokenType.Operator.Service, postfix: List<Token>, pointer: Int, vars: MutableMap<String, Variable>): Int {
        var newPointer = 0
        when (tokenType) {
            TokenType.Operator.While -> {
                val logicalBlockToken = postfix[pointer + 1]
                val executionBlockToken = postfix[pointer + 2]
                require(logicalBlockToken.type is TokenType.LogicalBlock && executionBlockToken.type is TokenType.ExecutionBlock)
                newPointer = logicalBlockToken.type.length + executionBlockToken.type.length + pointer + 3

                while (true) {
                    val logicalBlockResult = interpretInternal(postfix, (pointer + 3)..(pointer + 3 + logicalBlockToken.type.length), vars)
                    if (logicalBlockResult is Operand && logicalBlockResult.variableType.classToCast == Boolean::class) {
                        val doExecute = logicalBlockResult.getBooleanValue()
                        if (doExecute) {
                            interpretInternal(postfix, (pointer + 3 + logicalBlockToken.type.length)..(pointer + 3 + logicalBlockToken.type.length + executionBlockToken.type.length), vars)
                        } else {
                            break
                        }
                    } else {
                        throw java.lang.IllegalArgumentException("illegal logic block in while structure")
                    }
                }
            }
        }
        return newPointer
    }

    private fun processUnaryOperator(nextToken: Token, operandStack: Stack<Operand>) {
        require(nextToken.type is TokenType.Operator.Unary)
        require(operandStack.size >= 1) {
            "Inappropriate context for unary operator ${nextToken.type} stack: $operandStack"
        }
        val operand = operandStack.pop()

        when (nextToken.type) {
            TokenType.Operator.Not -> {
                operandStack.add(unaryOperatorProcessor.processNot(operand))
            }
        }
    }

    private fun processBinaryOperator(nextToken: Token, vars: MutableMap<String, Variable>, operandStack: Stack<Operand>) {
        require(nextToken.type is TokenType.Operator.Binary)
        require(operandStack.size >= 2) {
            "Inappropriate context for binary operator ${nextToken.type} stack: $operandStack"
        }
        val rOperand = operandStack.pop()
        val lOperand = operandStack.pop()
        when (nextToken.type) {
            is TokenType.Operator.Degree -> {
                operandStack.add(binaryOperatorsProcessor.processDegree(lOperand, rOperand))
            }
            is TokenType.Operator.Multiply -> {
                operandStack.add(binaryOperatorsProcessor.processMultiply(lOperand, rOperand))
            }
            is TokenType.Operator.Div -> {
                operandStack.add(binaryOperatorsProcessor.processDiv(lOperand, rOperand))
            }
            is TokenType.Operator.Plus -> {
                operandStack.add(binaryOperatorsProcessor.processPlus(lOperand, rOperand))
            }
            is TokenType.Operator.Minus -> {
                operandStack.add(binaryOperatorsProcessor.processMinus(lOperand, rOperand))
            }
            is TokenType.Operator.Less -> {
                operandStack.add(binaryOperatorsProcessor.processLess(lOperand, rOperand))
            }
            is TokenType.Operator.More -> {
                operandStack.add(binaryOperatorsProcessor.processMore(lOperand, rOperand))
            }
            is TokenType.Operator.LessOrEquals -> {
                operandStack.add(binaryOperatorsProcessor.processLessOrEquals(lOperand, rOperand))
            }
            is TokenType.Operator.MoreOrEquals -> {
                operandStack.add(binaryOperatorsProcessor.processMoreOrEquals(lOperand, rOperand))
            }
            is TokenType.Operator.Equals -> {
                operandStack.add(binaryOperatorsProcessor.processEquals(lOperand, rOperand))
            }
            is TokenType.Operator.NotEquals -> {
                operandStack.add(binaryOperatorsProcessor.processNotEquals(lOperand, rOperand))
            }
            is TokenType.Operator.LogicalAnd -> {
                operandStack.add(binaryOperatorsProcessor.processLogicalAnd(lOperand, rOperand))
            }
            is TokenType.Operator.LogicalOr -> {
                operandStack.add(binaryOperatorsProcessor.processLogicalOr(lOperand, rOperand))
            }
            is TokenType.Operator.Assignment -> {
                require(lOperand is VariableOperand) {
                    "Cant assign value to not Variable Operand!"
                }
                val newVariable = Variable(
                        value = rOperand.value,
                        type = rOperand.variableType,
                )

                vars[lOperand.name] = newVariable
            }
        }
    }

    private fun convertTokenToOperand(token: Token, vars: MutableMap<String, Variable>): Operand {
        require(token.type is TokenType.Vars)
        return if (token.type is TokenType.Vars.Variable) {
            val isInitialized = vars.containsKey(token.value)
            if (isInitialized) {
                VariableOperand(
                        name = token.value,
                        value = vars[token.value]!!.value,
                        variableType = vars[token.value]!!.type,
                        isInitialized = true,
                )
            } else {
                VariableOperand(
                        name = token.value,
                        value = "",
                        variableType = VariableType.Undefined,
                        isInitialized = false,
                )
            }
        } else {
            Operand(value = token.value, variableType = when (token.type) {
                is TokenType.Vars.Number -> VariableType.Number
                is TokenType.Vars.Boolean -> VariableType.Logical
                else -> throw IllegalArgumentException("cant process this token: $token")
            })
        }
    }
}
