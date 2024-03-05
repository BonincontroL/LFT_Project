import java.io.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
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

    // attributo=nome.valore
    public void start() {
        int expr_val = 0;
        switch (look.tag) {
            case '(':
            case NumberTok.tag:
                expr_val = expr();
                match(Tag.EOF);
                System.out.println("Ris: " + expr_val); // print(expr.val)
                // System.out.println("FINE");
                break;
            default:
                error("Syntax error in start() --> La tua espressione DEVE iniziare con un numero o una parentesi!"+"carattere: "+look.tag+" non valido!");
                break;

        }
    }

    private int expr() {
        // System.out.println("expr() ");
        int term_val = 0, exprp_val = 0;

        switch (look.tag) {
            case '(':
            case NumberTok.tag:
                term_val = term();
                exprp_val = exprp(term_val); // exprp.i=term.val
                break;
            default:
                error("Syntax error in expr()..."+"carattere: "+look.tag+"non valido!");
                break;
        }
        // System.out.println("expr() "+exprp_val);
        return exprp_val; // expr.val=exprp.val
    }

    private int exprp(int exprp_i) {
        // System.out.println("exprp() ");
        int term_val = 0, exprp_val = 0;
        switch (look.tag) {
            case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);// exprp.i=exprp.i+term.val
                break;
            case '-':
                match(Token.minus.tag);
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);// exprp.i=exprp.i-term.val
                break;

            //case '/': //
            //case '*': //

            case ')':
            case Tag.EOF:
                exprp_val = exprp_i;
                break;

            default:
                error("Syntax error in exprp()..."+"carattere: "+look.tag+" non valido!");
                break;
        }
        // System.out.println("exprp() "+exprp_val);
        return exprp_val; // exprp.val=exprp.val
    }

    private int term() {
        // System.out.println("term()");
        int fact_val = 0, termp_val = 0;

        switch (look.tag) {
            case '(':
            case NumberTok.tag:
                fact_val = fact();
                termp_val = termp(fact_val);
                break;
            default:
                error("Syntax error in term()..."+" carattere: "+look.tag+" non valido!");
                break;

        }

        return termp_val;
    }

    private int termp(int termp_i) {
        // System.out.println("termp() ");
        int fact_val = 0, termp_val = 0;

        switch (look.tag) {
            case '*':
                match(Token.mult.tag);
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                break;
            case '/':
                match(Token.div.tag);
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                break;

            case '+':
            case '-':
            case ')':
            case Tag.EOF:
                termp_val = termp_i;
                break;
            default:
                error("Syntax error in termp()..."+" carattere: "+look.tag+" non valido!");
                break;
        }

        return termp_val;
    }

    private int fact() {
        // System.out.println("fact() ");
        int fact_val = 0;

        switch (look.tag) {
            case '(':
                match(Token.lpt.tag); // (
                fact_val = expr(); // .....
                match(Token.rpt.tag); // )
                break;
            case NumberTok.tag:
                fact_val = ((NumberTok) look).value; // fact.val=NUM.value (cast) Ottengo il valore numerico del token , devo fare prima il cast e poi il controllo
                match(NumberTok.tag);
                break;
            default:
                error("Syntax error in fact()..."+" carattere: "+look.tag+" non valido!");
                break;

        }

        return fact_val;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "input.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            System.out.println("Input ok");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
