public class Es_9 {

    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (ch == '/')
                        state = 1;
                    else
                        state = -1;
                    break;
                case 1:
                    if (ch == '*')
                        state = 2;
                    else
                        state = -1;
                    break;
                case 2:
                    if (ch == '*')
                        state = 3;
                    else if (ch == 'a')
                        state = 4;
                    else if (ch == '/')
                        state = 5;
                    else
                        state = -1;

                    break;
                case 3:
                    if (ch == '*')
                        state = 3;
                    else if (ch == 'a')
                        state = 4;
                    else if (ch == '/')
                        state = 6;
                    else
                        state = -1;
                    break;
                case 4:
                    if (ch == '*')
                        state = 3;
                    else if (ch == 'a')
                        state = 4;
                    else if (ch == '/')
                        state = 5;
                    else
                        state = -1;
                    break;
                case 5:
                    if (ch == 'a')
                        state = 4;
                    else if (ch == '/')
                        state = 5;
                    else if (ch == '*')
                        state = 3;
                    else
                        state = -1;
                    break;
                case 6:
                    /*
                     * if (ch == '/')
                     * state = -1;
                     * else if (ch == '*')
                     * state = -1;
                     * else if (ch == 'a')
                     * state = -1;
                     * else
                     */
                    state = -1;
                    break;

            }
        }
        return (state == 6);
    }

    public static void main(String[] args) {
        System.out.println(scan("/*/*/") ? "OK" : "NOPE");// ok
        System.out.println(scan("/*a*/") ? "OK" : "NOPE");// ok
        System.out.println(scan("/*a*a///*/") ? "OK" : "NOPE");// ok
        System.out.println(scan("/*a*a*/") ? "OK" : "NOPE");// ok
        System.out.println(scan("/*a/**/") ? "OK" : "NOPE");// ok
        System.out.println(scan("/*****/") ? "OK" : "NOPE");// ok
        System.out.println(scan("/**a**/a*/") ? "OK" : "NOPE"); // nope
        System.out.println(scan("/*prova*/") ? "OK" : "NOPE"); // nope
        System.out.println(scan("/***a/") ? "OK" : "NOPE");// nope/
        System.out.println(scan("*/") ? "OK" : "NOPE");// nope
        System.out.println(scan(".") ? "OK" : "NOPE");// nope
        System.out.println(scan("/**/") ? "OK" : "NOPE");// ok
        System.out.println(scan("**/***/") ? "OK" : "NOPE");// nope
        System.out.println(scan("/*/*/*/") ? "OK" : "NOPE");// nope
        System.out.println(scan("/**/***/") ? "OK" : "NOPE");// nope
        System.out.println(scan("/*******a*/*/////") ? "OK" : "NOPE");// nope
        System.out.println(scan("/*******a*/********") ? "OK" : "NOPE");// nope
        System.out.println(scan("/*******a*/****/*/**/aa*/") ? "OK" : "NOPE");// nope
    }

}
