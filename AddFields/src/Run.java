import java.util.ArrayList;

public class Run {

    public static void main(String[] args) {
        ArrayList<Page> pages = CsvFileReader_Page.readCsvFile("/home/devel/SEO/PersonalSearch/AddFields/data/Informationsystem_103_2017_11_07_18_57_48_correctly.csv");

        System.out.println(pages.size());

    }
}
