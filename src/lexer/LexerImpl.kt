package lexer

import java.lang.IllegalArgumentException
import java.lang.StringBuilder

class LexerImpl : Lexer {

    private val tokenTypes: List<TokenType>

    init {
        val tokenTypesFactory: TokenTypesFactory = TokenTypesFactoryImpl()
        tokenTypes = tokenTypesFactory.producePatterns()
    }


    override fun parseInput(input: String): List<Token> {
        val buffer = StringBuilder()
        var prevTokenType: TokenType? = null
        val tokens = mutableListOf<Token>()
        for (i in input.indices) {
            buffer.append(input[i])
            var nextTokenType = extractTokenTypeForInput(buffer.toString())

            if (nextTokenType != null) {
                if (i == input.length - 1) {
                    tokens.add(buffer.toString().createToken(nextTokenType))
                    buffer.clear()
                }
            } else {
                if (prevTokenType != null) {
                    val bufferString = buffer.toString()
                    tokens.add(bufferString.substring(0, bufferString.length - 1).createToken(prevTokenType))
                    buffer.clear()
                    buffer.append(
                        bufferString.substring(bufferString.length - 1, bufferString.length)
                    )
                    nextTokenType = extractTokenTypeForInput(buffer.toString())
                }
            }
            prevTokenType = nextTokenType
        }

        if (buffer.isNotEmpty()) {
            val tokenType = extractTokenTypeForInput(buffer.toString())
                ?: throw IllegalArgumentException("Cant parse input: $buffer")
            tokens.add(buffer.toString().createToken(tokenType))
        }

        return tokens
    }

    private fun extractTokenTypeForInput(input: String): TokenType? {
        val availableTokenTypes = tokenTypes.filter {
            it.pattern.matcher(input).find()
        }
        return when {
            availableTokenTypes.isEmpty() -> null
            availableTokenTypes.size == 1 -> availableTokenTypes.first()
            availableTokenTypes.size == 2 && availableTokenTypes.contains(TokenType.Vars.Variable) -> {
                availableTokenTypes.first { it != TokenType.Vars.Variable }
            }
            else ->
                throw IllegalArgumentException("Input $input can not be prossed. There are too many tokens for this input : $availableTokenTypes")
        }
    }

}
