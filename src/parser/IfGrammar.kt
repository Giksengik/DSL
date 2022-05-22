package parser

import lexer.Token
import lexer.TokenType

object IfGrammar : Grammar {

    override fun checkSyntax(notProcessedTokens: List<Token>) {
        require(notProcessedTokens.firstOrNull()?.type is TokenType.Operator.LBracket) {
            "Require open bracket for logical block in if structure, but got: ${notProcessedTokens.firstOrNull()}"
        }
        val rightLogicalBracketPos = notProcessedTokens.indexOfFirst { it.type is TokenType.Operator.RBracket }
        val leftExecutionBracketPos = notProcessedTokens.subList(rightLogicalBracketPos + 1, notProcessedTokens.size).indexOfFirst { it.type is TokenType.Operator.LCurlyBracket }
        val rightExecutionBracketPos = notProcessedTokens.subList(leftExecutionBracketPos + 1, notProcessedTokens.size).indexOfFirst { it.type is TokenType.Operator.RCurlyBracket }

        require(rightLogicalBracketPos != -1 && leftExecutionBracketPos != -1 && rightExecutionBracketPos != -1) {
            "Wrong if block structure"
        }
    }
}