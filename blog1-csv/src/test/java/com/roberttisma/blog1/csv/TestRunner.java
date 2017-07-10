package com.roberttisma.blog1.csv;

import com.roberttisma.blog1.csv.bridges.CsvWriterBridgeContext;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridgeContext.CsvWriterBridgeContextBuilder;
import com.roberttisma.blog1.model.Person;

import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

import static com.roberttisma.blog1.csv.PersonCsvWriterFactory.JACKSON_BEAN;
import static com.roberttisma.blog1.csv.PersonCsvWriterFactory.JACKSON_EXPLICIT;
import static com.roberttisma.blog1.csv.PersonCsvWriterFactory.JACKSON_LAMBDA;
import static com.roberttisma.blog1.csv.PersonCsvWriterFactory.OPEN_CSV_BEAN;
import static com.roberttisma.blog1.csv.PersonCsvWriterFactory.OPEN_CSV_EXPLICIT;
import static com.roberttisma.blog1.csv.PersonCsvWriterFactory.OPEN_CSV_LAMBDA;
import static com.roberttisma.blog1.csv.PersonCsvWriterFactory.SIMPLE_EXPLICIT;
import static com.roberttisma.blog1.csv.PersonCsvWriterFactory.SIMPLE_LAMBDA;
import static com.roberttisma.blog1.csv.PersonCsvWriterFactory.SUPER_CSV_BEAN;
import static com.roberttisma.blog1.csv.PersonCsvWriterFactory.SUPER_CSV_EXPLICIT;
import static com.roberttisma.blog1.csv.PersonCsvWriterFactory.SUPER_CSV_LAMBDA;
import static com.roberttisma.blog1.csv.Utils.checkState;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

public class TestRunner {

  private final CsvWriterBridgeContextBuilder<Person> ctxBuilder;
  private final List<? extends Person> data;

  /**
   * State
   */
  private EnumMap<PersonCsvWriterFactory, Path> pathMap = new EnumMap<>(PersonCsvWriterFactory.class);
  private EnumMap<PersonCsvWriterFactory, Boolean> processedMap = new EnumMap<>(PersonCsvWriterFactory.class);

  public TestRunner(CsvWriterBridgeContextBuilder<Person> ctxBuilder,
      List<? extends Person> data) {
    this.ctxBuilder = ctxBuilder;
    this.data = data;

    //Initialize
    stream(PersonCsvWriterFactory.values()).forEach(x -> processedMap.put(x, false));
  }


  public void runAll() throws IOException {
    run(PersonCsvWriterFactory.values());
  }

  public void run(PersonCsvWriterFactory ... factories) throws IOException {
    for (PersonCsvWriterFactory factory : factories) {
        CsvWriterBridgeContext<Person> tempCtx = ctxBuilder.build(factory.name());
        Path p = factory.writeToFile(tempCtx,data);
        pathMap.put(factory, p);
        processedMap.put(factory,true);
    }
  }

  public void runButIgnore(PersonCsvWriterFactory ... ignoreFactories) throws IOException {
    Set<PersonCsvWriterFactory> ignoreFactorySet  = stream(ignoreFactories).collect(toSet());
    PersonCsvWriterFactory[] factories = stream(PersonCsvWriterFactory.values())
        .filter(x -> !ignoreFactorySet.contains(x))
        .toArray(PersonCsvWriterFactory[]::new);
    run(factories);
  }

  private boolean isFactoryProcessed(PersonCsvWriterFactory factory){
    return processedMap.get(factory);
  }

  private Path get(PersonCsvWriterFactory factory) {
    checkState(isFactoryProcessed(factory), "Need to run the [%s] factory first", factory.name());
    return pathMap.get(factory);
  }

  public Path getSuperCsvBeanPath() {
    return get(SUPER_CSV_BEAN);
  }

  public Path getSuperCsvLambdaPath() {
    return get(SUPER_CSV_LAMBDA);
  }

  public Path getSuperCsvExplicitPath() {
    return get(SUPER_CSV_EXPLICIT);
  }

  public Path getOpenCsvBeanPath() {
    return get(OPEN_CSV_BEAN);
  }

  public Path getOpenCsvLambdaPath() {
    return get(OPEN_CSV_LAMBDA);
  }

  public Path getOpenCsvExplicitPath() {
    return get(OPEN_CSV_EXPLICIT);
  }

  public Path getSimpleLambdaPath() {
    return get(SIMPLE_LAMBDA);
  }

  public Path getSimpleExplicitPath() {
    return get(SIMPLE_EXPLICIT);
  }

  public Path getJacksonBeanPath() {
    return get(JACKSON_BEAN);
  }

  public Path getJacksonLambdaPath() {
    return get(JACKSON_LAMBDA);
  }

  public Path getJacksonExplicitPath() {
    return get(JACKSON_EXPLICIT);
  }

}
