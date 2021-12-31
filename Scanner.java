package com.craftinginterpreteres.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util. Map;

import static com.craftinginterpreteres.lox.TokenType.*;

/* lexical analysis
   lexemes -- blobs of characters
   token -- grammar of language
 */
class Scanner {
    private final String source;
    private final List<Token> tokens =new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(string source) {
	this.source = source;
    }
}

List<Token> scanTokens() {
    while (!isAtEnd()) {
	// at beginning of next lexeme
	start = current;
	scanToken();
    }
    tokens.add(new Token(EOF, "", null, line));
    return tokens;
}

private void string() {
    while (peek() != '"' && !isAtEnd()) {
        if (peek() == '\n') line++;
        advance();
    }
    if (isAtEnd()) {
        Lox.error(line, "Unterminated string.");
        return;
    }
    advance(); // closing ".

    // Trim surrounding quotes
    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
}

private boolean match(char expected) {
    if (isAtEnd()) return false;
    if (source.charAt(current) != expected) return false;
    current++;
    return true;
}

// lookahead 1 char
private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
}

private char peekNext() {
    if (current +1 >= source.length()) return '\0';
    return source.charAt(current + 1);
}

private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
}
                                  
private boolean isAtEnd() {
    return current >= source.length();
}

private void scanToken() {
    char c = advance();
    switch(c) {
    case '(': addToken(LEFT_PAREN); break;
    case ')': addToken(RIGHT_PAREN); break;
    case '{': addToken(LEFT_BRACE); break;
    case '}': addToken(RIGHT_BRACE); break;
    case ',': addToken(COMMA); break;
    case '.': addToken(DOT); break;
    case '-': addToken(MINUS); break;
    case '+': addToken(PLUS); break;
    case ';': addToken(SEMICOLON); break;
    case '*': addToken(STAR); break;
    case '!':
        addToken(match('=') ? BANG_EQUAL : BANG);
        break;
    case '=':
        addToken(match('=') ? BANG_EQUAL : EQUAL);
        break;
    case '<':
        addToken(match('=') ? BANG_EQUAL : LESS);
        break;
    case '>':
        addToken(match('=') ? BANG_EQUAL : GREATER);
        break;

    case '/':
	if (match('/')) {
	    // special case for comments
	    while (peek() != '\n' && isAtEnd());
	} else {
	    addToken(SLASH);
	}
	break;
    // ignores whitespace
    case ' ':
    case '\r':
    case '\t':
        break;

    case '\n':
        line++;
        break;

    case '"': string(); break;

    default:
        if (isDigit(c)) {
            number();
        } else {
            Lox.error(line, "Unexpected character.");
        }
        break;
    
    }
}

private void number() {
    while(isDigit(peek())) advance();

    if (peek() == '.' && isDigit(peekNext())) {
        // Consume "."
        advance();
        while (isDigit(peek())) advance();
    }
    addToken(NUMBER,
             Double.parseDouble(source.substring(start, current));
}

private char advance() {
    return source.charAt(current++);
}

private void addToken(TokenType type) {
    addToken(type, null);
}

private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(newToken(type, text, literal, line));
}
