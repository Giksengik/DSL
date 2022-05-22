package parser

import lexer.Token
import lexer.TokenPriority
import lexer.TokenType
import java.util.*

class ParserImpl : Parser {

    private val postfix: MutableList<Token> = mutableListOf()
    private val stack: Stack<Token> = Stack()

    private val grammarFactory: GrammarFactory = GrammarFactoryImpl()
    private val grammars: Map<TokenType, Grammar> = grammarFactory.create()

    private val processorFactory: StructureProcessorFactory = StructureProcessorFactoryImpl()
    private val processors: Map<TokenType, StructureProcessor> = processorFactory.create()

    override fun parseToPostfix(tokensWithIgnore: List<Token>): List<Token> {
        val tokens = tokensWithIgnore.filter { it.type !is TokenType.Ignore }
        for (i in tokens.indices) {
            val token = tokens[i]
            when (token.type) {
                is TokenType.Operator -> {
                    if (token.type.priority == TokenPriority.ONE) {
                        processFirstPriorityOperator(token)
                    } else {
                        processOperator(token, tokens.subList(i + 1, tokens.size))
                    }
                }
                is TokenType.Vars -> {
                    postfix.add(token)
                }
                else -> Unit
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

    private fun processOperator(token: Token, notProcessedTokens: List<Token>) {
        require(token.type is TokenType.Operator)
        if (token.type.priority == TokenPriority.LOWEST) {
            processLowestPriority(token, notProcessedTokens)
        } else if (stack.isEmpty()) {
            stack.add(token)
        } else {
            while (stack.isNotEmpty()) {
                val topElement = stack.peek()
                require(topElement.type is TokenType.Operator)
                if (topElement.type.priority == TokenPriority.ONE) {
                    stack.add(token)
                    return
                } else if (token.type.priority >= topElement.type.priority) {
                    postfix.add(stack.pop())
                } else {
                    break
                }
            }
            stack.add(token)
        }
    }

    private fun processLowestPriority(token: Token, notProcessedTokens: List<Token>) {
        val tokenType = token.type
        require(tokenType is TokenType.Operator && tokenType.priority == TokenPriority.LOWEST)
        grammars[tokenType]?.checkSyntax(notProcessedTokens)
        processors[tokenType]?.processStructure(notProcessedTokens, stack, postfix)
                ?: throw java.lang.IllegalArgumentException("This token cant be processed yet: $token")
    }

    private fun processFirstPriorityOperator(token: Token) {
        if (stack.isEmpty() && token.type !is TokenType.Operator.Service) {
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
            val nextToken = stack.peek()
            when (border) {
                PushOutBorder.END_OF_STACK -> {
                    if (nextToken.type is TokenType.Operator && nextToken.type.priority == TokenPriority.ONE) {
                        break
                    } else {
                        postfix.add(stack.pop())
                    }
                }
                PushOutBorder.L_CURLY -> {
                    if (nextToken.type !is TokenType.Operator.LCurlyBracket) {
                        if (nextToken.type is TokenType.Operator && nextToken.type.priority == TokenPriority.ONE) {
                            break
                        } else {
                            postfix.add(stack.pop())
                        }
                    } else {
                        stack.pop()
                        break
                    }
                }
                PushOutBorder.L -> {
                    if (nextToken.type !is TokenType.Operator.LBracket) {
                        if (nextToken.type is TokenType.Operator && nextToken.type.priority == TokenPriority.ONE) {
                            break
                        } else {
                            postfix.add(stack.pop())
                        }
                    } else {
                        stack.pop()
                        break
                    }
                }
            }
        }
    }

    enum class PushOutBorder {
        END_OF_STACK, L_CURLY, L
    }
}
