package com.roberttisma.blog1.csv;

import com.roberttisma.blog1.csv.bridges.CsvWriterBridgeContext;
import com.roberttisma.blog1.csv.bridges.CsvWriterBridgeContext.CsvWriterBridgeContextBuilder;
import com.roberttisma.blog1.csv.bridges.impl.opencsv.OpenCsvPersonBean;
import com.roberttisma.blog1.csv.schema.PersonSchema;
import com.roberttisma.blog1.csv.schema.SchemaField;
import com.roberttisma.blog1.model.Person;
import com.roberttisma.blog1.model.PersonBean;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.roberttisma.blog1.csv.Config.COMMA;
import static com.roberttisma.blog1.csv.Config.EMPTY_CHARACTER;
import static com.roberttisma.blog1.csv.Config.OUTPUT_CSV_DIRPATH;
import static com.roberttisma.blog1.csv.PersonCsvWriterFactory.OPEN_CSV_BEAN;
import static com.roberttisma.blog1.csv.Utils.setsHaveDifference;
import static com.roberttisma.blog1.model.PersonBean.createPersonBean;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CsvConversionTest {

  /**
   * Constants
   */
  private static final String[] HEADERS_ARRAY = PersonSchema.getFieldNames();
  private static final Set<String> HEADERS_SET = stream(HEADERS_ARRAY).collect(toSet());
  private final static Character TAB = '\t';
  private final static Character DOUBLE_QUOTE = '"';
  private final static Character SINGLE_QUOTE = '\'';
  private static final boolean USE_DISK = true;
  private static final Path FIXTURES_DIR = Paths.get("src/test/resources/fixtures");

  private static final Path GOLDEN__COMMA_SEP_NO_QUOTES_ORDER_SHIFTED_BY_2_PATH
      = FIXTURES_DIR.resolve("reference.commaSepNoQuotesOrderShiftedBy2.csv");
  private static final Path GOLDEN__COMMA_SEP_NO_QUOTES_REGULAR_ORDER_PATH
      = FIXTURES_DIR.resolve("reference.commaSepNoQuotesRegularOrder.csv");
  private static final Path GOLDEN__COMMA_SEP_NO_QUOTES_1_LESS_HEADER_PATH
      = FIXTURES_DIR.resolve("reference.commaSepNoQuotes1LessHeader.csv");
  private static final Path GOLDEN__COMMA_SEP_WITH_DOUBLE_QUOTE_ORDER_SHIFTED_BY_2_PATH
      = FIXTURES_DIR.resolve("reference.commaSepWithDoubleQuoteOrderShiftedBy2.csv");
  private static final Path GOLDEN__TAB_SEP_WITH_DOUBLE_QUOTES_REGULAR_ORDER_PATH
      = FIXTURES_DIR.resolve("reference.tabSepWithDoubleQuotesRegularOrder.csv");
  private static final Path GOLDEN__TAB_SEP_WITH_SINGLE_QUOTE_ORDER_SHIFTED_BY_2_PATH
      = FIXTURES_DIR.resolve("reference.tabSepWithSingleQuoteOrderShiftedBy2.csv");
  private static final Path GOLDEN__TAB_SEP_WITH_SINGLE_QUOTES_REGULAR_ORDER_PATH
      = FIXTURES_DIR.resolve("reference.tabSepWithSingleQuotesRegularOrder.csv");


  @Rule public TestName testName = new TestName();

  /**
   * Asserts that the member field names of the class OpenCsvPersonBean match the names used in the
   * PersonSchema enum
   */
  @Test
  public void testOpenCsvPersonBeanMapping(){
    assertPersonClassFields(OpenCsvPersonBean.class);
  }

  /**
   * Asserts that the member field names of the class PersonBean match the names used in the
   * PersonSchema enum
   */
  @Test
  public void testModelPersonBeanMapping(){
    assertPersonClassFields(PersonBean.class);
  }

  @Test
  public void testPersonConversion(){
    //assert equalsPerson works correctly classifying equal objects
    PersonBean personBean = generatePersonBean().get(0);
    PersonBean personBean_clone = generatePersonBean().get(0);
    assertTrue("equalsPerson method reported objects to be different when they are the same",
        personBean.equalsPerson(personBean_clone));

    //assert equalsPerson works correctly classifying NOT equal objects
    PersonBean personBean_modified = generatePersonBean().get(0);
    personBean_modified.setAge(personBean_modified.getAge()+100);
    assertFalse("equalsPerson method reported objects to be same when they are the different",
        personBean.equalsPerson(personBean_modified));

    //assert PersonBean is correctly converted to OpenCsvPersonBean
    OpenCsvPersonBean openCsvPersonBean = OpenCsvPersonBean.convert(personBean);
    assertTrue("OpenCsvPersonBean and PersonBean are not equal", personBean.equalsPerson(openCsvPersonBean));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testCommaSepNoQuotes1LessHeader() throws IOException {
    TestRunner runner = runTest(COMMA, EMPTY_CHARACTER, 0, 1, OPEN_CSV_BEAN);

    // Assert all are the same
    assertFileContentsAreTheSame(
        GOLDEN__COMMA_SEP_NO_QUOTES_1_LESS_HEADER_PATH,
        runner.getJacksonLambdaPath(),
        runner.getOpenCsvLambdaPath(),
        runner.getSimpleLambdaPath(),
        runner.getSuperCsvBeanPath(),
        runner.getSuperCsvLambdaPath()
    );

    // Assert all Explicit configurations are the same
    assertFileContentsAreTheSame(
        runner.getJacksonExplicitPath(),
        runner.getOpenCsvExplicitPath(),
        runner.getSimpleExplicitPath(),
        runner.getSuperCsvExplicitPath()
    );

    // Assert that all explicit and JACKSON_BEAN configurations are different
    // and fail to match the golden reference
    assertFileContentsAreAllDifferent(
        GOLDEN__COMMA_SEP_NO_QUOTES_1_LESS_HEADER_PATH,
        runner.getJacksonBeanPath(),
        runner.getJacksonExplicitPath()
    );
  }


  @Test
  public void testCommaSepNoQuotesRegularOrder() throws IOException {
    TestRunner runner = runTest(COMMA, EMPTY_CHARACTER, 0, -1);
    assertFileContentsAreTheSame(
        GOLDEN__COMMA_SEP_NO_QUOTES_REGULAR_ORDER_PATH,
        runner.getSuperCsvLambdaPath(),
        runner.getSuperCsvBeanPath(),
        runner.getSuperCsvExplicitPath(),

        runner.getOpenCsvLambdaPath(),
        runner.getOpenCsvExplicitPath(),
        runner.getOpenCsvBeanPath(),

        runner.getSimpleLambdaPath(),
        runner.getSimpleExplicitPath(),

        runner.getJacksonBeanPath(),
        runner.getJacksonExplicitPath(),
        runner.getJacksonLambdaPath()
    );

  }

  @Test
  public void testTabSepWithSingleQuotesRegularOrder() throws IOException {
    TestRunner runner = runTest(TAB, SINGLE_QUOTE, 0, -1);
    //Assert that all the above methods produce the same output file
    assertFileContentsAreTheSame(
        GOLDEN__TAB_SEP_WITH_SINGLE_QUOTES_REGULAR_ORDER_PATH,
        runner.getSuperCsvLambdaPath(),
        runner.getSuperCsvBeanPath(),
        runner.getSuperCsvExplicitPath(),
        runner.getOpenCsvLambdaPath(),
        runner.getOpenCsvExplicitPath(),
        runner.getOpenCsvBeanPath(),
        runner.getSimpleLambdaPath(),
        runner.getSimpleExplicitPath()
    );
    //Assert that just the jacksonBean, jacksonExplicit and opencsvBean methods produce DIFFERENT content.
    // JacksonBean does not wrap non-strings with quotes, and uses double quotes instead of single quotes
    // JacksonExplicit uses double quotes instead of single quotes
    assertFileContentsAreAllDifferent(
        GOLDEN__TAB_SEP_WITH_SINGLE_QUOTES_REGULAR_ORDER_PATH,
        runner.getJacksonBeanPath() ,
        runner.getJacksonExplicitPath());

    // Assert that the Jackson Lambda and Explicit methods match, to indicated they share the same error, which is
    // using DOUBLE QUOTES as the quote string instead of SINGLE QUOTES.
    assertFileContentsAreTheSame(
        runner.getJacksonLambdaPath(),
        runner.getJacksonExplicitPath());
  }

  @Test
  public void testTabSepWithDoubleQuotesRegularOrder() throws IOException {
    TestRunner runner = runTest(TAB, DOUBLE_QUOTE, 0, -1);

    //Assert that all the above methods produce the same output file
    assertFileContentsAreTheSame(
        GOLDEN__TAB_SEP_WITH_DOUBLE_QUOTES_REGULAR_ORDER_PATH,
        runner.getSuperCsvLambdaPath(),
        runner.getSuperCsvBeanPath(),
        runner.getSuperCsvExplicitPath(),

        runner.getOpenCsvLambdaPath(),
        runner.getOpenCsvExplicitPath(),
        runner.getOpenCsvBeanPath(),

        runner.getJacksonLambdaPath(),
        runner.getJacksonExplicitPath(),

        runner.getSimpleLambdaPath(),
        runner.getSimpleExplicitPath()
    );

    //Assert that only the JacksonBean configuration produces a different output than the others
    assertFileContentsAreAllDifferent(
        GOLDEN__TAB_SEP_WITH_DOUBLE_QUOTES_REGULAR_ORDER_PATH,
        runner.getJacksonBeanPath());
  }

  @Test
  public void testTabSepWithSingleQuoteOrderShiftedBy2() throws IOException {
    TestRunner runner = runTest(TAB, SINGLE_QUOTE, -2, -1);
    //Assert the following methods produce the same output file, which is correct
    assertFileContentsAreTheSame(
        GOLDEN__TAB_SEP_WITH_SINGLE_QUOTE_ORDER_SHIFTED_BY_2_PATH,
        runner.getSuperCsvBeanPath(),
        runner.getSuperCsvLambdaPath(),
        runner.getOpenCsvBeanPath(),
        runner.getOpenCsvLambdaPath(),
        runner.getSimpleLambdaPath());

    //Assert that due to the order change, the explicit methods do not create the correct
    // csv file like the lambda method does. the lmabda method was proven correct in the
    // previous assertion
    assertFileContentsAreAllDifferent(
        GOLDEN__TAB_SEP_WITH_SINGLE_QUOTE_ORDER_SHIFTED_BY_2_PATH,
        runner.getOpenCsvExplicitPath(),
        runner.getJacksonExplicitPath(),
        runner.getJacksonBeanPath(),
        runner.getJacksonLambdaPath());

    //Asserts that the explicit methods all had the same incorrect output, except for jacksonExplicit
    assertFileContentsAreTheSame(
        runner.getSimpleExplicitPath(),
        runner.getSuperCsvExplicitPath(),
        runner.getOpenCsvExplicitPath());
  }

  @Test
  public void testCommaSepWithDoubleQuoteOrderShiftedBy2() throws IOException {
    TestRunner runner = runTest(COMMA, DOUBLE_QUOTE, -2, -1);
    assertFileContentsAreTheSame(
        GOLDEN__COMMA_SEP_WITH_DOUBLE_QUOTE_ORDER_SHIFTED_BY_2_PATH,
        runner.getJacksonLambdaPath(),
        runner.getOpenCsvBeanPath(),
        runner.getOpenCsvLambdaPath(),
        runner.getSimpleLambdaPath(),
        runner.getSuperCsvBeanPath(),
        runner.getSuperCsvLambdaPath()
    );

    assertFileContentsAreAllDifferent(
        GOLDEN__COMMA_SEP_WITH_DOUBLE_QUOTE_ORDER_SHIFTED_BY_2_PATH,
        runner.getJacksonExplicitPath(),
        runner.getJacksonBeanPath());

    assertFileContentsAreTheSame(
        runner.getJacksonExplicitPath(),
        runner.getOpenCsvExplicitPath(),
        runner.getSuperCsvExplicitPath(),
        runner.getSimpleExplicitPath()
    );
  }

  @Test
  public void testCommaSepNoQuoteOrderShiftedBy2() throws IOException {
    TestRunner runner = runTest(COMMA, EMPTY_CHARACTER, -2, -1);
    assertFileContentsAreTheSame(
        GOLDEN__COMMA_SEP_NO_QUOTES_ORDER_SHIFTED_BY_2_PATH,
        runner.getJacksonBeanPath(),
        runner.getJacksonLambdaPath(),
        runner.getOpenCsvBeanPath(),
        runner.getOpenCsvLambdaPath(),
        runner.getSuperCsvBeanPath(),
        runner.getSuperCsvLambdaPath(),
        runner.getSimpleLambdaPath()
    );

    // Asserts that all *_EXPLICIT configurations have the same output
    assertFileContentsAreTheSame(
        runner.getJacksonExplicitPath(),
        runner.getOpenCsvExplicitPath(),
        runner.getSuperCsvExplicitPath(),
        runner.getSimpleExplicitPath()
    );

    // Effectively asserts ALL *_EXPLICIT configurations do not match the golden reference
    assertFileContentsAreAllDifferent(
        GOLDEN__COMMA_SEP_NO_QUOTES_ORDER_SHIFTED_BY_2_PATH,
        runner.getJacksonExplicitPath()
    );
  }

  @SuppressWarnings("unchecked")
  private TestRunner runTest(Character separator, Character quote, int shift,
      int skipFieldIdx, // if < 0, then disabled, if > 0, then skip the field
      PersonCsvWriterFactory ... ignoreFactories ) throws IOException {
    List<PersonBean> data = generatePersonBean();

    // Remove fields based on skipFieldIdx
    SchemaField<Person>[] schema = skipFieldIdx < 0 ? PersonSchema.getSchema() :
        (SchemaField<Person>[]) stream(PersonSchema.getSchema())
            .skip(skipFieldIdx)
            .toArray(SchemaField[]::new);

    // Reorder schema by shifting
    SchemaField<Person>[]  reorderedSchema = shiftCopy(schema, shift);

    // Create Context builder
    CsvWriterBridgeContextBuilder<Person> builder =
        CsvWriterBridgeContext.<Person>builder()
            .setOutputDirPath(getCurrentTestOutputDirPath())
            .setUseDisk(USE_DISK)
            .setQuote(quote)
            .setSeparator(separator)
            .setSchema(reorderedSchema);

    // Run tests on all configurations except for the ones to be ignored
    TestRunner runner = new TestRunner(builder,data);
    runner.runButIgnore(ignoreFactories);
    return runner;
  }

  private Path getCurrentTestOutputDirPath(){
    return OUTPUT_CSV_DIRPATH.resolve(testName.getMethodName());
  }

  /**
   * Shifts the array but creates new array
   */
  private static <T> T[] shiftCopy(final T[] original, final int num){
    int length = original.length;
    int shiftNum = num % length; //wrap value
    int bufSize = Math.abs(shiftNum);
    T[] out = Arrays.copyOf(original, length);
    for(int i = 0; i < length ; i++){
      int pos = (length + i - shiftNum) % length;
      out[i] = original[pos];
    }
    return out;
  }


  /**
   * Asserts all files have the exact same content. If at least one file contents DIFFER from the first,
   * the assertion fails
   */
  private static void assertFileContentsAreTheSame(Path firstPath, Path...otherPaths) throws FileNotFoundException {
    final String firstPathContent = Utils.readFileToString(firstPath);
    assertTrue("There must be atleast 1 other path",
        otherPaths.length>0);
    for(final Path otherPath : otherPaths){
      String otherPathContent = Utils.readFileToString(otherPath);
      assertTrue(format("The file [%s] does not match the first file [%s]",
          otherPath.toString(), firstPath.toString()),
          firstPathContent.equals(otherPathContent));
    }
  }

  /**
   * Asserts all files have the DIFFERENT content. If at least one files MATCH the contents of any of the other
   * file, the assertion fails.
   */
  private static void assertFileContentsAreAllDifferent(Path firstPath, Path...otherPaths) throws
      FileNotFoundException {
    Set<String> contentSet = new HashSet<>();
    final int totalPaths = otherPaths.length + 1;
    final String firstPathContent = Utils.readFileToString(firstPath);
    contentSet.add(firstPathContent);
    assertTrue("There must be atleast 1 other path",
        otherPaths.length>0);

    for(final Path otherPath : otherPaths){
      String otherPathContent = Utils.readFileToString(otherPath);
      contentSet.add(otherPathContent);
    }

    // If they are all different, then the contentSet size should be the same size as the number of files
    assertTrue(format("The input files are NOT all different. There are %s files that match the contents of another "
            + "file",
        totalPaths - contentSet.size()),
        contentSet.size() == totalPaths);
  }

  private static void assertPersonClassFields(Class<? extends Person> clazz){
    Set<String> personBeanFieldNames = stream(clazz.getDeclaredFields())
        .map(Field::getName)
        .collect(toSet());
    assertFalse(format("The PersonSchema enum contains field names that DO NOT MATCH the member field "
            + "names in the %s class\n\t%s: %s\n\tPersonSchema: %s",
        clazz.getSimpleName(), clazz.getSimpleName(),
        personBeanFieldNames, HEADERS_SET),
        setsHaveDifference(personBeanFieldNames, HEADERS_SET));
  }

  /**
   * Generates PersonBean for testing
   */
  public static List<PersonBean> generatePersonBean(){
    List<PersonBean> list = new ArrayList<>();
    list.add(createPersonBean(1, "Denis", "Ritchie", 70));
    list.add(createPersonBean(2, "Frank", "Rosenblatt", 43));
    list.add(createPersonBean(3, "Alex", "Murphy", 37));
    return list;
  }
}
