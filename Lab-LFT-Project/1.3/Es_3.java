
public class Es_3 {
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
          else
            state = -1;
          break;
        case 1:
          if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
            state = 1;
          else if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
            state = 2;
          else if (ch >= 'A' && ch <= 'K')
            state = 3;
          else if (ch >= 'L' && ch <= 'Z')
            state = 4;
          else
            state = -1;
          break;
        case 2:
          if (ch == '1' || ch == '3' || ch == '5' || ch == '7' || ch == '9')
            state = 2;
          if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
            state = 1;
          else if (ch >= 'A' && ch <= 'K')
            state = 5;
          else if (ch >= 'L' && ch <= 'Z')
            state = 6;
          else
            state = -1;
          break;
        case 3:
          if (ch >= 'a' && ch <= 'z')
            state = 3;
          else
            state = -1;
          break;
        case 4:
          if (ch >= 'a' && ch <= 'z')
            state = 4;
          else
            state = -1;
          break;
        case 5:
          if (ch >= 'a' && ch <= 'z')
            state = 5;
          else
            state = -1;
          break;
        case 6:
          if (ch >= 'a' && ch <= 'z')
            state = 6;
          else
            state = -1;
          break;

      }
    }
    return state == 3 || state == 6;
  }

  public static void main(String[] args) {
   // System.out.println(scan(args[0]) ? "OK" : "NOPE");
    System.out.println(scan("123454Bianchi") ? "OK" : "NOPE");//ok
    System.out.println(scan("654321Rossi") ? "OK" : "NOPE");//ok
	System.out.println(scan("2Bianchi") ? "OK" : "NOPE");//ok
	System.out.println(scan("122B") ? "OK" : "NOPE");//ok
	 
	System.out.println(scan("Rossi") ? "OK" : "NOPE");//nope
	System.out.println(scan("654322") ? "OK" : "NOPE");//nope
    System.out.println(scan("654321BiaNchi") ? "OK" : "NOPE");//nope
    System.out.println(scan("654321Bianchi") ? "OK" : "NOPE");//nope
    System.out.println(scan("123456Rossi") ? "OK" : "NOPE");//nope


  }
}