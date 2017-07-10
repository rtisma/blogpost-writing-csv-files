package com.roberttisma.blog1.csv.bridges.impl.supercsv;

import com.roberttisma.blog1.csv.bridges.CsvWriterBridgeContext;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridge;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;

import static com.roberttisma.blog1.csv.Utils.checkState;
import static com.roberttisma.blog1.csv.Utils.closeObject;
import static com.roberttisma.blog1.csv.bridges.impl.supercsv.SuperCsvUtils.createCsvPreference;

public class SuperCsvBeanCsvWriterBridge<T> implements CsvWriterBridge<T> {

  private final ICsvBeanWriter beanWriter;
  private final String[] beanFieldNames;
  private final String[] header;

  public SuperCsvBeanCsvWriterBridge(ICsvBeanWriter beanWriter, String[] beanFieldNames,
      String[] header) {
    this.beanWriter = beanWriter;
    this.beanFieldNames = beanFieldNames;
    this.header = header;
    checkState(beanFieldNames.length == header.length,
        "The beanFieldNames length (%s) and the "
        + "outputFieldNames length (%s) are not equal", beanFieldNames.length, header.length  );
  }


  @Override
  public void write(T object) throws IOException {
    beanWriter.write(object, beanFieldNames);
  }

  @Override
  public void writeHeader() throws IOException {
    //NOTE: Unlike the other bean examples, SuperCsv allows you to alias
    // the columnNames with something different than the object's member field names
    beanWriter.writeHeader(header);
  }

  @Override
  public void close() throws IOException {
    closeObject(beanWriter);
  }

  public static <T> SuperCsvBeanCsvWriterBridge<T> createSuperCsvBeanCsvWriterBridge(CsvWriterBridgeContext ctx) {
    CsvPreference csvPreference = createCsvPreference(ctx.getSeparator(),ctx.getQuote());
    CsvBeanWriter beanWriter = new CsvBeanWriter(ctx.buildWriter(), csvPreference);
    return new SuperCsvBeanCsvWriterBridge<T>(beanWriter,ctx.getFieldNames(), ctx.getAlternativeFieldNames());
  }

}
