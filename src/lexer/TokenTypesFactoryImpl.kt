package lexer

class TokenTypesFactoryImpl : TokenTypesFactory {

    override fun producePatterns(): List<TokenType> = listOf(
        TokenType.Vars.Number,
        TokenType.Space,
        TokenType.Vars.Variable,
        TokenType.Operator.Assignment,
        TokenType.Vars.Boolean,
        TokenType.Operator.Plus,
        TokenType.Operator.Degree,
        TokenType.Operator.Minus,
        TokenType.Operator.Multiply,
        TokenType.Operator.Div,
        TokenType.Operator.Less,
        TokenType.Operator.More,
        TokenType.Operator.Equals,
        TokenType.Operator.If,
        TokenType.Operator.Else,
        TokenType.Operator.Elif,
        TokenType.Operator.While,
        TokenType.Operator.LBracket,
        TokenType.Operator.RBracket,
        TokenType.Operator.LCurlyBracket,
        TokenType.Operator.RCurlyBracket,
        TokenType.Operator.Not,
        TokenType.Operator.LessOrEquals,
        TokenType.Operator.MoreOrEquals,
        TokenType.Operator.NotEquals,
        TokenType.Operator.LogicalAnd,
        TokenType.Operator.LogicalOr,
        TokenType.Operator.EndOperation,
    )
}
