package in.bluebytes.superbox.controller;

public class ControllerHelper {

    private ControllerHelper() {
        //Private constructor to avoid object creation
    }

    public static int sanitizeId(String id) {
        try{
            return Integer.parseInt(id);
        } catch(NumberFormatException nfe) {
            return -3; //some random number - should be negative
        }
    }
}
