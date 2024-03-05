public class Es_10 {

    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        // (sull’alfabeto {/, *, a})

       
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if (ch == '/')
                        state = 1;
                    else if (ch == 'a')
                        state = 3;
                    else if (ch == '*')
                        state = 2;
                    else
                        state = -1;
                    break;
                case 1:
                    if (ch == '*')
                        state = 4;
                    else if (ch == 'a')
                        state = 3;
                    else if (ch == '/')
                        state = 1;
                    else
                        state = -1;
                    break;
                case 2:
                    if (ch == '*')
                        state = 2;
                    else if (ch == 'a')
                        state = 3;
                    else if (ch == '/')
                        state = 1;
                    else
                        state = -1;
                    break;
                case 3:
                    if (ch == 'a')
                        state = 3;
                    else if (ch == '/')
                        state = 1;
                    else if (ch == '*')
                        state = 2;
                    else
                        state = -1;
                    break;
                case 4:
                    if (ch == '*')
                        state = 5;
                    else if (ch == 'a')
                        state = 4;
                    else if (ch == '/')
                        state = 4;         
                    else
                        state = -1;
                    break;
                case 5:
                    if(ch == '/')
                        state = 6;
                    else if(ch == 'a')
                        state =4;
                    else if(ch == '*')
                        state =5;
                    else state =-1;
                    break;
                case 6:
                    if(ch== '*')
                        state = 6;
                    else if(ch == 'a')
                        state = 6;
                    else if(ch == '/')
                        state = 1;
                    else state =-1;
                    break;
                    
            }
        }
       // System.out.println(state);
        return (state == 3 || state == 6 || state==2 || state ==1) ;
    }

    public static void main(String[] args) {
       
        System.out.println(scan("/*aaaaaa***///*/*/****")? "OK" : "NOPE"); //ok
        System.out.println(scan("aaa/****/aa") ? "OK" : "NOPE");// ok
        System.out.println(scan("aa/") ? "OK" : "NOPE");// ok
        System.out.println(scan("aa") ? "OK" : "NOPE");// ok
        System.out.println(scan("/****/") ? "OK" : "NOPE");// ok
        System.out.println(scan("aaaa") ? "OK" : "NOPE");// ok
        System.out.println(scan("/*aa*/") ? "OK" : "NOPE");// ok
        System.out.println(scan("*/a") ? "OK" : "NOPE");// ok
        System.out.println(scan("a/**/***/a") ? "OK" : "NOPE");// ok
        System.out.println(scan("a/**/aa/***/a") ? "OK" : "NOPE");// ok
        System.out.println(scan("**/aa/***/a") ? "OK" : "NOPE");// ok
        System.out.println((scan("/*aa*a/aaaa*/")? "OK" : "NOPE"));//ok
        System.out.println((scan("/*aa*a/aa*/a*/")? "OK" : "NOPE"));//ok
        System.out.println("------------------");
        System.out.println(scan("/*aa*a") ? "OK" : "NOPE");// nope
        System.out.println(scan("/**/aa/**") ? "OK" : "NOPE");// nope
        System.out.println(scan("aaa/*/aa") ? "OK" : "NOPE");// nope
        System.out.println(scan("aaa/*/aa") ? "OK" : "NOPE");// nope
        System.out.println(scan("aa/*aa") ? "OK" : "NOPE");// nope
        System.out.println(scan("a/**//***a") ? "OK" : "NOPE");// nope
        System.out.println((scan("/*aa")? "OK" : "NOPE"));//nope
        System.out.println((scan("/*aa*a/")? "OK" : "NOPE"));//nope
        System.out.println(scan("/*aaaaaa***")? "OK" : "NOPE"); //nope
        System.out.println(scan("/*aaaaaa*/*/*")? "OK" : "NOPE"); //nope
        System.out.println(scan("/*aaaaaa")? "OK" : "NOPE"); //nope
        System.out.println(scan("/*******************")? "OK" : "NOPE"); //nope
       
    }

}
