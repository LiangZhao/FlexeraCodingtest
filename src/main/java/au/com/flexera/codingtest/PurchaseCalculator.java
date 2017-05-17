package au.com.flexera.codingtest;

import java.lang.reflect.Array;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * PurchaseCalculator
 *
 * @author LiangZhao
 * @version 1.0
 * @since 13/05/2017
 *
 */
public class PurchaseCalculator {
    private static final int OFFSET_DEVICE_ID = 0;
    private static final int OFFSET_INSTALL_ID = 1;
    private static final int OFFSET_TYPE = 2;

    /**
     * Using the thread pool, the purpose is try to reduce the amount of data within the map.
     * ( :-( doesn't make it any better, I think I need to think about it )
     * @param file
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public int catulateMinimumCopy(String file) throws  IOException , InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(4);
        List<Map<String, LicenseCounter>> counters = new ArrayList<>();

        File csvData = new File(file);
        LineIterator it = FileUtils.lineIterator(csvData, "UTF-8");
        it.nextLine();//skip header
        int i = 0;
        List<String> events = new ArrayList<>(10000);
        while (it.hasNext()) {
            i++;
            events.add(it.nextLine());
            if (i % 100000 == 0){
                service.submit(new CatulateTask(events, counters));
                events = new ArrayList<>(10000);
            }
        }
        if (events != null) {
            service.submit(new CatulateTask(events, counters));
        }

        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("start reduce");
        int result = counters.stream().reduce(new HashMap<>(), PurchaseCalculator::mergeLicenseCounter)
        .values().stream()
        .reduce(0, PurchaseCalculator::countLicense, PurchaseCalculator::combineLicenseCount);
        return result;
    }

    /**
     * Oraginal method without thread pool, it is quick but will case memory leaks due to excessive data volume sometimes
     *
     * @param file
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public int catulateMinimumCopy1(String file) throws  IOException , InterruptedException {
        Map<String, LicenseCounter> counters = new HashMap<>();

        File csvData = new File(file);
        LineIterator it = FileUtils.lineIterator(csvData, "UTF-8");
        it.nextLine();//skip header
        int i = 0;
        List<String> events = new ArrayList<>(10000);
        while (it.hasNext()) {
            i++;
            processInstallEvent(it.nextLine(), counters, null);
            if (i % 1000000 == 0){
                System.gc();
                System.out.println("process at: " + i);
            }
        }
        System.out.println("start reduce");
        int count = counters.values()
                .parallelStream()
                .reduce(0, PurchaseCalculator::countLicense, PurchaseCalculator::combineLicenseCount);
        return count;
    }

    /**
     * Oraginal method with application id
     * @param file
     * @param appId
     * @return
     * @throws IOException
     */
    public int catulateMinimumCopyForApp(String file, String appId) throws  IOException {

        File csvData = new File(file);
        LineIterator it = FileUtils.lineIterator(csvData, "UTF-8");
        it.nextLine();//skip header

        Map<String, LicenseCounter> counters = new HashMap<>();

        it.forEachRemaining(line -> {
            processInstallEvent(line, counters, appId);
        });

        int count = counters.values()
                .parallelStream()
                .reduce(0, PurchaseCalculator::countLicense, PurchaseCalculator::combineLicenseCount);
        return count;
    }


    public void processInstallEvent(String event, Map<String, LicenseCounter> counters, String appId) {
        String[] item = split(event, ",", 4);

        if (appId != null && !item[2].equals(appId)) {
            return;
        }
        String key = item[1] + "." + item[2];
        LicenseCounter counter = counters.get(key);
        if (counter == null)
        {
            counter = new LicenseCounter();
            counter.setInstallId(key);
            counters.put(key, counter);
        }

        if (item[3].toUpperCase().equals("LAPTOP")){
            counter.countLaptop(new String(item[OFFSET_DEVICE_ID]));
        } else {
            counter.countDesktop(new String(item[OFFSET_DEVICE_ID]));
        }
    }

    public String[] split(String str, String regex, int size) {
        String[] result = new String[size];

        int lastIndex = 0;
        for (int i = 0; i < size; i++) {
            int index = str.indexOf(regex, lastIndex);
            if (index > lastIndex){
                result[i] = str.substring(lastIndex, index);
                lastIndex = index + 1;
            } else {
                result[i] = str.substring(lastIndex, str.length());
                break;
            }
        }
        return result;
    }

    public String[] splitInstallEvent(String event){
        String[] result = new String[3];
        StringBuffer countent = new StringBuffer(event);
        int index = countent.indexOf(",");
        result[0] = countent.substring(0, index);

        int secondIndex = countent.indexOf(",", index + 1);
        secondIndex = countent.indexOf(",", secondIndex + 1);
        result[1] = countent.substring(index + 1, secondIndex);
        int thridIndex = countent.indexOf(",", secondIndex + 1);
        result[2] = countent.substring(secondIndex + 1, thridIndex);
        return result;
    }

    public static Map<String, LicenseCounter> mergeLicenseCounter(Map<String, LicenseCounter> merge, Map<String, LicenseCounter> items){
        items.values().forEach(counter -> {
            if (merge.containsKey(counter.getInstallId())) {
                LicenseCounter newCounter = merge.get(counter.getInstallId()).merge(counter);
                merge.put(newCounter.getInstallId(), newCounter);
            } else {
                merge.put(counter.getInstallId(), counter);
            }
        });

        return merge;
    }

    public static int countLicense(Integer count, LicenseCounter counter) {
        return count + counter.catulateLicenseCount();
    }

    public static int combineLicenseCount(int total, int fragmentCount) {
        return total + fragmentCount;
    }

    class CatulateTask implements Runnable {
        private List<String> events;
        private List<Map<String, LicenseCounter>> mapings;
        CatulateTask(List<String> events, List<Map<String, LicenseCounter>> mapings){
            this.events = events;
            this.mapings = mapings;
        }

        @Override
        public void run() {
            Map<String, LicenseCounter> theCounters = new HashMap<>();
            events.forEach(event -> processInstallEvent(event, theCounters, null));
            mapings.add(theCounters);
            System.out.println("process event: " + events.size());
            System.gc();
        }
    }

}
