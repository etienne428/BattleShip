package Utils;

public class Parser {

    public static String getCommand(String s) throws CommandException {
        if (s.length() < 5) {
            throw new CommandException();
        }
        String c = (s.substring(0, 5));
        return c.toUpperCase();
    }

    public static String getElement(String s) {
        s = s.substring(5);
        return s.replaceAll(" ", "");
    }
}
