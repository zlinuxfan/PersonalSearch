package com.ps.Page;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class PageReader {
    private String fileName;
    private final int numberUrlPage;

    public PageReader(String file, int numberUrlInPage) throws FileNotFoundException {
        this.fileName = file;
        this.numberUrlPage = numberUrlInPage;
    }

    public ArrayList<Page> read() throws IOException {

        Reader in = null;
        try {
            in = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        CSVParser records = null;
        String[] headers = readHeader();
        ArrayList<Page> pages = new ArrayList<>();

        try {
            assert in != null;
            records = CSVFormat.DEFAULT.withDelimiter(';').withHeader(headers).parse(in);

            // skip header
            records.iterator().next();

            for (CSVRecord record : records) {

                if (headers.length != record.size()) {
                    System.out.println("Number row in header: " + headers.length + " > number row in line: " + record.size() + ".");
                    continue;
                }

                ArrayList<UrlInfo> urlInfos = new ArrayList<>();

                if (record.isMapped("Ссылка1-1")) {
                    urlInfos.add(
                            new UrlInfo(
                                    "file.csv",
                                    new URL(record.get("Ссылка1-1")),
                                    record.get("Заголовок1-1"),
                                    record.get("Описание1-1")
                            )
                    );
                }

                pages.add(
                        new Page.Builder(
                                null,
                                record.get("Название элемента"),
                                null,
                                null,
                                null,
                                null,
                                urlInfos,
                                numberUrlPage
                        )
                                .idYouTube("")
                                .searchQuery(record.get("Поисковый запрос"))
                                .elementTitle(record.get("Заголовок (title)"))
                                .build());
            }

        } catch (IOException e) {
            System.out.println("Can not parse line csv file." + Arrays.toString(e.getStackTrace()));
        } finally {
            if (records != null) {
                records.close();
            }

            if (in != null) {
                in.close();
            }
        }

        return pages;
    }

    private String[] readHeader() throws IOException {

        Reader in = null;
        try {
            in = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Iterable<CSVRecord> header = null;
        try {
            assert in != null;
            header = CSVFormat.DEFAULT.withDelimiter(';').parse(in);
        } catch (IOException e) {
            System.out.println("Can not parse header in csv file." + Arrays.toString(e.getStackTrace()));
        }

        ArrayList<String> headers = new ArrayList<>();

        assert header != null;
        for (String fieldName : header.iterator().next()) {
            headers.add(fieldName);
        }

        return headers.toArray(new String[headers.size()]);
    }
}
