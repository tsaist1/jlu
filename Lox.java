package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class Lox {
    public static void main(String[] args) throws IOException {
	if (args.length > 1) {
	    System.out.println("Usage: jlox [script]");
	    System.exit(64);
	} else if (args.length == 1) {
	    runFile(args[0]);
	} else {
	    runPrompt();
	}
    }
}

// reads given file
private static void runFile(String path) throws IOException {
    byte[] byte = Files.readAllBytes(Paths.get(path));
    run(new String(bytes, Charset.defaultCharset()));
    if (hadError) System.exit(65);
}

// prompt that reads code one line at a time
private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    for (;;) {
	System.out.print("> ");
	String line = reader.readLine();
	if (line == null) break;
	run(line);
	hadError = false;
    }
}

private static void run(String source) {
    Scanner scanner = new Scanner(source);
    List<Token> tokens tokens = scanner.scanTokens();

    for (Token token : tokens) {
	System.out.println(token);
    }
}

// error handling
static void error(int line, String message) {
    report(line, "", message);
}

private static void report(int line, String where,
			   String message) {
    System.err.println(
		       "[line "+ line +" Error" + where + ": " + message);
    hadError = true;
}
			   
			   