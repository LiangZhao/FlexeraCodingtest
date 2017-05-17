package au.com.flexera.codingtest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * LicenseCounter Tester.
 *
 * @author LiangZhao
 * @version 1.0
 * @since 16/05/2017
 */
public class LicenseCounterTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: countLaptop(String deviceId)
     */
    @Test
    public void testCountLaptop() throws Exception {
        LicenseCounter counter = new LicenseCounter();
        counter.countLaptop("1");
        Assert.assertEquals(1, counter.getLaptopCount());

        counter.countLaptop("2");
        Assert.assertEquals(2, counter.getLaptopCount());

        counter.countLaptop("2");
        Assert.assertEquals(2, counter.getLaptopCount());
    }

    /**
     * Method: countDesktop(String deviceId)
     */
    @Test
    public void testCountDesktop() throws Exception {
        LicenseCounter counter = new LicenseCounter();
        counter.countDesktop("1");
        Assert.assertEquals(1, counter.getDesktopCount());

        counter.countDesktop("2");
        Assert.assertEquals(2, counter.getDesktopCount());

        counter.countDesktop("2");
        Assert.assertEquals(2, counter.getDesktopCount());
    }

    /**
     * Method: catulateLicenseCount()
     */
    @Test
    public void testCatulateLicenseCount() throws Exception {
        LicenseCounter counter = new LicenseCounter();
        counter.countDesktop("1");
        counter.countLaptop("2");
        Assert.assertEquals(1, counter.catulateLicenseCount());
    }

    /**
     * Method: merge(LicenseCounter counter)
     */
    @Test
    public void testMerge() throws Exception {
        LicenseCounter counter = new LicenseCounter();
        counter.countDesktop("1");
        Assert.assertEquals(1, counter.getDesktopCount());

        counter.countDesktop("2");
        Assert.assertEquals(2, counter.getDesktopCount());

        counter.countDesktop("2");
        Assert.assertEquals(2, counter.getDesktopCount());

        LicenseCounter counter1 = new LicenseCounter();
        counter1.countDesktop("1");
        Assert.assertEquals(1, counter1.getDesktopCount());

        counter1.countDesktop("5");
        Assert.assertEquals(2, counter1.getDesktopCount());

        counter1.countLaptop("4");
        Assert.assertEquals(2, counter1.getDesktopCount());

        LicenseCounter counter2 = counter.merge(counter1);
        Assert.assertEquals(3, counter2.getDesktopCount());
        Assert.assertEquals(1, counter2.getLaptopCount());
        Assert.assertEquals(3, counter2.catulateLicenseCount());
    }



} 
