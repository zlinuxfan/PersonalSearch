import java.io.*;

public class Run {
    private static final String patchData = "/home/devel/SEO/PersonalSearch/CountSiteInGoogle/data/";
    private static final String fileData = "work-domains";

    public static void main(String[] args) throws IOException, InterruptedException {
        Google google = new Google();
        BufferedReader in = new BufferedReader(new FileReader(new File(patchData + fileData + ".txt")));
        BufferedWriter out = new BufferedWriter(new FileWriter(new File(patchData + fileData + "_WithCounter.txt"), true));

        String line;
//        System.out.println(getNumber(line));

        int counter = 1;
        String answerNumber;
        while ((line = in.readLine()) != null) {

            System.out.print(counter + ") ");
            String counterSite = google.counterSite(line);

            if (! counterSite.equals("")) {
                answerNumber = getNumber(counterSite);
            } else {
                answerNumber = "0";
            }

            out.write(line + "; " + answerNumber);
            out.write(System.lineSeparator());
            out.flush();


//            if (counter > 3) {
//                break;
//            }

            counter++;
            Thread.sleep(11000 + (int) (Math.random() * (35_000 + (int) (Math.random() * 120_000))));
        }

        out.close();
        in.close();
    }

    static String getNumber(String line) {
        System.out.println("-> " + line);
        String[] split = line.split(":");
        String s = split[1] != null ? (split[1].replaceAll("\\([^)]+\\)", "")) : "0";

        return s.replaceAll(" ", "");
    }
}
