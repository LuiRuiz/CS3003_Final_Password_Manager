import java.io.Serializable;
import java.util.*;

public class NewPassword extends Password implements Serializable {
    private final String SYMBOLS = "$!_@?*&";
    private ArrayList<Integer> IncludedTypes = new ArrayList<>();
    private String tempPassword = "";

    // new password constructor. Populates IncludedTypes and generates a password
    public NewPassword (int passLength,String name ){
        super(passLength,name);
        IncludedTypes.add(0); //enables lower case
        IncludedTypes.add(1); // enable numbers
        IncludedTypes.add(2); // enables upper case
        IncludedTypes.add(3); // enables lower case
        // loop through numerical representation of randomized password
        for (Integer i : getPasswordArray(passLength)) {
            // turn int of getPasswordArray to char then add char
            tempPassword += getCharFromNum(i);
        }
        //set password
        this.password = tempPassword;
    }

    /*
    * This is a function to get a randomized interger array to represent a password
    * by the symbols it is derived from
    * it requires getCharFromNum to be used on each elm to create a password
    * */
    public Integer[] getPasswordArray(int length){
        Random rand = new Random();
        Integer[] passwordArray =  new Integer[length];
        Arrays.fill(passwordArray, 0);
        for (Integer i = 0; i < passwordArray.length; i++){
            // ensures that one of each character types is used
            if (i < IncludedTypes.size()){
                passwordArray[i]= IncludedTypes.get(i);
            }else{
                // add random character type to password array
                passwordArray[i]= IncludedTypes.get(rand.nextInt(IncludedTypes.size()));
            }
        }
        List<Integer> intList = Arrays.asList(passwordArray);
        Collections.shuffle(intList);
        intList.toArray(passwordArray);
        return passwordArray;
    }
    /*
    * Takes an integer 0-3 and translates it to a random char of specific type
    * */
    public char getCharFromNum(int i){
        Random rand = new Random();
        int min, max;
        char genChar = ' ';

        switch(i){
            case 3:
                //sybols
                min = 0;
                max = 0;
                genChar =  SYMBOLS.charAt(rand.nextInt(SYMBOLS.length()));
                break;
            case 0:
                //lowercase
                max =122;
                min = 97;
                genChar = (char) (rand.nextInt(max - min + 1) + min);
                break;
            case 2:
                max = 90;
                min = 65;
                //upper case
                genChar = (char) (rand.nextInt(max - min + 1) + min);
                break;
            case 1:
                //
                max = 57;
                min = 48;
                // number
                genChar = (char) (rand.nextInt(max - min + 1) + min);
                break;
        }
        return genChar;
    }
}
