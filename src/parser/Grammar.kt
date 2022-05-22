package parser

import lexer.Token

interface Grammar {

    fun checkSyntax(notProcessedTokens: List<Token>)
}