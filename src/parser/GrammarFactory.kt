package parser

import lexer.TokenType

interface GrammarFactory {

    fun create() : Map<TokenType, Grammar>
}