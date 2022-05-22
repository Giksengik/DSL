package lexer

fun main() {
    val lexer: Lexer = LexerImpl()

    print(lexer.parseInput(""" 
        a = 555 + 44 * 55;
    """))
}
