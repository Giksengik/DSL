package parser

import lexer.Token
import java.util.*

interface StructureProcessor {

    fun processStructure(notProcessedTokens: List<Token>, stack: Stack<Token>, postfix: MutableList<Token>)
}