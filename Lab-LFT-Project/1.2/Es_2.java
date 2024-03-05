
public class Es_2
{
     public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if (ch >= '0' && ch <= '9')
                        state = -1;
                    else if (ch == '_')
                        state = 1;
                    else
                        state = 2;
                    break;
                case 1:
                    if (ch == '_')
                        state = 1;
                    else if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
                        state = 2;
                    else
                        state = -1;
                    break;
                case 2:
                    if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_')
                        state = 2;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 2;
    }

    public static void main(String[] args)
    {
			System.out.println((scan("x") ? "OK" : "NOPE")=="OK");//ok
			System.out.println((scan("flag1") ? "OK" : "NOPE")=="OK");//ok
			System.out.println((scan("x_1") ? "OK" : "NOPE")=="OK");//ok
			System.out.println((scan("lft_lab") ? "OK" : "NOPE")=="OK");//ok
			System.out.println((scan("_temp") ? "OK" : "NOPE")=="OK");//ok
			System.out.println((scan("__5") ? "OK" : "NOPE")=="OK");//ok
			System.out.println((scan("x___") ? "OK" : "NOPE")=="OK");//ok
			
			System.out.println((scan("5") ? "OK" : "NOPE")=="NOPE");//nope
			System.out.println((scan("221B") ? "OK" : "NOPE")=="NOPE");//nope
			System.out.println((scan("123") ? "OK" : "NOPE")=="NOPE");//nope
			System.out.println((scan("____") ? "OK" : "NOPE")=="NOPE");//nope
			System.out.println((scan("9_to_5_") ? "OK" : "NOPE")=="NOPE");//nope
			System.out.println((scan("_ __ _") ? "OK" : "NOPE")=="NOPE");//nope
			
			
			
    }
}