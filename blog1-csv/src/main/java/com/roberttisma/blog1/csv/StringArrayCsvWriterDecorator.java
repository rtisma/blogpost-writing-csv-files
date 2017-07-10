package com.roberttisma.blog1.csv;

import com.roberttisma.blog1.csv.bridges.CsvWriterBridge;
import com.roberttisma.blog1.csv.converter.Converter;

import java.io.IOException;

import static com.roberttisma.blog1.csv.Utils.closeObject;

public class StringArrayCsvWriterDecorator<T> implements CsvWriterBridge<T> {

  private final CsvWriterBridge<String[]> arrayCsvWriter;
  private final Converter<T, String[]> converter;


  public StringArrayCsvWriterDecorator( CsvWriterBridge<String[]> arrayCsvWriter,
      Converter<T, String[]> converter) {
    this.arrayCsvWriter = arrayCsvWriter;
    this.converter = converter;
  }


  @Override
  public void write(T object) throws IOException {
    String[] data = converter.convert(object);
    arrayCsvWriter.write(data);
  }

  @Override
  public void writeHeader() throws IOException {
    this.arrayCsvWriter.writeHeader();
  }

  @Override
  public void close() throws IOException {
    closeObject(arrayCsvWriter);
  }

  public static <T> StringArrayCsvWriterDecorator<T> createStringArrayCsvWriterDecorator(CsvWriterBridge<String[]>
      arrayCsvWriter, Converter<T, String[]> converter){
    return new StringArrayCsvWriterDecorator<T>(arrayCsvWriter, converter);
  }

}
