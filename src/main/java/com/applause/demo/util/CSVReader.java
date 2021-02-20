package com.applause.demo.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.SKIP_EMPTY_LINES;

public class CSVReader {
    private final static Logger logger = LoggerFactory.getLogger(CSVReader.class);

    public static <T> List<T> readList(Class<T> type, String fileName) {
        try {
            CsvSchema schema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            File file = new ClassPathResource(fileName).getFile();
            MappingIterator<T> readValues =
                    mapper.readerFor(type).with(schema).with(SKIP_EMPTY_LINES).readValues(file);

            return readValues.readAll();
        } catch (Exception e) {
            logger.error("Error occurred while loading object list from file " + fileName, e);
            return Collections.emptyList();
        }
    }
}
