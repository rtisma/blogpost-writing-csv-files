package com.roberttisma.blog1.csv;

import com.roberttisma.blog1.csv.bridges.CsvWriterBridge;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridgeContext;
import com.roberttisma.blog1.csv.bridges.impl.opencsv.OpenCsvPersonBean;
import com.roberttisma.blog1.csv.bridges.impl.supercsv.SuperCsvBeanCsvWriterBridge;
import com.roberttisma.blog1.csv.converter.Converter;
import com.roberttisma.blog1.csv.converter.impl.ExplicitPersonConverter;
import com.roberttisma.blog1.model.Person;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import static com.roberttisma.blog1.csv.StringArrayCsvWriterDecorator.createStringArrayCsvWriterDecorator;
import static com.roberttisma.blog1.csv.bridges.impl.jackson.JacksonBeanCsvWriterBridge.createJacksonBeanCsvWriterBridge;
import static com.roberttisma.blog1.csv.bridges.impl.jackson.JacksonStringArrayCsvWriterBridge.createJacksonStringArrayCsvWriterBridge;
import static com.roberttisma.blog1.csv.bridges.impl.opencsv.OpenCsvBeanCsvWriterBridge.createOpenCsvBeanCsvWriterBridge;
import static com.roberttisma.blog1.csv.bridges.impl.opencsv.OpenCsvStringArrayCsvWriterBridge.createOpenCsvStringArrayCsvWriterBridge;
import static com.roberttisma.blog1.csv.bridges.impl.simple.SimpleStringArrayCsvWriterBridge.createSimpleStringArrayCsvWriterBridge;
import static com.roberttisma.blog1.csv.bridges.impl.supercsv.SuperCsvStringArrayCsvWriterBridge.createSuperCsvStringArrayCsvWriterBridge;

/**
 * Factory enum that creates CsvWriterBridge<Person> implementations, using an enumeration
 * of configurations with method references to their factory methods.
 */
public enum PersonCsvWriterFactory {
  SUPER_CSV_EXPLICIT(PersonCsvWriterFactory::buildSuperCsvExplicitCsvWriter),
  SUPER_CSV_LAMBDA(PersonCsvWriterFactory::buildSuperCsvLambdaCsvWriter),
  SUPER_CSV_BEAN(SuperCsvBeanCsvWriterBridge::createSuperCsvBeanCsvWriterBridge),
  SIMPLE_EXPLICIT(PersonCsvWriterFactory::buildSimpleCsvExplicitCsvWriter),
  SIMPLE_LAMBDA(PersonCsvWriterFactory::buildSimpleCsvLambdaCsvWriter),
  OPEN_CSV_EXPLICIT(PersonCsvWriterFactory::buildOpenCsvExplicitCsvWriter),
  OPEN_CSV_LAMBDA(PersonCsvWriterFactory::buildOpenCsvLambdaCsvWriter),
  OPEN_CSV_BEAN(PersonCsvWriterFactory::buildOpenCsvBeanCsvWriter),
  JACKSON_EXPLICIT(PersonCsvWriterFactory::buildJacksonExplicitCsvWriter),
  JACKSON_LAMBDA(PersonCsvWriterFactory::buildJacksonLambdaCsvWriter),
  JACKSON_BEAN(PersonCsvWriterFactory::buildJacksonBeanCsvWriter);

  private static final Converter<Person, String[]> EXPLICIT_PERSON_CONVERTER = new ExplicitPersonConverter();

  private final Function<CsvWriterBridgeContext<Person>, CsvWriterBridge<Person>> buildFunction;

  PersonCsvWriterFactory(
      Function<CsvWriterBridgeContext<Person>, CsvWriterBridge<Person>> buildFunction) {
    this.buildFunction = buildFunction;
  }

  public Function<CsvWriterBridgeContext<Person>, CsvWriterBridge<Person>> getBuildFunction() {
    return buildFunction;
  }

  /**
   * Creates a CsvWriterBridge and writes a list of Persons to a CSV file configured by the CsvWriterBridgeContext
   * @throws IOException
   */
  public Path writeToFile(CsvWriterBridgeContext<Person> ctx, List<? extends Person> data) throws IOException {
    CsvWriterBridge<Person> personCsvWriterBridge = buildFunction.apply(ctx);
    personCsvWriterBridge.writeHeader();
    personCsvWriterBridge.write(data);
    personCsvWriterBridge.close();
    return ctx.getFilePath();
  }

  /**
   * BeanMethod based bridge factory methods
   */
  public static CsvWriterBridge<Person> buildJacksonBeanCsvWriter(CsvWriterBridgeContext<Person> ctx) {
    try{
      return createJacksonBeanCsvWriterBridge(ctx, Person.class);
    } catch (Throwable t){
      throw new RuntimeException(t);
    }
  }

  public static CsvWriterBridge<Person> buildOpenCsvBeanCsvWriter(CsvWriterBridgeContext<Person> ctx) {
    try {
      return createOpenCsvBeanCsvWriterBridge(ctx, OpenCsvPersonBean.class);
    } catch (Throwable t){
      throw new RuntimeException(t);
    }
  }

  /**
   *  LambdaMethod based bridge factory methods
   */
  public static CsvWriterBridge<Person> buildSuperCsvLambdaCsvWriter(CsvWriterBridgeContext<Person> ctx){
    CsvWriterBridge<String[]> csvWriterBridge = createSuperCsvStringArrayCsvWriterBridge(ctx);
    return createStringArrayCsvWriterDecorator(csvWriterBridge, ctx.buildLambdaConverter());
  }

  public static CsvWriterBridge<Person> buildOpenCsvLambdaCsvWriter(CsvWriterBridgeContext<Person> ctx){
    CsvWriterBridge<String[]> csvWriterBridge = createOpenCsvStringArrayCsvWriterBridge(ctx);
    return createStringArrayCsvWriterDecorator(csvWriterBridge, ctx.buildLambdaConverter());
  }

  public static CsvWriterBridge<Person> buildSimpleCsvLambdaCsvWriter(CsvWriterBridgeContext<Person> ctx){
    CsvWriterBridge<String[]> csvWriterBridge = createSimpleStringArrayCsvWriterBridge(ctx);
    return createStringArrayCsvWriterDecorator(csvWriterBridge, ctx.buildLambdaConverter());
  }

  public static CsvWriterBridge<Person> buildJacksonLambdaCsvWriter(CsvWriterBridgeContext<Person> ctx){
    try {
      CsvWriterBridge<String[]> csvWriterBridge = createJacksonStringArrayCsvWriterBridge(ctx);
      return createStringArrayCsvWriterDecorator(csvWriterBridge, ctx.buildLambdaConverter());
    } catch (Throwable t){
      throw new RuntimeException(t);
    }
  }

  /**
   *  ExplicitMethod based bridge factory methods
   */
  public static CsvWriterBridge<Person> buildSuperCsvExplicitCsvWriter(CsvWriterBridgeContext<Person> ctx){
    CsvWriterBridge<String[]> csvWriterBridge = createSuperCsvStringArrayCsvWriterBridge(ctx);
    return decorateWithExplicit(csvWriterBridge); //WARNING: Explicit converter is hardcoded!!!
  }

  public static CsvWriterBridge<Person> buildOpenCsvExplicitCsvWriter(CsvWriterBridgeContext<Person> ctx){
    CsvWriterBridge<String[]> csvWriterBridge = createOpenCsvStringArrayCsvWriterBridge(ctx);
    return decorateWithExplicit(csvWriterBridge); //WARNING: Explicit converter is hardcoded!!!
  }

  public static CsvWriterBridge<Person> buildJacksonExplicitCsvWriter(CsvWriterBridgeContext<Person> ctx){
    try {
      CsvWriterBridge<String[]> csvWriterBridge = createJacksonStringArrayCsvWriterBridge(ctx);
      return decorateWithExplicit(csvWriterBridge); //WARNING: Explicit converter is hardcoded!!!
    } catch (Throwable t){
      throw new RuntimeException(t);
    }
  }

  public static CsvWriterBridge<Person> buildSimpleCsvExplicitCsvWriter(CsvWriterBridgeContext<Person> ctx){
    CsvWriterBridge<String[]> csvWriterBridge = createSimpleStringArrayCsvWriterBridge(ctx);
    return decorateWithExplicit(csvWriterBridge); //WARNING: Explicit converter is hardcoded!!!
  }

  private static CsvWriterBridge<Person> decorateWithExplicit(CsvWriterBridge<String[]> csvWriterBridge){
    return createStringArrayCsvWriterDecorator(csvWriterBridge, EXPLICIT_PERSON_CONVERTER);
  }




}
