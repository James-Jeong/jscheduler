package schedule.unit;

import job.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import schedule.handler.JobScheduler;

import java.util.ArrayList;
import java.util.List;

public class ScheduleUnit {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleUnit.class);

    public static final int DEFAULT_THREAD_COUNT = 5;
    private final long createdTime = System.currentTimeMillis();

    private final String scheduleUnitKey;

    private final int poolSize; // Thread pool size
    private final JobScheduler jobScheduler;

    private final List<String> jobKeyList = new ArrayList<>();

    ////////////////////////////////////////////////////////////////////////////////

    public ScheduleUnit(String key, int poolSize, int queueSize) {
        this.scheduleUnitKey = key;

        if (poolSize > 0) {
            this.poolSize = poolSize;
        } else {
            this.poolSize = DEFAULT_THREAD_COUNT;
        }

        jobScheduler = new JobScheduler(scheduleUnitKey, poolSize, queueSize);
    }

    ////////////////////////////////////////////////////////////////////////////////

    public boolean start(Job job) {
        if (job == null) { return false; }
        job.setScheduleUnitKey(scheduleUnitKey);
        synchronized (jobKeyList) {
            jobKeyList.add(job.getName());
        }
        return jobScheduler.schedule(job);
    }

    public void stop(Job job) {
        if (job == null) { return; }
        job.setScheduleUnitKey(null);
        synchronized (jobKeyList) {
            jobKeyList.remove(job.getName());
        }
        jobScheduler.cancel(job);
    }

    public void stopAll() {
        synchronized (jobKeyList) {
            jobKeyList.clear();
        }
        jobScheduler.stop();
    }

    ////////////////////////////////////////////////////////////////////////////////

    public int getJobKeyListSize() {
        return jobKeyList.size();
    }

    public JobScheduler getJobScheduler() {
        return jobScheduler;
    }

    public String getScheduleUnitKey() {
        return scheduleUnitKey;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public List<String> getJobKeyList() {
        return jobKeyList;
    }

    ////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "ScheduleUnit{" +
                "key='" + scheduleUnitKey + '\'' +
                ", threadCount=" + poolSize +
                '}';
    }
}
