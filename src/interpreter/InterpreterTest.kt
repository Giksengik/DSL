package interpreter

import lexer.LexerImpl
import parser.ParserImpl

fun main() {
    val lexer = LexerImpl()
    val parser = ParserImpl()
    val interpreter = InterpreterImpl()
    val tokens = lexer.parseInput("""
        a = 1;
        b = 100;
        while(a <= b) {
            c = 1;
            while(c < 55) {
                c = c + 5;
                a = a + 5;
            }
        }
    """.trimIndent())
    val postfix = parser.parseToPostfix(tokens)
    interpreter.interpret(postfix)
}
