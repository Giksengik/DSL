package parser

import lexer.TokenType

interface StructureProcessorFactory {

    fun create(): Map<TokenType, StructureProcessor>
}