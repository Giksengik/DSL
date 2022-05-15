package interpreter

import lexer.Token
import lexer.TokenType
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass

class InterpreterImpl : Interpreter {

    private val vars: MutableMap<String, Variable> = HashMap()
    private val operandStack: Stack<Operand> = Stack()

    private val binaryOperatorsProcessor: BinaryOperatorsProcessor = BinaryOperatorProcessorImpl()
    private val unaryOperatorProcessor: UnaryOperatorProcessor = UnaryOperatorProcessorImpl()

    override fun interpret(postfixQueue: Queue<Token>) {
        while (postfixQueue.isNotEmpty()) {
            val nextToken = postfixQueue.poll()
            when (nextToken.type) {
                is TokenType.Vars -> {
                    operandStack.add(convertTokenToOperand(nextToken))
                }
                is TokenType.Operator.Binary -> {
                    processBinaryOperator(nextToken)
                }
                is TokenType.Operator.Unary -> {
                    processUnaryOperator(nextToken)
                }
            }
        }

        print(vars)
        print(operandStack)
    }

    private fun processUnaryOperator(nextToken: Token) {
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

    private fun processBinaryOperator(nextToken: Token) {
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

    private fun convertTokenToOperand(token: Token): Operand {
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
            Operand(
                value = token.value,
                variableType = when (token.type) {
                    is TokenType.Vars.Number -> VariableType.Number
                    is TokenType.Vars.Boolean -> VariableType.Logical
                    else -> throw IllegalArgumentException("cant process this token: $token")
                }
            )
        }
    }
}
