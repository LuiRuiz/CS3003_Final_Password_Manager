//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try{
            AESFileManager.generateKeyAndIV();
        }catch(Exception e) {
            e.printStackTrace();
        }
        PasswordApp app = new PasswordApp();
        app.start();
    }
}