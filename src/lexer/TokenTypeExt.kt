package lexer

import java.util.regex.Pattern

val TokenType.pattern: Pattern
    get() = Pattern.compile(this.regex)
