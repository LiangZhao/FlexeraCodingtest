package au.com.flexera.codingtest;

import java.util.HashSet;
import java.util.Set;

/**
 * LicenseCounter
 * @author LiangZhao
 * @version 1.0
 * @since 13/05/2017
 *
 */
public class LicenseCounter {


    private int laptopCount;
    private int desktopCount;
    private String installId;

    public String getInstallId() {
        return installId;
    }

    public void setInstallId(String installId) {
        this.installId = installId;
    }

    private Set<String> laptopIdCache;
    private Set<String> desktopIdCache;

    public LicenseCounter(){
        this.laptopIdCache = new HashSet<>();
        this.desktopIdCache = new HashSet<>();
    }

    public int getLaptopCount() {
        return laptopCount;
    }

    public void setLaptopCount(int laptopCount) {
        this.laptopCount = laptopCount;
    }

    public int getDesktopCount() {
        return desktopCount;
    }

    public void setDesktopCount(int desktopCount) {
        this.desktopCount = desktopCount;
    }

    public int countLaptop(String deviceId) {
        if (!laptopIdCache.contains(deviceId)) {
            laptopIdCache.add(deviceId);
            return ++laptopCount;
        }else {
            return laptopCount;
        }
    }

    public int countDesktop(String deviceId) {
        if (!desktopIdCache.contains(deviceId)) {
            desktopIdCache.add(deviceId);
            return ++desktopCount;
        } else {
            return desktopCount;
        }
    }

    public int catulateLicenseCount(){
        int result = this.laptopCount;
        if (this.desktopCount > this.laptopCount){
            result+= this.desktopCount - this.laptopCount;
        }
        return result;
    }

    /**
     * Use in function programming version, but it will cause more memory use so give up this solution.
     * @param counter
     * @return
     */
    public LicenseCounter merge(LicenseCounter counter){
        LicenseCounter result = new LicenseCounter();
        result.setInstallId(counter.installId);

        Set<String> container = new HashSet<>(this.laptopIdCache);
        container.retainAll(counter.laptopIdCache);
        this.laptopIdCache.addAll(counter.laptopIdCache);
        result.laptopIdCache = this.laptopIdCache;
        result.setLaptopCount(this.laptopCount + counter.laptopCount - container.size());

        container = new HashSet<>(this.desktopIdCache);
        container.retainAll(counter.desktopIdCache);
        this.desktopIdCache.addAll(counter.desktopIdCache);
        result.desktopIdCache = this.desktopIdCache;
        result.setDesktopCount(this.desktopCount + counter.desktopCount - container.size());

        return result;
    }

    @Override
    public String toString() {
        return "LicenseCounter{" +
                "laptopCount=" + laptopCount +
                ", desktopCount=" + desktopCount +
                ", installId='" + installId + '\'' +
                ", laptopIdCache=" + laptopIdCache +
                ", desktopIdCache=" + desktopIdCache +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LicenseCounter)) return false;

        LicenseCounter counter = (LicenseCounter) o;

        return installId.equals(counter.installId);
    }

    @Override
    public int hashCode() {
        return installId.hashCode();
    }
}
