package lexer

interface TokenTypesFactory {

    fun producePatterns(): List<TokenType>
}
