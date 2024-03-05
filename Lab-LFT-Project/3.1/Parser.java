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

    public void start() { // Inzio leggere sequenza di Token
      // System.out.println(look.tag + " in start");
            switch (look.tag) {
            case '(': // caso ( o num  altrimenti errore
            case NumberTok.tag:
                expr();   //variabile  --> il metodo invoca la procedura  expr()
                match(Tag.EOF);  //terminale  --> il metodo controlla che il simbolo corrente sia proprio Tag. ... e fa avanzare il lexer al simbolo successivo
                break;
            default:   // altrimenti look.tag non è nell'insieme guida di nessuna produzione
                error("Syntax error in start() --> La tua espressione DEVE iniziare con un numero o una parentesi!");
                break;
        }
    }

    private void expr() {  // E
       // System.out.println(look.tag + " in expr");
        switch (look.tag) {
            case '(':
            case NumberTok.tag:
                term();
                exprp();
                break;
            default:
                error("Syntax error in expr()...");
                break;
        }
    }

    private void exprp() { // E'
       // System.out.println(look.tag+" in exprp");
        switch (look.tag) {
            case '+':
                match(Token.plus.tag);
                term();
                exprp();
                break;
            case '-':
                match(Token.minus.tag);
                term();
                exprp();
                break;


            //case '/':    ////
            //case '*': 	////

            case ')':
            case Tag.EOF:
                break;
            default:
                error("Syntax error in exprp()...");
                break;

        }
    }

    private void term() { //T
      // System.out.println(look.tag+" in term");
        switch (look.tag) {
            case '(':
            case NumberTok.tag:
                fact();
                termp();
                break;
            default:
                error("Syntax error in term()...");
                break;

        }
    }

    private void termp() {//T'
       // System.out.println(look.tag+" in termp");
        switch (look.tag) {
            case '*':
                match(Token.mult.tag);
                fact();
                termp();
                break;
            case '/':
                match(Token.div.tag);
                fact();
                termp();
                break;

           /*  case NumberTok.tag:   */
            case '+':
            case '-':
            case ')':
            case Tag.EOF:
                break;
            default:
                error("Syntax error in termp()...");
                break;
        }

    }

    private void fact() {//F
      // System.out.println(look.tag+" in fact");
        switch (look.tag) {
            case '(':
                match(Token.lpt.tag); // (
                expr();               //    .....
                match(Token.rpt.tag); //          )
                break;
            case NumberTok.tag:
                match(NumberTok.tag);
                break;
            default:
                error("Syntax error in fact()...");
                break;
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "input.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}