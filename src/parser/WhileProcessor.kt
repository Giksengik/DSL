package parser

import lexer.Token
import lexer.TokenType
import java.util.*

object WhileProcessor : StructureProcessor {

    override fun processStructure(notProcessedTokens: List<Token>, stack: Stack<Token>, postfix: MutableList<Token>) {
        val logicalBlockLength = BracketsUtils.getLogicalBlockLength(notProcessedTokens)
        val executionBlockLength = BracketsUtils.getExecutionBlockLength(notProcessedTokens.subList(logicalBlockLength + 2, notProcessedTokens.size))
        postfix.add(Token(TokenType.Operator.While, "while"))
        postfix.add(Token(TokenType.LogicalBlock(logicalBlockLength), "lB $logicalBlockLength"))
        postfix.add(Token(TokenType.ExecutionBlock(executionBlockLength), "exB $executionBlockLength"))
    }
}