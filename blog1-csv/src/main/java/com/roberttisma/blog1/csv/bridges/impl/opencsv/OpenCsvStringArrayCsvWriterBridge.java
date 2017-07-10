package com.roberttisma.blog1.csv.bridges.impl.opencsv;

import com.opencsv.CSVWriter;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridge;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridgeContext;

import java.io.IOException;

import static com.roberttisma.blog1.csv.Utils.closeObject;

public class OpenCsvStringArrayCsvWriterBridge implements CsvWriterBridge<String[]> {

  private static final boolean APPLY_QUOTES_TO_ALL = true;
  private final CSVWriter csvWriter;
  private final String[] header;

  public OpenCsvStringArrayCsvWriterBridge(CSVWriter csvWriter, String[] header) {
    this.csvWriter = csvWriter;
    this.header = header;
  }

  @Override
  public void write(String[] object) throws IOException {
    this.csvWriter.writeNext(object, APPLY_QUOTES_TO_ALL);

  }

  @Override
  public void writeHeader() throws IOException {
    this.csvWriter.writeNext(header, APPLY_QUOTES_TO_ALL);
  }

  @Override
  public void close() throws IOException {
    closeObject(csvWriter);
  }

  public static OpenCsvStringArrayCsvWriterBridge createOpenCsvStringArrayCsvWriterBridge(CsvWriterBridgeContext
      ctx){
    CSVWriter csvWriter = new CSVWriter(ctx.buildWriter(),ctx.getSeparator(), ctx.getQuote());
    return new OpenCsvStringArrayCsvWriterBridge(csvWriter,ctx.getAlternativeFieldNames());
  }

}
