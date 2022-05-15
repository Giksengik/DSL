package parser

import lexer.LexerImpl

fun main() {
    val lexer = LexerImpl()
    val parser = ParserImpl()
    val tokens = lexer.parseInput(
        """
            
            
        a = 5 + 5 + 5;
        b = a * 4 ** 5 + 20;
        c = a + b;
    """.trimIndent()
    )
    print(parser.parseToPostfix(tokens).map { it.value }.runningFold("") { acc, s -> "$acc $s" }.last())
}
