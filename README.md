[//]: # (# Sports Centre)

[//]: # (This project explains how to use BDD using cucumber in springboot)

[//]: # ()
[//]: # (### Cucumber)

[//]: # ()
[//]: # (    * Cucumber is a high-level testing framework that supports behaviour driven development.)

[//]: # (    )
[//]: # (    * It runs automated acceptance tests on web applications)

[//]: # (    )
[//]: # (    * Cucumber is a tool that executes plain-text functional descriptions as automated tests. )

[//]: # (      The language that Cucumber understand is Gherkin.)

[//]: # (      )
[//]: # ()
[//]: # (       Flow :)

[//]: # (    )
[//]: # (        1. Describe behaviour.)

[//]: # (        2. Write Step definition.)

[//]: # (        3. Run and Fail.)

[//]: # (        4. Write code to make step pass.)

[//]: # (        5. Run and pass.)

[//]: # ()
[//]: # (### Feature File Introduction)

[//]: # (    )
[//]: # (    Feature Introduction Every .feature file conventionally consists of a single feature.)

[//]: # (    A line starting with the keyword Feature followed by free indented text starts a feature. )

[//]: # (    A feature usually contains a list of scenarios. scenarios together independent of your file and directory structure.)

[//]: # (    )
[//]: # (    Given -> What software will look like to user)

[//]: # (    )
[//]: # (    When  -> Things that the user will do)

[//]: # (    )
[//]: # (    Then  -> What the user should expect)

[//]: # (    )
[//]: # (### Cucumber Structure)

[//]: # ()
[//]: # (• Feature: Single file, ideally describing a single feature)

[//]: # ()
[//]: # (• Scenario: A test case)

[//]: # ()
[//]: # (• Given-When-Then: Test Preconditions, Execution and Postconditions)

[//]: # ()
[//]: # (• And: Additional test constructs)

[//]: # ()
[//]: # (    )
[//]: # ()
[//]: # (### Calculator Application)

[//]: # ()
[//]: # (    Here I take simple calculation application which has addition, subtraction, multiplication and divison.)

[//]: # (    )
[//]: # (    I made simple calcuation service in rest using springboot for add, sub, multi and divide.)

[//]: # (    )
[//]: # (        For example if you hit the url after the starting spring boot application the output will be like this.)

[//]: # (        )
[//]: # (              Rest URL : http://localhost:8081/calc/add/5/4)

[//]: # (              Output   : Addition of 5 + 4 is 9)

[//]: # (              )
[//]: # (        Similar for sub, multi and divide.)

[//]: # (        )
[//]: # (### Cucumber Configuration and Feature File)

[//]: # ()
[//]: # (* calculation.feature)

[//]: # ()
[//]: # ( ![cucum-feature.png]&#40;cucum-feature.png&#41;)

[//]: # (        )
[//]: # (        )
[//]: # (* CalculationIntegrationTest.java)

[//]: # (```)

[//]: # (@RunWith&#40;Cucumber.class&#41;)

[//]: # (@CucumberOptions&#40;features = "src/test/resources/features", plugin = {"pretty", "html:target/cucumber"}, glue = {"net.pmolinav.systemtests.cucumber.stepdefs"}&#41;)

[//]: # (public class CalculationIntegrationTest {)

[//]: # (})

[//]: # ()
[//]: # (```)

[//]: # ()
[//]: # (* CucumberConfig.java)

[//]: # (```)

[//]: # (@ContextConfiguration)

[//]: # (@SpringBootTest&#40;classes = SpringbootCucumberDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT&#41;)

[//]: # (public class CucumberConfig {)

[//]: # ()
[//]: # (    @Autowired)

[//]: # (    public TestRestTemplate testRestTemplate;)

[//]: # ()
[//]: # (    @LocalServerPort)

[//]: # (    public int port;)

[//]: # ()
[//]: # (    public String staticURL = "http://localhost:";)

[//]: # ()
[//]: # (    public HttpHeaders httpHeaders;)

[//]: # ()
[//]: # (    @Before)

[//]: # (    public void setUp&#40;&#41; throws Exception {)

[//]: # (        httpHeaders = new HttpHeaders&#40;&#41;;)

[//]: # (    })

[//]: # (})

[//]: # (```)

[//]: # ()
[//]: # (* CalculationStepdefs.java)

[//]: # ()
[//]: # (```)

[//]: # (public class CalculationStepdefs extends CucumberConfig {)

[//]: # ()
[//]: # (    private ResponseEntity<String> responseEntity;)

[//]: # ()
[//]: # (    private String result_add = "Addition of 5 + 4 is 9";)

[//]: # (    private String result_sub = "Subtraction of 6 - 4 is 2";)

[//]: # (    private String result_multi = "Multiple of 2 * 2 is 4";)

[//]: # (    private String result_divi = "Divide of 25 / 5 is 5";)

[//]: # ()
[//]: # (    @Given&#40;"^Create two numbers for addition$"&#41;)

[//]: # (    public void createTwoNumbersForAddition&#40;&#41; throws Throwable {)

[//]: # (        String URI="/calc";)

[//]: # (        getCompleteEndPoint&#40;URI&#41;;)

[//]: # (    })

[//]: # ()
[//]: # (    @And&#40;"^Add Number&#40;\\d+&#41; \"&#40;[^\"]*&#41;\" and Number&#40;\\d+&#41; \"&#40;[^\"]*&#41;\"$"&#41;)

[//]: # (    public void addNumberAndNumber&#40;int arg0, String arg1, int arg2, String arg3&#41; throws Throwable {)

[//]: # (        String URI = "/calc/add/" + arg1 +"/"+arg3;)

[//]: # (        responseEntity = testRestTemplate.getForEntity&#40;getCompleteEndPoint&#40;URI&#41;, String.class&#41;;)

[//]: # (        Assert.assertEquals&#40;"5", arg1&#41;;)

[//]: # (        Assert.assertEquals&#40;"4", arg3&#41;;)

[//]: # (    })

[//]: # ()
[//]: # (    @Then&#40;"^The output of addition is \"&#40;[^\"]*&#41;\"$"&#41;)

[//]: # (    public void theOutputOfAdditionIs&#40;String arg0&#41; throws Throwable {)

[//]: # (        Assert.assertEquals&#40;result_add, responseEntity.getBody&#40;&#41;&#41;;)

[//]: # (    })

[//]: # ()
[//]: # (    public String getCompleteEndPoint&#40;String URI&#41;{)

[//]: # (        System.out.println&#40;"Complete URL--->" + &#40;staticURL + port + URI&#41;&#41;;)

[//]: # (        return staticURL + port + URI;)

[//]: # (    })

[//]: # ()
[//]: # (    @Given&#40;"^Create two numbers for subtraction$"&#41;)

[//]: # (    public void createTwoNumbersForSubtraction&#40;&#41; throws Throwable {)

[//]: # (        String URI="/calc";)

[//]: # (        getCompleteEndPoint&#40;URI&#41;;)

[//]: # (    })

[//]: # ()
[//]: # (    @And&#40;"^Sub NumberA \"&#40;[^\"]*&#41;\" and NumberB \"&#40;[^\"]*&#41;\"$"&#41;)

[//]: # (    public void subNumberAAndNumberB&#40;String arg0, String arg1&#41; throws Throwable {)

[//]: # (        String URI = "/calc/sub/" + arg0 +"/"+arg1;)

[//]: # (        responseEntity = testRestTemplate.getForEntity&#40;getCompleteEndPoint&#40;URI&#41;, String.class&#41;;)

[//]: # (        Assert.assertEquals&#40;"6", arg0&#41;;)

[//]: # (        Assert.assertEquals&#40;"4", arg1&#41;;)

[//]: # (    })

[//]: # ()
[//]: # (    @Then&#40;"^The output of subtract is \"&#40;[^\"]*&#41;\"$"&#41;)

[//]: # (    public void theOutputOfSubtractIs&#40;String arg0&#41; throws Throwable {)

[//]: # (        Assert.assertEquals&#40;result_sub, responseEntity.getBody&#40;&#41;&#41;;)

[//]: # (    })

[//]: # ()
[//]: # (    @Given&#40;"^Create two numbers for Multiplication$"&#41;)

[//]: # (    public void createTwoNumbersForMultiplication&#40;&#41; throws Throwable {)

[//]: # (        String URI="/calc";)

[//]: # (        getCompleteEndPoint&#40;URI&#41;;)

[//]: # (    })

[//]: # ()
[//]: # (    @And&#40;"^Multi NumberA \"&#40;[^\"]*&#41;\" and NumberB \"&#40;[^\"]*&#41;\"$"&#41;)

[//]: # (    public void multiNumberAAndNumberB&#40;String arg0, String arg1&#41; throws Throwable {)

[//]: # (        String URI = "/calc/multiply/" + arg0 +"/"+arg1;)

[//]: # (        responseEntity = testRestTemplate.getForEntity&#40;getCompleteEndPoint&#40;URI&#41;, String.class&#41;;)

[//]: # (        Assert.assertEquals&#40;"2", arg0&#41;;)

[//]: # (        Assert.assertEquals&#40;"2", arg1&#41;;)

[//]: # (    })

[//]: # ()
[//]: # (    @Then&#40;"^The output of multiply is \"&#40;[^\"]*&#41;\"$"&#41;)

[//]: # (    public void theOutputOfMultiplyIs&#40;String arg0&#41; throws Throwable {)

[//]: # (        Assert.assertEquals&#40;result_multi, responseEntity.getBody&#40;&#41;&#41;;)

[//]: # (    })

[//]: # ()
[//]: # (    @Given&#40;"^Create two numbers for division$"&#41;)

[//]: # (    public void createTwoNumbersForDivision&#40;&#41; throws Throwable {)

[//]: # (        String URI="/calc";)

[//]: # (        getCompleteEndPoint&#40;URI&#41;;)

[//]: # (    })

[//]: # ()
[//]: # (    @And&#40;"^Divide NumberA \"&#40;[^\"]*&#41;\" and NumberB \"&#40;[^\"]*&#41;\"$"&#41;)

[//]: # (    public void divideNumberAAndNumberB&#40;String arg0, String arg1&#41; throws Throwable {)

[//]: # (        String URI = "/calc/divide/" + arg0 +"/"+arg1;)

[//]: # (        responseEntity = testRestTemplate.getForEntity&#40;getCompleteEndPoint&#40;URI&#41;, String.class&#41;;)

[//]: # (        Assert.assertEquals&#40;"25", arg0&#41;;)

[//]: # (        Assert.assertEquals&#40;"5", arg1&#41;;)

[//]: # (    })

[//]: # ()
[//]: # (    @Then&#40;"^The output of divison is \"&#40;[^\"]*&#41;\"$"&#41;)

[//]: # (    public void theOutputOfDivisonIs&#40;String arg0&#41; throws Throwable {)

[//]: # (        Assert.assertEquals&#40;result_divi, responseEntity.getBody&#40;&#41;&#41;;)

[//]: # (    })

[//]: # (})

[//]: # ()
[//]: # (```)

[//]: # ()
[//]: # (### Project Structure)

[//]: # ()
[//]: # (![cucumber-project-structure.png]&#40;cucumber-project-structure.png&#41;)

[//]: # ()
[//]: # ()
[//]: # (### Test Results &#40;After running the feature file or CalculationIntegrationTest.java&#41;)

[//]: # ()
[//]: # (![springboot-cucumber.png]&#40;springboot-cucumber.png&#41;)

[//]: # ()
[//]: # ()
[//]: # (### Happy Coding)
