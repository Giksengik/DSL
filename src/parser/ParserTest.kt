package parser

import lexer.LexerImpl

fun main() {
    val lexer = LexerImpl()
    val parser = ParserImpl()
    val tokens = lexer.parseInput("""
        ss = true;
        ff = false;
        f = ss && ff;
        c = 0;
        while(c < 2) { c = c + 1; while(c > -1) { a = 5; } }
    """.trimIndent())
    print(parser.parseToPostfix(tokens).map { it.value }.runningFold("") { acc, s -> "$acc $s" }.last())
}
