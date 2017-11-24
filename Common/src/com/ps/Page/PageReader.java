package com.ps.Page;

import com.Page;
import com.UrlInfo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class PageReader {
    private static final char DELIMITER = ';';
    private Reader in = null;

    public PageReader(String file) throws FileNotFoundException {
        this.in = new FileReader(file);
    }

    public ArrayList<Page> read() throws IOException {

        CSVParser records = null;
        String[] headers = readHeader();
        ArrayList<Page> pages = new ArrayList<>();

        try {
            records = CSVFormat.DEFAULT.withDelimiter(';').withHeader(headers).parse(in);

            for (CSVRecord record : records) {

                if (headers.length != record.size()) {
                    System.out.println("Number row in header: " + headers.length + " > number row in line: " + record.size() + ".");
                    continue;
                }

                ArrayList<UrlInfo> urlInfos = new ArrayList<>();
                urlInfos.add(
                        new UrlInfo(
                                "file.csv",
                                record.get("Ссылка1-1"),
                                record.get("Заголовок1-1"),
                                record.get("Описание1-1")
                        )
                );

                pages.add(
                        new Page.Builder(
                                record.get("GUID идентификатор элемента"),
                                record.get("Название элемента"),
                                record.get("Описание элемента"),
                                record.get("Текст для элемента"),
                                record.get("Путь к элементу"),
                                null,
                                urlInfos
                        ).idYouTube(
                                record.get("Адрес (youtube)")
                        ).build());
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
        CSVParser header = null;
        ArrayList<String> headers = new ArrayList<>();

        try {
            header = CSVFormat.DEFAULT.withDelimiter(DELIMITER).parse(in);
        } catch (IOException e) {
            System.out.println("Can not parse header in csv file." + Arrays.toString(e.getStackTrace()));
        }

        if (header == null) {
            throw new IOException("Can not read header in csv file");
        }

        for (String fieldName : header.iterator().next()) {
            headers.add(fieldName);
        }

        return headers.toArray(new String[headers.size()]);
    }
}
