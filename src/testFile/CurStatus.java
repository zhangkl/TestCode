package testFile;

import java.sql.Timestamp;

/**
 * Created by zhangkl on 2017/8/8.
 */
public class CurStatus {

    public CurStatus(Double rate) {
        this.rate = rate;
    }

    private Double rate;

    public Double getRate() {
        if (rate.isNaN() || rate.isInfinite()) {
            rate = Double.valueOf(0);
        }
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
