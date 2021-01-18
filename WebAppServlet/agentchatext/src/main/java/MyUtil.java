import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;

public class MyUtil {

    public static List<String> strings = new ArrayList<String>();

    public static List<String> getData(HttpServletRequest request) throws ServletException, IOException {

        DataInputStream inputStream = new DataInputStream(request.getInputStream());
        System.out.println("Input Stream: " + inputStream);
        byte[] array = inputStream.readAllBytes();
        String s = new String(array);

        System.out.println("Data: " + s);

        strings.add(s);

        System.out.println("List: " + strings);

        return strings;

    }

    public List<String> getLoginList() {
        return strings;
    }

}
