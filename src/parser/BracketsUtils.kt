package parser

import lexer.Token
import lexer.TokenType

object BracketsUtils {

    fun getLogicalBlockLength(tokens: List<Token>): Int {
        require(tokens[0].type is TokenType.Operator.LBracket)
        var bracketCount = 0
        var serviceCount = 0

        for (i in tokens.indices) {
            val token = tokens[i]
            if (token.type is TokenType.Operator.LBracket) {
                bracketCount++
                serviceCount++
            } else if (token.type is TokenType.Operator.RBracket) {
                bracketCount--
                if (bracketCount == 0) return i - serviceCount
            } else if (token.type is TokenType.Operator.Service && token.type !is TokenType.Operator.Assignment) serviceCount++

        }

        throw java.lang.IllegalArgumentException("This logical block can not be processed")
    }

    fun getExecutionBlockLength(tokens: List<Token>): Int {
        require(tokens[0].type is TokenType.Operator.LCurlyBracket)

        var bracketCount = 0
        var serviceCount = 0
        var whileCount = 0

        for (i in tokens.indices) {
            val token = tokens[i]
            if (token.type is TokenType.Operator.LCurlyBracket) {
                bracketCount++
                serviceCount++
            } else if (token.type is TokenType.Operator.While) whileCount += 1
            else if (token.type is TokenType.Operator.RCurlyBracket) {
                bracketCount--
                if (bracketCount == 0) {
                    return i - serviceCount + whileCount * 2
                }
            } else if (token.type is TokenType.Operator.Service && token.type !is TokenType.Operator.Assignment) serviceCount++


        }

        throw java.lang.IllegalArgumentException("This execution block can not be processed")
    }
}