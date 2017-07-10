package com.roberttisma.blog1.csv.bridges.impl.jackson;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridge;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridgeContext;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static com.roberttisma.blog1.csv.Utils.closeObject;
import static com.roberttisma.blog1.csv.bridges.impl.jackson.JacksonUtils.createBeanSequenceWriter;
import static com.roberttisma.blog1.csv.bridges.impl.jackson.JacksonUtils.createStringArraySequenceWriter;

/**
 * Important: CsvMapper cannot be configured to WRITE_NUMBERS_AS_STRINGS.
 * This feature exists in ObjectMapper however it does not in CsvMapper,
 * and as a result, this method will not add quotes around non-string types
 */
public class JacksonBeanCsvWriterBridge<T>  implements CsvWriterBridge<T> {

  private static final CsvMapper CSV_MAPPER = new CsvMapper();

  private final SequenceWriter beanSequenceWriter;
  private final SequenceWriter headerSequenceWriter;
  private final String[] header;

  public JacksonBeanCsvWriterBridge(SequenceWriter beanSequenceWriter,
      SequenceWriter headerSequenceWriter, String[] header) {
    this.beanSequenceWriter = beanSequenceWriter;
    this.headerSequenceWriter = headerSequenceWriter;
    this.header = header;
  }

  @Override
  public void write(T object) throws IOException {
    beanSequenceWriter.write(object);
  }

  @Override
  public void write(List<? extends T> objects) throws IOException {
    beanSequenceWriter.writeAll(objects);
  }

  @Override
  public void writeHeader() throws IOException {
    headerSequenceWriter.write(header);
  }

  @Override
  public void close() throws IOException {
    closeObject(beanSequenceWriter);
  }

  /**
   * Since Jackson cannot write a custom header, a special headerSequenceWriter is created that only writes arrays,
   * and the beanSequenceWriter writes the beans without writing the first header
   */
  public static <T> JacksonBeanCsvWriterBridge<T> createJacksonBeanCsvWriterBridge(CsvWriterBridgeContext<T> ctx,
      Class<T> clazz) throws IOException {
    Writer writer = ctx.buildWriter();
    SequenceWriter headerSequenceWriter = createStringArraySequenceWriter(
        ctx.getSeparator(),ctx.getQuote(), writer );
    SequenceWriter beanSequenceWriter = createBeanSequenceWriter(ctx.getSeparator(),ctx.getQuote(), ctx
        .getFieldNames(), clazz, writer);
    return new JacksonBeanCsvWriterBridge<T>(beanSequenceWriter, headerSequenceWriter, ctx.getAlternativeFieldNames());
  }


}
