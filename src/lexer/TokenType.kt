package lexer

sealed class TokenType(val regex: String) {

    sealed class Operator(regex: String, val priority: TokenPriority) : TokenType(regex) {

        sealed class Logical(regex: String, priority: TokenPriority) : Operator(regex, priority)

        sealed class Service(regex: String, priority: TokenPriority) : Operator(regex, priority)

        sealed interface Unary
        sealed interface Binary

        object LBracket : Service("^\\($", TokenPriority.ONE)
        object RBracket : Service("^\\)$", TokenPriority.ONE)
        object LCurlyBracket : Service("^\\{$", TokenPriority.ONE)
        object RCurlyBracket : Service("^\\}$", TokenPriority.ONE)
        object EndOperation : Service("^\\;$", TokenPriority.ONE)

        object Not : Logical("^!$", TokenPriority.TWO), Unary

        object Degree : Operator("^\\*\\*$", TokenPriority.THREE), Binary
        object Multiply : Operator("^\\*$", TokenPriority.THREE), Binary
        object Div : Operator("^/$", TokenPriority.THREE), Binary

        object Plus : Operator("^\\+$", TokenPriority.FOUR), Binary
        object Minus : Operator("^-$", TokenPriority.FOUR), Binary

        object Less : Logical("^\\<$", TokenPriority.FIVE), Binary
        object More : Logical("^\\>$", TokenPriority.FIVE), Binary
        object LessOrEquals : Logical("^\\<=$", TokenPriority.FIVE), Binary
        object MoreOrEquals : Logical("^\\>=$", TokenPriority.FIVE), Binary

        object Equals : Logical("^==$", TokenPriority.SIX), Binary
        object NotEquals : Logical("^!=$", TokenPriority.SIX), Binary

        object LogicalAnd : Logical("^\\&\\&$", TokenPriority.SEVEN), Binary
        object LogicalOr : Logical("^\\|\\|$", TokenPriority.EIGHT), Binary

        object Assignment : Service("^=$", TokenPriority.NINE), Binary
        object If : Service("^if$", TokenPriority.LOWEST)
        object Else : Service("^else$", TokenPriority.LOWEST)
        object Elif : Service("^elif$", TokenPriority.LOWEST)
        object While : Service("^while$", TokenPriority.LOWEST)
    }

    sealed class Vars(regex: String) : TokenType(regex) {
        object Variable : Vars("^[a-zA-Z][a-zA-Z0-9_]*$")
        object Number : Vars("^0$|^[1-9][0-9]*$")
        object Boolean : Vars("^true$|^false$")
    }

    sealed class Ignore(regex: String) : TokenType(regex)
    object Space : Ignore("^\\s{1,}$")

    sealed class Meta(regex: String) : TokenType(regex)
    sealed class BlockInfo(val length: Int) : Meta(REGEX_EMPTY)
    class LogicalBlock(length: Int) : BlockInfo(length)
    class ExecutionBlock(length: Int) : BlockInfo(length)
    private companion object {
        const val REGEX_EMPTY = ""
    }
}

enum class TokenPriority {
    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, LOWEST
}
