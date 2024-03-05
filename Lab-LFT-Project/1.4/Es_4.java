
public class Es_4 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 1;
                    else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
                        state = 2;
                    else if (ch == ' ')
                        state = 0;
                    else
                        state = -1;
                    break;
                case 1:
                    if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 1;
                    else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
                        state = 2;
                    else if (ch == ' ')
                        state = 3;
                    else if (ch >= 'A' && ch <= 'K')
                        state = 4;
                    else
                        state = -1;
                    break;
                case 2:
                    if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
                        state = 2;
                    if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 1;
                    else if (ch == ' ')
                        state = 5;
                    else if (ch >= 'L' && ch <= 'Z')
                        state = 6;
                    else
                        state = -1;
                    break;
                case 3:
                    if (ch == ' ')
                        state = 3;
                    else if (ch >= 'A' && ch <= 'K')
                        state = 4;
                    else
                        state = -1;
                    break;
                case 4:
                    if (ch >= 'a' && ch <= 'z')
                        state = 4;
                    else if (ch == ' ')
                        state = 8;
                    else
                        state = -1;
                    break;
                case 5:
                    if (ch == ' ')
                        state = 5;
                    else if (ch >= 'L' && ch <= 'Z')
                        state = 6;
                    else
                        state = -1;
                    break;
                case 6:
                    if (ch >= 'a' && ch <= 'z')
                        state = 6;
                    else if (ch == ' ')
                        state = 7;
                    break;
                case 7:
                    if (ch >= 'A' && ch <= 'Z')
                        state = 6;
                    else if (ch == ' ')
                        state = 8;
                    else
                        state = -1;
                    break;
                case 8:
                    if (ch >= 'A' && ch <= 'Z')
                        state = 4;
                    else if (ch == ' ')
                        state = 8;
                    else
                        state = -1;
                    break;

            }
        }
        return (state == 4 || state == 6 || state == 8);
    }

    public static void main(String[] args) {
        // System.out.println(scan(args[0]) ? "OK" : "NOPE");
        System.out.println(scan("654321Rossi  Rossi") ? "OK" : "NOPE");// ok
        System.out.println(scan("654321 Rossi") ? "OK" : "NOPE");// okA
        System.out.println(scan("123456 Bianchi      ") ? "OK" : "NOPE");// ok
        System.out.println(scan("1234 56Bianchi") ? "OK" : "NOPE");// nope
        System.out.println(scan("123456Bia nchi") ? "OK" : "NOPE"); // nope
        System.out.println(scan("123456De Gasperi") ? "OK" : "NOPE"); // ok
        System.out.println(scan("    123456De Gasperi    ") ? "OK" : "NOPE"); // ok
        System.out.println(scan("12 3456De Gasperi    ") ? "OK" : "NOPE"); // nope
        System.out.println(scan("    123456De Gas Peri    ") ? "OK" : "NOPE"); // ok
        System.out.println(scan("   12345Ze Gape i") ? "OK" : "NOPE"); // nope
         System.out.println(scan("           1234 Bianchi Maurizio               ") ? "OK" : "NOPE"); // ok


    }

}
