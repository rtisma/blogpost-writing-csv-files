package com.roberttisma.blog1.csv.bridges.impl.supercsv;

import com.roberttisma.blog1.csv.bridges.CsvWriterBridgeContext;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridge;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.Writer;

import static com.roberttisma.blog1.csv.Utils.closeObject;
import static com.roberttisma.blog1.csv.bridges.impl.supercsv.SuperCsvUtils.createCsvPreference;

public class SuperCsvStringArrayCsvWriterBridge implements CsvWriterBridge<String[]> {

  private final ICsvListWriter listWriter;
  private final String[] header;

  public SuperCsvStringArrayCsvWriterBridge(ICsvListWriter listWriter, String[] header) {
    this.listWriter = listWriter;
    this.header = header;
  }

  @Override
  public void write(String[] object) throws IOException {
    listWriter.write(object);
  }

  @Override
  public void close() throws IOException {
    closeObject(listWriter);
  }

  @Override
  public void writeHeader() throws IOException {
    listWriter.writeHeader(header);
  }

  public static SuperCsvStringArrayCsvWriterBridge createSuperCsvStringArrayCsvWriterBridge(
      Writer writer, String[] header, Character separator, Character quote){
    CsvPreference preference = createCsvPreference(separator, quote);
    CsvListWriter listWriter = new CsvListWriter(writer, preference);
    return new SuperCsvStringArrayCsvWriterBridge(listWriter,header);
  }

  public static SuperCsvStringArrayCsvWriterBridge createSuperCsvStringArrayCsvWriterBridge(CsvWriterBridgeContext
      ctx) {
    return createSuperCsvStringArrayCsvWriterBridge(ctx.buildWriter(), ctx.getAlternativeFieldNames(),
        ctx.getSeparator(), ctx.getQuote());
  }

}
