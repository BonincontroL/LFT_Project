import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move();
        } else
            error("syntax error");
    }

    public void prog() {
        switch (look.tag) {
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.COND:
            case '{':
                stalist();
                match(Tag.EOF);
                break;
            default:
                System.out.println("Syntax error in prog()...");
        }
    }

    private void stalist() {
        switch (look.tag) {
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.COND:
            case '{':
                stat();
                stalistp();
                break;
            default:
                error("Syntax error in stalist()...");
        }
    }

    private void stalistp() {

        switch (look.tag) {
            case ';':
                match(';');
                stat();
                stalistp();
                break;
            case '}':
                break;
            case Tag.EOF:
                match(Tag.EOF);
                break;

            default:
                error("Syntax error in stalistp()...");

        }

    }

    private void stat() {

        switch (look.tag) {

            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('[');
                exprlist();
                match(']');
                break;
            case Tag.READ:
                match(Tag.READ);
                match('[');
                idlist();
                match(']');
                break;
            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                bexpr();
                match(')');
                stat();
                break;
            case Tag.COND:
                match(Tag.COND);
                match('[');
                optlist();
                match(']');
                statp();
                break;
            case '{':
                match('{');
                stalist();
                match('}');
                break;
            default:
                error("Syntax error in stat()...");

        }
    }

    private void statp() {
        switch (look.tag) {
            case Tag.END:
                match(Tag.END);
                break;
            case Tag.ELSE:
                match(Tag.ELSE);
                stat();
                match(Tag.END);
                break;
        }
    }

    private void idlist() {

        switch (look.tag) {
            case Tag.ID:
                match(Tag.ID);
                idlistp();
                break;
            default:
                error("Syntax error in idlist()...");

        }

    }

    private void idlistp() {

        switch (look.tag) {
            case ',':
                match(',');
                match(Tag.ID);
                idlistp();
                break;
            case ']':
            case Tag.END:
            case ';':
            case '}':
                break;
            case Tag.EOF:
                match(Tag.EOF);
                break;
            default:
                error("Syntax error in idlistp()...");
                break;

        }

    }

    private void optlist() {

        switch (look.tag) {
            case Tag.OPTION:
                optitem();
                optlistp();
                break;
            default:
                error("Syntax error in optlist()... ");
                break;
        }
    }

    private void optlistp() {
        switch (look.tag) {
            case Tag.OPTION:
                optitem();
                optlistp();
                break;
            case Tag.EOF:
                match(Tag.EOF);
                break;
            case ']':
                break;
            default:
                error("Syntax error in optlistp()...");

        }
    }

    private void optitem() {

        switch (look.tag) {
            case Tag.OPTION:
                match(Tag.OPTION);
                match('(');
                bexpr();
                match(')');
                match(Tag.DO);
                stat();
                break;
            default:
                error("Syntax error in optitem()...");
                break;

        }
    }

    private void bexpr() {
        switch (look.tag) {
            case Tag.RELOP:
                match(Tag.RELOP);
                expr();
                expr();
                break;
            default:
                error("Syntax error in bexpr()...");
                break;
        }
    }

    private void expr() {
        switch (look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                break;
            case '-':
                match('-');
                expr();
                expr();
                break;
            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                break;
            case '/':
                match('/');
                expr();
                expr();
                break;
            case NumberTok.tag:
                match(NumberTok.tag);
                break;
            case Tag.ID:
                match(Tag.ID);
                break;
            default:
                error("Syntax error in expr()...");
                break;
        }
    }

    private void exprlist() {
        switch (look.tag) {
            case '+':
            case '-':
            case '*':
            case '/':
            case ')':
            case NumberTok.tag:
            case Tag.ID:
                expr();
                exprlistp();
                break;
            default:
                error("Syntax error in exprlist()...");
                break;
        }
    }

    private void exprlistp() {
        switch (look.tag) {
            case ',':
                match(',');
                expr();
                exprlistp();
                break;
            case Tag.EOF:
                match(Tag.EOF);
                break;
            case ')':
            case ']':
                break;
            default:
                error("Syntax error in exprlistp()...");
                break;
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "input.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}