package lexer

data class Token(
    val type: TokenType,
    val value: String,
)

fun String.createToken(tokenType: TokenType): Token =
    Token(
        type = tokenType,
        value = this
    )
