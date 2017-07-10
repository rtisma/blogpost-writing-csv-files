package com.roberttisma.blog1.csv.bridges.impl.jackson;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.io.Writer;

import static com.fasterxml.jackson.dataformat.csv.CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS;
import static com.roberttisma.blog1.csv.Config.EMPTY_CHARACTER;

public class JacksonUtils {

  public static SequenceWriter createBeanSequenceWriter(Character separator, Character quote,
      String[] fieldNames, Class<?> clazz, Writer writer) throws IOException {
    CsvMapper csvMapper = new CsvMapper();
    CsvSchema csvSchema = csvMapper.schemaFor(clazz)
        .withColumnSeparator(separator)
        .sortedBy(fieldNames);

    if (!quote.equals(EMPTY_CHARACTER)){
      csvSchema.withQuoteChar(quote);
      csvMapper.configure(ALWAYS_QUOTE_STRINGS, true);
    }

    return csvMapper
        .writer(csvSchema)
        .writeValues(writer);
  }

  public static SequenceWriter createStringArraySequenceWriter(Character separator,
      Character quote, Writer writer) throws IOException {
    CsvMapper csvMapper = new CsvMapper();

    CsvSchema csvSchema = csvMapper.schema()
        .withColumnSeparator(separator)
        .withoutHeader();

    if (!quote.equals(EMPTY_CHARACTER)){
      csvSchema.withQuoteChar(quote);
      csvMapper.configure(ALWAYS_QUOTE_STRINGS, true);
    }

    return csvMapper
        .writer(csvSchema)
        .writeValues(writer);
  }

}
