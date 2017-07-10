package com.roberttisma.blog1.csv.bridges.impl.simple;

import com.roberttisma.blog1.csv.bridges.CsvWriterBridge;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridgeContext;

import java.io.IOException;
import java.io.Writer;

import static com.roberttisma.blog1.csv.Utils.closeObject;
import static com.roberttisma.blog1.csv.Utils.resolveChar;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class SimpleStringArrayCsvWriterBridge implements CsvWriterBridge<String[]> {

  private static final Character NEWLINE_CHAR = '\n';

  private final Writer writer;
  private final String[] header;
  private final String quoteChar;
  private final String joiningSequence;

  private SimpleStringArrayCsvWriterBridge(Writer writer, String[] header, Character separatorChar,
      Character quote) {
    this.writer = writer;
    this.header = header;
    this.quoteChar = resolveChar(quote);
    this.joiningSequence = quoteChar+separatorChar+quoteChar;
  }

  @Override
  public void write(String[] object) throws IOException {
    writer.write(toCsv(object));
  }

  @Override
  public void writeHeader() throws IOException {
    write(header);
  }

  @Override
  public void close() throws IOException {
    closeObject(writer);
  }

  private String toCsv(String[] data){
    StringBuilder sb = new StringBuilder();
    sb.append(quoteChar);
    sb.append(stream(data).collect(joining(joiningSequence)));
    sb.append(quoteChar);
    sb.append(NEWLINE_CHAR);
    return sb.toString();
  }

  public static <T> SimpleStringArrayCsvWriterBridge createSimpleStringArrayCsvWriterBridge(CsvWriterBridgeContext<T>
      ctx) {
    return new SimpleStringArrayCsvWriterBridge(ctx.buildWriter(),ctx.getAlternativeFieldNames(),
        ctx.getSeparator(), ctx.getQuote());
  }

}
