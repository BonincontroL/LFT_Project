import java.io.*;
import java.util.Scanner;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0; // Tiene il numero di variabili nel programma

    public Translator(Lexer l, BufferedReader br) {
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


    // Guida(prog -> <stalist> EOF)={assign, print, read, cond, while, '{'}
    public void prog() {
        // System.out.println("prog(): ...");
        switch (look.tag) {
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.COND:
            case '{':
                int lnext = code.newLabel();// L0
                statlist(lnext);
                code.emit(OpCode.GOto, lnext);
                code.emitLabel(lnext);// L0
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }
                break;
            default:
                error("Syntax error in prog(): " + look.tag + " incorrect expression format");
        }
    }

    // Guida(statlist --> <stat><statlistp>)={assign, print, read, cond, while, '{'}
    private void statlist(int lnext) {
        switch (look.tag) {
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.COND:
            case '{':
                stat(lnext);
                stalistp(lnext);
                break;
            default:
                error("Syntax error in stalist(): " + look.tag + " incorrect expression format");
        }
    }

    // Guida (stalistp --> ;<stat><stalistp>)={;}
    // Guida (stalistp --> eps)={EOF,}}
    private void stalistp(int lnext) {
        switch (look.tag) {
            case ';':
                int label = code.newLabel();
                code.emit(OpCode.GOto, label);
                code.emitLabel(label);
                match(';');
                stat(lnext);
                stalistp(lnext);
                break;
            case '}':
            case Tag.EOF:
                label = code.newLabel();
                code.emit(OpCode.GOto, label);
                code.emitLabel(label);
                break;
            default:
                error("Syntax error in stalistp(): " + look.tag + " incorrect expression format");
        }
    }

    // Guida(stat-->assign <expr> to <idlist>)={assign}
    // Guida(stat-->print[<exprlist>])={print}
    // Guida(stat-->read[<idlist>])={read}
    // Guida(stat-->while(<bexpr><stat>))={while}
    // Guida(stat-->COND[<optlist><statp>])={COND}
    // Guida(stat-->({<statlist>}))={'{'}

    public void stat(int lnext) {
        switch (look.tag) {
            case Tag.ASSIGN: // Assegna il valore dell’espressione ⟨expr⟩ a tutti gli identificatori inclusi
                             // nella lista di identificatori ⟨idlist⟩
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist(Tag.ASSIGN);
                break;
            case Tag.PRINT: // stampa il valore della lista delle espressioni
                match(Tag.PRINT);
                match('[');
                exprlist(Tag.PRINT);
                match(']');
                break;
            case Tag.READ: // legge un valore da tastiera e lo inserisce nell'id specifico
                match(Tag.READ);
                match('[');
                idlist(Tag.READ);
                match(']');
                break;
            case Tag.WHILE: // Permette l’esecuzione ciclica di ⟨stat⟩. La condizione per l’esecuzione del
                            // ciclo e` ⟨bexpr⟩.
                // System.out.println(lnext);
                // int label = count;

                match(Tag.WHILE); // riconosco il tag WHILE
                match('(');
                int while_true = code.newLabel();// creo l'etichetta per il true
                int while_false = code.newLabel();// creo l'etichetta per il false
                bexpr(while_true);
                code.emit(OpCode.GOto, while_false);
                code.emitLabel(while_true);
                match(')');
                stat(lnext);
                code.emit(OpCode.GOto, while_true - 1);
                code.emitLabel(while_false);
                break;
            case Tag.COND:
                int cond_false = code.newLabel();
                match(Tag.COND);
                match('[');
                optlist(cond_false);
                match(']');
                statp(cond_false);
                code.emitLabel(cond_false);
                break;
            case '{': // Permette di raggruppare una sequenza di istruzioni
                match('{');
                statlist(lnext);
                match('}');
                break;
            default:
                error("Syntax error in stat(): " + look.tag + " incorrect expression format");
        }
    }

    // Guida(statp-->end)={end}
    // Guida(statp-->else<stat>end)={else}
    private void statp(int lnext) {
        switch (look.tag) {
            case Tag.END:
                match(Tag.END);
                code.emit(OpCode.GOto, lnext);
                break;
            case Tag.ELSE:
                match(Tag.ELSE);
                stat(lnext);
                match(Tag.END);
                code.emit(OpCode.GOto, lnext);
                break;
            default:
                error("Syntax error in statp(): " + look.tag + " incorrect expression format");
        }
    }

    // Guida(idlist-->ID<idlistp>)={ID}
    private void idlist(int tag) {
        switch (look.tag) {
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                // Se la variabile non é stata inizializzata la inizializzo
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                idlistp(tag, id_addr);
                break;
            default:
                error("Syntax error in idlist(): " + look.tag + " incorrect expression format");
        }
    }

    // Guida(idlistp-->,ID<idlistp>)={,}
    // Guida(idlistp-->eps)={'}',;,END,EOF,option,]}
    private void idlistp(int tag, int adress) {
        switch (look.tag) {
            case ',':
                if (tag == Tag.ASSIGN)
                    code.emit(OpCode.dup);
                match(',');
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                if (tag == Tag.READ)
                    code.emit(OpCode.invokestatic, 0);
                if (tag == Tag.ASSIGN || tag == Tag.READ)
                    code.emit(OpCode.istore, adress);
                idlistp(tag, id_addr);
                break;
            case ']':
            case Tag.END:
            case Tag.EOF:
            case ';':
            case '}':
                if (tag == Tag.READ)
                    code.emit(OpCode.invokestatic, 0);
                if (tag == Tag.ASSIGN || tag == Tag.READ)
                    code.emit(OpCode.istore, adress);
                break;
            default:
                error("Syntax error in idlistp(): " + look.tag + " incorrect expression format");
        }

    }

    // Guida(optlist--><optitem><optlistp>)={OPTION}
    private void optlist(int lnext) {
        switch (look.tag) {
            case Tag.OPTION:
                optitem(lnext);
                optlistp(lnext);
                break;
            default:
                error("Syntax error in optlist(): " + look.tag + " incorrect expression format");
        }
    }

    // Guida(optlistp--><optitem><optlistp>)={OPTION}
    // Guida(optlistp-->EOF)={']'}
    private void optlistp(int lnext) {
        switch (look.tag) {
            case Tag.OPTION:
                optitem(lnext);
                optlistp(lnext);
                break;
            case Tag.EOF:
                match(Tag.EOF);
                break;
            case ']':
                break;
            default:
                error("Syntax error in optlistp(): " + look.tag + " incorrect expression format");
        }
    }

    // Guida(optitem-->option<bexpr>do<stat>)={option}
    private void optitem(int lnext) {
        switch (look.tag) {
            case Tag.OPTION:
                match(Tag.OPTION);
                match('(');
                int if_true = code.newLabel();
                int if_false = code.newLabel();
                bexpr(if_true);
                code.emit(OpCode.GOto, if_false);
                code.emitLabel(if_true);
                match(')');
                match(Tag.DO);
                stat(lnext);
                code.emit(OpCode.GOto, lnext);
                code.emitLabel(if_false);
                break;
            default:
                error("Syntax error in optitem(): " + look.tag + " incorrect expression format");
        }
    }

    // Guida(bexpr-->Relop<expr><expr>)={>,<,==,>=,<=,<>}
    private void bexpr(int lnext) {
        switch (look.tag) {
            case Tag.RELOP:
                String relop = ((Word) look).lexeme;
                match(Tag.RELOP);
                expr();
                expr();
                switch (relop) {
                    case ">":
                        code.emit(OpCode.if_icmpgt, lnext);
                        break;
                    case "<":
                        code.emit(OpCode.if_icmplt, lnext);
                        break;
                    case "==":
                        code.emit(OpCode.if_icmpeq, lnext);
                        break;
                    case ">=":
                        code.emit(OpCode.if_icmpge, lnext);
                        break;
                    case "<=":
                        code.emit(OpCode.if_icmple, lnext);
                        break;
                    case "<>":
                        code.emit(OpCode.if_icmpne, lnext);
                        break;
                    default:
                        error("Non hai utilizzato un operatore relazionale valido nella tua istruzione condizionale.");
                        break;
                }
                break;
            default:
                error("Syntax error in bexpr(): " + look.tag + " incorrect expression format");
                break;
        }
    }

    /*
     * <expr> --> +(<exprlist>) = {+}
     * <expr> --> *(<exprlist>) = {*}
     * <expr> --> -<expr><expr> = {-}
     * <expr> --> /<expr><expr> = {/}
     * <expr> --> NUM = NUM
     * <expr> --> ID = ID
     */
    private void expr() {
        switch (look.tag) {
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '+':
                match('+');
                match('(');
                exprlist('+');
                match(')');
                break;
            case '*':
                match('*');
                match('(');
                exprlist('*');
                match(')');
                break;
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc, ((NumberTok) look).value);
                match(Tag.NUM);
                break;
            case Tag.ID: // Se la variabile non é stata inizializzata la inizializzo
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    error("ERROR Identifier used but never created: " + look);
                }
                code.emit(OpCode.iload, id_addr);
                match(Tag.ID);
                break;
            default:
                error("Syntax error in expr(): " + look.tag + " incorrect expression format");
        }
    }

    // Guida(exprlist--><expr><exprlistp>)={+,-,*,/,),NUM,ID}
    private void exprlist(int tag) {
        switch (look.tag) {
            case '+':
            case '-':
            case '*':
            case '/':
            case ')':
            case NumberTok.tag:
            case Tag.ID:
                expr();
                exprlistp(tag);
                break;
            default:
                error("Syntax error in exprlist(): " + look.tag + " incorrect expression format");
        }
    }

    // Guida(exprlistp-->,<expr><exprlistp>)={,}
    // Guida(exprlistp-->eps)={),]}
    private void exprlistp(int tag) {
        switch (look.tag) {
            case ',':
                if (tag == Tag.PRINT)
                    code.emit(OpCode.invokestatic, 1);
                match(',');
                expr();            
                exprlistp(tag);
                if (tag == Token.plus.tag)
                    code.emit(OpCode.iadd);                
                else if (tag == Token.mult.tag)
                    code.emit(OpCode.imul);
                break;
            case ')':
            case ']':
                if (tag == Tag.PRINT)
                    code.emit(OpCode.invokestatic, 1);
                break;
            default:
                error("Syntax error in exprlistp(): " + look.tag + " incorrect expression format");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "input.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK, Generation of code succeded! :)");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }      


        /*
         * executeCommand("java -jar jasmin.jar Output.j");
         * executeCommand("java Output");
         * System.out.println("Test successful!!");
         */
    }


    
    
    /* 
    private static void executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            logOutput(process.getInputStream(), "");
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void logOutput(InputStream inputStream, String prefix) {
        new Thread(() -> {
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            while (scanner.hasNextLine()) {
                log(prefix + scanner.nextLine());
            }
            scanner.close();
        }).start();
    }

    private static synchronized void log(String message) {
        System.out.println(message);
    }
    */
}
