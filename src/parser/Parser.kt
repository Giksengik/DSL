package parser

import lexer.Token
import java.util.*

interface Parser {

    fun parseToPostfix(tokens: List<Token>): Queue<Token>
}
