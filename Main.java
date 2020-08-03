import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TextEditor txt = new TextEditor();
        if (args.length > 0) {
            if (args[0].equals("vim"))
                txt.readFile(args[1]);
        }
        txt.showTxt();
        while (scanner.hasNext()) {
            txt.inputHandler(scanner.nextLine());
        }
    }
}
