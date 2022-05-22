package parser

import lexer.TokenType

class GrammarFactoryImpl : GrammarFactory {

    override fun create(): Map<TokenType, Grammar> = mapOf(
            TokenType.Operator.If to IfGrammar,
            TokenType.Operator.While to WhileGrammar,
    )
}