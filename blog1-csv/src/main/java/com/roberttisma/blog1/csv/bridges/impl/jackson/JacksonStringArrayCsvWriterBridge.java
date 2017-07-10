package com.roberttisma.blog1.csv.bridges.impl.jackson;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridge;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridgeContext;

import java.io.IOException;
import java.io.Writer;

import static com.roberttisma.blog1.csv.Utils.closeObject;
import static com.roberttisma.blog1.csv.bridges.impl.jackson.JacksonUtils.createStringArraySequenceWriter;

public class JacksonStringArrayCsvWriterBridge implements CsvWriterBridge<String[]> {

  private final SequenceWriter sequenceWriter;
  private final String[] header;

  public JacksonStringArrayCsvWriterBridge(SequenceWriter sequenceWriter, String[] header) {
    this.sequenceWriter = sequenceWriter;
    this.header = header;
  }

  @Override
  public void write(String[] object) throws IOException {
    this.sequenceWriter.write(object);
  }

  @Override
  public void writeHeader() throws IOException {
    this.sequenceWriter.write(header);
  }

  @Override
  public void close() throws IOException {
    closeObject(sequenceWriter);
  }

  public static JacksonStringArrayCsvWriterBridge createJacksonStringArrayCsvWriterBridge(CsvWriterBridgeContext ctx)
      throws IOException {
    Writer writer = ctx.buildWriter();
    SequenceWriter sequenceWriter = createStringArraySequenceWriter(ctx.getSeparator(), ctx.getQuote(), writer);
    return new JacksonStringArrayCsvWriterBridge(sequenceWriter, ctx.getAlternativeFieldNames());
  }

}
