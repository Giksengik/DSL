package interpreter

import lexer.Token
import java.util.*

interface Interpreter {

    fun interpret(postfixQueue: Queue<Token>)
}
