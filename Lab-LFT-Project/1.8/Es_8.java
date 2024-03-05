public class Es_8 {

    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (ch == '+' || ch == '-')
                        state = 1;
                    else if (ch >= 48 && ch <= 57)
                        state = 2;
                    else if (ch == '.')
                        state = 3;
                    else
                        state = -1;
                    break;
                case 1:
                    if (ch >= 48 && ch <= 57)
                        state = 2;
                    else if(ch == '.')
                        state= 3;
                    else
                        state = -1;
                    break;
                case 2:
                    if (ch >= 48 && ch <= 57)
                        state = 2;
                    else if (ch == '.')
                        state = 3;
                    else if (ch == 'e')
                        state = 5;
                    else
                        state = -1;
                    break;
                case 3:
                    if (ch >= 48 && ch <= 57)
                        state = 4;
                    else
                        state = -1;
                    break;
                case 4:
                    if (ch >= 48 && ch <= 57)
                        state = 4;
                    else if (ch == 'e')
                        state = 5;
                    else
                        state = -1;
                    break;
                case 5:
                    if (ch >= 48 && ch <= 57 || ch == '+' || ch == '-')
                        state = 6;
                    else
                        state = -1;
                    break;
                case 6:
                    if (ch >= 48 && ch <= 57)
                        state = 6;
                    else if (ch == '.')
                        state = 7;
                    else
                        state = -1;
                    break;
                case 7:
                    if (ch >= 48 && ch <= 57)
                        state = 6;
                    else
                        state = -1;
                    break;
            }
        }
        // System.out.println(state);
        return (state == 2 || state == 6 || state == 4);
    }

    public static void main(String[] args) {
        // System.out.println(scan(args[0]) ? "OK" : "NOPE");
        // control test 
        System.out.println(scan(".567") ? "OK" : "NOPE");// ok
        System.out.println(scan("123.5") ? "OK" : "NOPE");// ok
        System.out.println(scan(".567") ? "OK" : "NOPE");// ok
        System.out.println(scan("+7.5") ? "OK" : "NOPE"); // ok
        System.out.println(scan("67e10") ? "OK" : "NOPE"); // ok
        System.out.println(scan("1e-2") ? "OK" : "NOPE");// ok
        System.out.println(scan("1e2.3") ? "OK" : "NOPE");// ok
        System.out.println(scan("-.7e2") ? "OK" : "NOPE");// ok
        System.out.println(scan("1.2.3") ? "OK" : "NOPE");// nope
        System.out.println(scan("e3") ? "OK" : "NOPE");// nope
        System.out.println(scan("+e3") ? "OK" : "NOPE");// nope
        System.out.println(scan("123.") ? "OK" : "NOPE");// nope
        System.out.println(scan("++3") ? "OK" : "NOPE");// nope
        System.out.println(scan("+e6") ? "OK" : "NOPE");// nope
        System.out.println(scan("4e5e6") ? "OK" : "NOPE");// nope
        System.out.println(scan("--") ? "OK" : "NOPE");// nope
        System.out.println(scan("+.-98e") ? "OK" : "NOPE");// nope

    }

}
