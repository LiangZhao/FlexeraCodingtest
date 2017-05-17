package au.com.flexera.codingtest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * PurchaseCalculator Tester.
 *
 * @author LiangZhao
 * @version 1.0
 * @since 16/05/2017
 */
public class PurchaseCalculatorTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }


    @Test
    public void testSplit() {
        PurchaseCalculator calculator = new PurchaseCalculator();
        String[] result = calculator.split("1,1,374,LAPTOP,Exported from System A", ",", 4);
        Assert.assertEquals(4, result.length);
        Assert.assertEquals("1", result[0]);
        Assert.assertEquals("1", result[1]);
        Assert.assertEquals("374", result[2]);
        Assert.assertEquals("LAPTOP", result[3]);

    }

    @Test
    public void testSplitWithFiveToken() {
        PurchaseCalculator calculator = new PurchaseCalculator();
        String[] result = calculator.split("2,1,374,DESKTOP,Exported from System A", ",", 5);
        Assert.assertEquals(5, result.length);
        Assert.assertEquals("2", result[0]);
        Assert.assertEquals("1", result[1]);
        Assert.assertEquals("374", result[2]);
        Assert.assertEquals("DESKTOP", result[3]);
        Assert.assertNotNull(result[4]);
    }

//    @Test
//    public void testSplitInstallEvent() {
//        PurchaseCalculator calculator = new PurchaseCalculator();
//        String[] result = calculator.splitInstallEvent("1,1091,606,DESKTOP,Exported from System A");
//        Assert.assertEquals(3, result.length);
//        Assert.assertEquals("1", result[0]);
//        Assert.assertEquals("1091,606", result[1]);
//        Assert.assertEquals("DESKTOP", result[2]);
//    }

    /**
     * With sample file Example1.csv
     * Only one copy of the application is required as the user has installed it on two computers, with one of them being a laptop.
     */
    @Test
    public void testCatulateMinimumCopyOne() throws Exception {
        PurchaseCalculator calculator = new PurchaseCalculator();
        int result = calculator.catulateMinimumCopy(this.getClass().getResource("/Example1.csv").getFile());
        Assert.assertEquals(1, result);
    }

    /**
     * With sample file Example2.csv
     * Three copies of the application are required as UserID 2 has installed the application on two computers,
     * but neither of them is a laptop and thus both computers require a purchase of the application.
     */
    @Test
    public void testCatulateMinimumCopyTwo() throws Exception {
        PurchaseCalculator calculator = new PurchaseCalculator();
        int result = calculator.catulateMinimumCopy(this.getClass().getResource("/Example2.csv").getFile());
        Assert.assertEquals(3, result);
    }

    /**
     * With sample file Example3.csv
     * Only two copies of the application are required as the data from the second and third rows are effectively
     * duplicates even though the ComputerType is lower case and the comment is different.
     */
    @Test
    public void testCatulateMinimumCopyThree() throws Exception {
        PurchaseCalculator calculator = new PurchaseCalculator();
        int result = calculator.catulateMinimumCopy(this.getClass().getResource("/Example3.csv").getFile());
        Assert.assertEquals(2, result);
    }

    /**
     * Example 1 with param application ID
     */
    @Test
    public void testCatulateMinimumCopyForApp() throws Exception {
        PurchaseCalculator calculator = new PurchaseCalculator();
        int result = calculator.catulateMinimumCopyForApp(this.getClass().getResource("/Example1.csv").getFile(), "374");
        Assert.assertEquals(1, result);
    }

    /**
     * Example 2 with param application ID and different data
     */
    @Test
    public void testCatulateMinimumCopyForAppTwo() throws Exception {
        PurchaseCalculator calculator = new PurchaseCalculator();
        int result = calculator.catulateMinimumCopyForApp(this.getClass().getResource("/Sample-test-2.csv").getFile(), "606");
        Assert.assertEquals(2, result);
    }

    /**
     * Example 2 with multi application id (merge)
     */
    @Test
    public void testCatulateMinimumCopyTwoM() throws Exception {
        PurchaseCalculator calculator = new PurchaseCalculator();
        int result = calculator.catulateMinimumCopy(this.getClass().getResource("/Sample-test-2m.csv").getFile());
        Assert.assertEquals(4, result);
    }

    /**
     * Example 2 with multi application id
     */
    @Test
    public void testCatulateMinimumCopyForAppTwoM() throws Exception {
        PurchaseCalculator calculator = new PurchaseCalculator();
        int result = calculator.catulateMinimumCopyForApp(this.getClass().getResource("/Sample-test-2m.csv").getFile(), "606");
        Assert.assertEquals(2, result);
        result = calculator.catulateMinimumCopyForApp(this.getClass().getResource("/Sample-test-2m.csv").getFile(), "607");
        Assert.assertEquals(1, result);
        result = calculator.catulateMinimumCopyForApp(this.getClass().getResource("/Sample-test-2m.csv").getFile(), "608");
        Assert.assertEquals(1, result);
    }

    /**
     * Multi data Example
     */
    @Test
    public void testCatulateMinimumCopyMore() throws Exception {
        PurchaseCalculator calculator = new PurchaseCalculator();
        int result = calculator.catulateMinimumCopy(this.getClass().getResource("/Sample-test-more.csv").getFile());
        Assert.assertEquals(11, result);
    }

    /**
     * Count all license numbers with small file
     */
    @Test
    public void testCatulateMinimumCopy() throws Exception {
        PurchaseCalculator calculator = new PurchaseCalculator();
        int result = calculator.catulateMinimumCopy(this.getClass().getResource("/sample-small.csv").getFile());
        Assert.assertEquals(198978, result);
    }

    /**
     *  calculates the minimum of copies of the application with ID 374 a company must purchase
     */
    @Test
    public void testCatulateMinimumCopy_AppID374() throws Exception {
        PurchaseCalculator calculator = new PurchaseCalculator();
        int result = calculator.catulateMinimumCopyForApp(this.getClass().getResource("/sample-small.csv").getFile(),"374");
        Assert.assertEquals(190, result);
    }

    /**
     *  calculates the minimum of copies of the application with ID 374 a company must purchase (Large data)
     */
    @Test
    public void testCatulateMinimumCopy_LargeData_AppID374() throws Exception {
        PurchaseCalculator calculator = new PurchaseCalculator();
        int result = calculator.catulateMinimumCopyForApp(this.getClass().getResource("/sample-large.csv").getFile(),"374");
        Assert.assertEquals(15336, result);
    }
} 
