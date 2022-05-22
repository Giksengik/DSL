package interpreter

import lexer.Token

interface Interpreter {

    fun interpret(postfix: List<Token>): Int
}
