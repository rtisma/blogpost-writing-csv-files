package com.roberttisma.blog1.csv.bridges.impl.supercsv;

import com.roberttisma.blog1.csv.Utils;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.AlwaysQuoteMode;
import org.supercsv.quote.NormalQuoteMode;

public class SuperCsvUtils {

  private static final String NEWLINE = "\n";

  public static CsvPreference createCsvPreference(char separator, char quote){
    String quoteChar = Utils.resolveChar(quote);
    return new CsvPreference.Builder(quote, separator, NEWLINE)
        .useQuoteMode(quoteChar.isEmpty() ? new NormalQuoteMode() : new AlwaysQuoteMode())
        .build();
  }

}
