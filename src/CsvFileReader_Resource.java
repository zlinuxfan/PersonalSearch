import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CsvFileReader_Resource {
    private static final String DELIMITER = ";";

    // attributes index
    private static final int RESOURCE_NAME_REQUEST = 0; //Название;
    private static final int RESOURCE_TEXT_OF_ELEMENT = 1; //Заголовок;
    private static final int RESOURCE_TITLE = 2; //Title;
    private static final int RESOURCE_DESCRIPTION = 3; //Description;
    private static final int RESOURCE_COMMON_QUESTION = 4; //Общий вопрос

    public static ArrayList<Resource> readCsvFile(String fileName) {

        BufferedReader fileReader = null;
        ArrayList<Resource> resource = new ArrayList<>();

        try {

            fileReader = new BufferedReader(new FileReader(fileName));
            fileReader.readLine();
            String line;

            while ((line = fileReader.readLine()) != null) {

                String[] tokens = line.split(DELIMITER);

                if (tokens.length == 5) {
                    resource.add(new Resource(
                            tokens[RESOURCE_NAME_REQUEST],
                            tokens[RESOURCE_TEXT_OF_ELEMENT],
                            tokens[RESOURCE_TITLE],
                            tokens[RESOURCE_DESCRIPTION],
                            tokens[RESOURCE_COMMON_QUESTION]
                    ));
                } else if (tokens.length == 4) {
                    resource.add(new Resource(
                            tokens[RESOURCE_NAME_REQUEST],
                            tokens[RESOURCE_TITLE],
                            tokens[RESOURCE_DESCRIPTION],
                            tokens[RESOURCE_TEXT_OF_ELEMENT]
                            ));
                } else {
                    resource.add(new Resource(
                            tokens[RESOURCE_NAME_REQUEST],
                            tokens[RESOURCE_TITLE],
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
