package com.roberttisma.blog1.csv.bridges.impl.opencsv;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridge;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridgeContext;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;

import static com.roberttisma.blog1.csv.Utils.closeObject;
import static com.roberttisma.blog1.csv.Utils.resolveChar;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class OpenCsvBeanCsvWriterBridge<T> implements CsvWriterBridge<T> {

  private final StatefulBeanToCsv<T> beanWriter;
  private final Writer internalWriter;
  private final String[] header;
  private final String quoteString;
  private final String joiningSeq;

  public OpenCsvBeanCsvWriterBridge(StatefulBeanToCsv<T> beanWriter, Writer internalWriter,
      String[] header, Character separatorChar, Character quoteChar) {
    this.beanWriter = beanWriter;
    this.internalWriter = internalWriter;
    this.header = header;

    String separatorString = resolveChar(separatorChar);
    this.quoteString = resolveChar(quoteChar);
    this.joiningSeq = quoteString+separatorString+quoteString;
  }

  @Override
  public void write(T object) throws IOException {
    try {
      beanWriter.write(object);
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  /**
   *
   * The standard OpenCSV StatefulBeanToCsv class does not support alternative field names.
   * Since there is no option to write the header separately from the data, the header is
   * MANUALLY transformed into a string and writen to file. This method is not RFC 4180 compliant
   * and is considered a hack.
   */
  @Override
  public void writeHeader() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(quoteString);
    sb.append(stream(header).collect(joining(joiningSeq)));
    sb.append(quoteString);
    sb.append("\n");
    internalWriter.write(sb.toString());
  }

  @Override
  public void close() throws IOException {
    closeObject(internalWriter);
  }

  public static <T> OpenCsvBeanCsvWriterBridge<T> createOpenCsvBeanCsvWriterBridge
      (CsvWriterBridgeContext<T> ctx, Class<? extends T> clazz) {
    return createOpenCsvBeanCsvWriterBridge(ctx.buildWriter(), ctx.getFieldNames(),
        ctx.getSeparator(), ctx.getQuote(), ctx.getAlternativeFieldNames(), clazz);
  }

  public static <T> OpenCsvBeanCsvWriterBridge<T> createOpenCsvBeanCsvWriterBridge
      (Writer writer, String[] fieldNames, Character separator, Character quote,
          String[] header, Class<? extends T> clazz){

    try {

      CustomOrderedHeaderMappingStrategy<T> strategy = new CustomOrderedHeaderMappingStrategy<>(
          fieldNames,
          clazz);
      StatefulBeanToCsv<T> beanWriter = new StatefulBeanToCsvBuilder<T>(writer)
          .withMappingStrategy(strategy)
          .withSeparator(separator)
          .withQuotechar(quote)
          .build();
      enableHeadersHack(beanWriter,false);
      return new OpenCsvBeanCsvWriterBridge<>(beanWriter, writer, header, separator, quote);

    } catch (Throwable e){
      throw new RuntimeException(e);
    }
  }

  /**
   * Since writing of the header is ALWAYS enabled, this function used the java reflection api
   * to hack the headerWritten flag to !enableHeader. If enableHeader = true, then the headerWritten
   * flag is set to false, and vice versa for when enableHeader = false
   */
  private static <T> void enableHeadersHack(StatefulBeanToCsv<T> beanToCsv, boolean enableHeader)
      throws NoSuchFieldException, IllegalAccessException {
        Field headerWrittenField = StatefulBeanToCsv.class.getDeclaredField("headerWritten");
        headerWrittenField.setAccessible(true);
        headerWrittenField.set(beanToCsv, !enableHeader);
  }


}
