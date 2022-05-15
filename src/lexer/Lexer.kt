package lexer

interface Lexer {

    fun parseInput(input: String): List<Token>
}
