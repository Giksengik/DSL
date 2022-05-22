package parser

import lexer.TokenType

class StructureProcessorFactoryImpl : StructureProcessorFactory {

    override fun create(): Map<TokenType, StructureProcessor> = mapOf(
        TokenType.Operator.While to WhileProcessor
    )
}