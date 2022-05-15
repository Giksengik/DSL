package parser

import lexer.Token
import lexer.TokenPriority
import lexer.TokenType
import java.util.*

class ParserImpl : Parser {

    private val postfix: Queue<Token> = LinkedList()
    private val stack: Stack<Token> = Stack()

    override fun parseToPostfix(tokens: List<Token>): Queue<Token> {
        for (token in tokens) {
            when (token.type) {
                is TokenType.Operator -> {
                    if (token.type.priority == TokenPriority.ONE) {
                        processFirstPriorityOperator(token)
                    } else {
                        processOperator(token)
                    }
                }
                is TokenType.Vars -> {
                    postfix.add(token)
                }
                is TokenType.Ignore -> Unit
            }
        }

        require(stack.none { it.type !is TokenType.Operator }) {
            "Failed to parse tokens, there are brackets mistake, stack: $stack, input: $tokens"
        }

        while (stack.isNotEmpty()) {
            postfix.add(stack.pop())
        }

        return postfix
    }

    private fun processOperator(token: Token) {
        require(token.type is TokenType.Operator)
        if (stack.isEmpty()) {
            stack.add(token)
        } else {
            while (stack.isNotEmpty()) {
                val topElement = stack.peek()
                require(topElement.type is TokenType.Operator)
                if (token.type.priority >= topElement.type.priority) {
                    postfix.add(stack.pop())
                } else {
                    break
                }
            }
            stack.add(token)
        }
    }

    private fun processFirstPriorityOperator(token: Token) {
        if (stack.isEmpty()) {
            stack.add(token)
        } else {
            when (token.type) {
                is TokenType.Operator.RBracket -> {
                    require(stack.any { it.type is TokenType.Operator.LBracket }) {
                        "Failed to parse tokens with close RBracket and without open LBracket. stack is: $stack"
                    }
                    processEndOperation(PushOutBorder.L)
                }
                is TokenType.Operator.RCurlyBracket -> {
                    require(stack.any { it.type is TokenType.Operator.LCurlyBracket }) {
                        "Failed to parse tokens with close RCurlyBracket and without open LCurlyBracket. stack is: $stack"
                    }
                    processEndOperation(PushOutBorder.L_CURLY)
                }
                is TokenType.Operator.EndOperation -> {
                    processEndOperation(PushOutBorder.END_OF_STACK)
                }
                else -> {
                    stack.add(token)
                }
            }
        }
    }

    private fun processEndOperation(border: PushOutBorder) {
        while (stack.isNotEmpty()) {
            val nextToken = stack.pop()
            when (border) {
                PushOutBorder.END_OF_STACK -> {
                    postfix.add(nextToken)
                    requireNotHighestPriority(nextToken)
                }
                PushOutBorder.L_CURLY -> {
                    if (nextToken.type !is TokenType.Operator.LCurlyBracket) {
                        postfix.add(nextToken)
                    } else {
                        requireNotHighestPriority(nextToken)
                    }
                }
                PushOutBorder.L -> {
                    if (nextToken.type !is TokenType.Operator.LBracket) {
                        postfix.add(nextToken)
                    } else {
                        requireNotHighestPriority(nextToken)
                    }
                }
            }
        }
    }

    private fun requireNotHighestPriority(token: Token) {
        if (token.type is TokenType.Operator)
            require(token.type.priority != TokenPriority.ONE) {
                "Unexpected token for this position: $token stack: $stack"
            }
    }

    enum class PushOutBorder {
        END_OF_STACK, L_CURLY, L
    }
}
