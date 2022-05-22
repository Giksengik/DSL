package parser

import lexer.Token
import java.util.*

interface Parser {

    fun parseToPostfix(tokensWithIgnore: List<Token>): List<Token>
}
