package interpreter

import lexer.LexerImpl
import parser.ParserImpl

fun main() {
    val lexer = LexerImpl()
    val parser = ParserImpl()
    val interpreter = InterpreterImpl()
    val tokens = lexer.parseInput(
        """
            
        a = 5 + 5 + 5;
        b = a * 4 ** 5 + 20;
        c = a + b;
        f = false || false && true || true;
    """.trimIndent()
    )
    val postfix = parser.parseToPostfix(tokens)
    interpreter.interpret(postfix)
}
