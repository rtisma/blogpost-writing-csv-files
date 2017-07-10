package com.roberttisma.blog1.csv.bridges;

import com.roberttisma.blog1.csv.converter.Converter;
import com.roberttisma.blog1.csv.schema.SchemaField;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;

import static com.roberttisma.blog1.csv.converter.impl.LambdaConverter.createLambdaConverter;
import static java.nio.file.Files.createDirectories;
import static java.util.Arrays.stream;

/**
 * Immutable context that contains configuration information for CsvWriterBridge implementations
 */
public class CsvWriterBridgeContext<T>  {

  private final Path outputDirPath;
  private final SchemaField<T>[] schema;
  private final boolean useDisk;
  private final Character separator;
  private final Character quote;
  private final String name;

  private CsvWriterBridgeContext(Path outputDirPath, SchemaField<T>[] schema,
      boolean useDisk, Character separator, Character quote, String name) {
    this.outputDirPath = outputDirPath;
    this.schema = schema;
    this.useDisk = useDisk;
    this.separator = separator;
    this.quote = quote;
    this.name = name;
  }

  public static <T> CsvWriterBridgeContextBuilder<T> builder(){
    return new CsvWriterBridgeContextBuilder<>();
  }

  public Path getOutputDirPath() {
    return outputDirPath;
  }

  public String getName() {
    return name;
  }

  public SchemaField<T>[] getSchema() {
    return schema;
  }

  public boolean isUseDisk() {
    return useDisk;
  }

  public Character getSeparator() {
    return separator;
  }

  public Character getQuote() {
    return quote;
  }

  /**
   * constructs converter using the schema
   */
  public Converter<T, String[]> buildLambdaConverter() {
    return createLambdaConverter(getSchema());
  }

  public Path getFilePath() throws IOException {
    createDirectories(getOutputDirPath());
    return getOutputDirPath().resolve(getName()+".output.csv");
  }

  /**
   * Constructs writer object for external csv writing libraries
   */
  public Writer buildWriter ()  {
    try {
      if (isUseDisk()){
        return new OutputStreamWriter(buildOutputStream());
      } else {
        return new Writer(){
          @Override public void write(char[] cbuf, int off, int len) throws IOException { }
          @Override public void flush() throws IOException { }
          @Override public void close() throws IOException { }
        };

      }
    } catch (IOException e){
      throw new RuntimeException(e);
    }
  }

  public String[] getFieldNames(){
    return stream(getSchema())
        .map(SchemaField::getFieldName)
        .toArray(String[]::new);
  }

  public String[] getAlternativeFieldNames(){
    return stream(getSchema())
        .map(SchemaField::getAlternativeFieldName)
        .toArray(String[]::new);
  }


  private FileOutputStream buildFileOutputStream() throws IOException {
    return new FileOutputStream(getFilePath().toFile());
  }

  private ByteArrayOutputStream buildByteArrayOutputStream() {
    return new ByteArrayOutputStream();
  }

  private OutputStream buildOutputStream() throws IOException {
    return isUseDisk() ? buildFileOutputStream() : buildByteArrayOutputStream();
  }

  public static <T> CsvWriterBridgeContext<T> createCsvWriterBridgeContext(Path outputDirPath,
      SchemaField<T>[] schemaFields, boolean useDisk,
      Character separator, Character quote, String name) {
    return new CsvWriterBridgeContext(outputDirPath, schemaFields, useDisk, separator, quote, name);
  }

  /**
   * Mutable builder class that is used to build CsvWriterBridgeContexts with
   * specific names
   */
  public static class CsvWriterBridgeContextBuilder<T> {

    private Path outputDirPath;
    private SchemaField<T>[] schema;
    private boolean useDisk;
    private Character separator;
    private Character quote;

    public CsvWriterBridgeContextBuilder<T> setOutputDirPath(Path outputDirPath) {
      this.outputDirPath = outputDirPath;
      return this;
    }

    public CsvWriterBridgeContextBuilder<T> setSchema(SchemaField<T>[] schema) {
      this.schema = schema;
      return this;
    }


    public CsvWriterBridgeContextBuilder<T> setUseDisk(boolean useDisk) {
      this.useDisk = useDisk;
      return this;
    }

    public CsvWriterBridgeContextBuilder<T> setSeparator(Character separator) {
      this.separator = separator;
      return this;
    }

    public CsvWriterBridgeContextBuilder<T> setQuote(Character quote) {
      this.quote = quote;
      return this;
    }

    /**
     * Build a immutable CsvWriterBridgeContext with the provided name
     */
    public CsvWriterBridgeContext<T> build(String name){
      return createCsvWriterBridgeContext(outputDirPath, schema, useDisk, separator, quote, name);
    }

  }

}
