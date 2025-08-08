import java.io.*;

public class Password implements Serializable {
    protected String name;
    protected String password;
    protected int passLength;
    // not used, made for manually setting passwords
    Password(int passLength, String name, String password){
        this.passLength = passLength;
        this.name = name;
        this.password = password;
    }
    //Overloaded constructor for NewPassword so that password attribute can be generated
    public Password(int passLength, String name) {
        this.passLength = passLength;
        this.name = name;
    }

    public final String getName(){
        return name;
    }

    public final String getPassword(){
        return password;
    }
}
