import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CsvFileReader_Resource {
    private static final String DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static String PAGE_HEADER = "";

    // attributes index
    private static final int RESOURCE_NAME_REQUEST = 0;
    private static final int RESOURCE_TITLE = 1;
    private static final int RESOURCE_DESCRIPTION = 2;

    public static ArrayList<Resource> readCsvFile(String fileName) {

        BufferedReader fileReader = null;
        ArrayList<Resource> resource = new ArrayList<>();

        try {

            fileReader = new BufferedReader(new FileReader(fileName));
            fileReader.readLine();
            String line;

            while ((line = fileReader.readLine()) != null) {

                String[] tokens = line.split(DELIMITER);

                if (tokens.length > 0) {
                    resource.add(new Resource(
                            tokens[RESOURCE_NAME_REQUEST],
                            tokens[RESOURCE_TITLE],
                            tokens[RESOURCE_DESCRIPTION]
                            ));
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                assert fileReader != null;
                fileReader.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }

        return resource;
    }
}
