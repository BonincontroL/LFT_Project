import java.io.*;
import java.util.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n')
                line++;
            readch(br);
        }

        // #region gestione casi di ( ) [ ] { } + . * / ; , ! - ..
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;
            case '(':
                peek = ' ';
                return Token.lpt;
            case ')':
                peek = ' ';
                return Token.rpt;
            case '[':
                peek = ' ';
                return Token.lpq;
            case ']':
                peek = ' ';
                return Token.rpq;
            case '{':
                peek = ' ';
                return Token.lpg;
            case '}':
                peek = ' ';
                return Token.rpg;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case '/':
                readch(br);
                if (peek == '/') // caso commento // ignorare commenti del tipo //ciao // --> / /
                { // commenti che iniziano con // e che terminano con un a capo oppure con EOF.
                    while (peek != '\n' && peek != (char) -1) {
                        readch(br);
                    }
                    if (peek == '\n')
                        line++;
                    peek = ' ';
                    return lexical_scan(br);
                } else if (peek == '*') // /* scenario
                {
                    while (peek != (char) -1) // continua a leggere fino alla fine EOF
                    {
                        while (peek != '*' && peek != (char) -1) {
                            readch(br);
                        }
                        // legge /* ...... *
                        readch(br);
                        if (peek == '/') // /* ..... */
                        {
                            peek = ' ';
                            return lexical_scan(br);
                        }
                        // altrimenti non fare nulla 
                    }
                    System.out.println("Comment not closed at line " + line); // ERROR di chiusura
                    return null;
                }
                return Token.div; //altrimenti

            case ';':
                peek = ' ';
                return Token.semicolon;
            case ',':
                peek = ' ';
                return Token.comma;
            // #endregion

            // #region gestire i casi di && || < > <= >= == <> ...

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : " + peek);
                    return null;
                }
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : " + peek);
                    return null;
                }
            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else {
                    return Word.lt;
                }
            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character"
                            + " after = : " + peek);
                    return null;
                }
                // #endregion

                // Fine dell’input
            case (char) -1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek) || peek == '_') {
                    // La classe Word utilizzata sia per le parole riservate e sia per gli
                    // identificatori (es. in: a543 out: <257,a54b3>)
                    Word word = new Word(257, ""); // Identificatore Lettera seguita da lettere e cifre
                    while (Character.isLetterOrDigit(peek) || peek == '_') {
                        word.lexeme += peek;
                        readch(br);
                    }
                    // #region Controlli word
                    // la sequenza non comincia con un numero e non puo essere composta solo dal
                    // simbolo _
                    boolean isTrue = false;// __a a__
                    for (int i = 0; i < word.lexeme.length(); i++) {
                        if (word.lexeme.charAt(i) != '_')
                            isTrue = true;
                    }
                    if (!isTrue) {
                        System.out.println("\'_\' at line " + line + " is not a valid identifier");
                        return null;
                    }

                    if (word.lexeme.equals("assign"))
                        return Word.assign;
                    if (word.lexeme.equals("conditional"))
                        return Word.conditional;
                    if (word.lexeme.equals("read"))
                        return Word.read;
                    if (word.lexeme.equals("print"))
                        return Word.print;
                    if (word.lexeme.equals("else"))
                        return Word.elsetok;
                    if (word.lexeme.equals("while"))
                        return Word.whiletok;
                    if (word.lexeme.equals("do"))
                        return Word.dotok;
                    if (word.lexeme.equals("to"))
                        return Word.to;
                    if (word.lexeme.equals("end"))
                        return Word.end;
                    if (word.lexeme.equals("option"))
                        return Word.option;
                    if (word.lexeme.equals("dotok"))
                        return Word.dotok;
                    if (word.lexeme.equals("elsetok"))
                        return Word.elsetok;
                    if (word.lexeme.equals("begin"))
                        return Word.begin;
                    // #endregion
                    return word;

                } else if (Character.isDigit(peek)) {
                    int v = 0;
                    do {
                        v = 10 * v + Character.getNumericValue(peek);
                        readch(br);
                    } while (Character.isDigit(peek));

                    return new NumberTok(v);

                } else {
                    System.err.println("Erroneous character: "
                            + peek);
                    return null;
                }
        }
        // return null;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "2.1_Lexer.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
