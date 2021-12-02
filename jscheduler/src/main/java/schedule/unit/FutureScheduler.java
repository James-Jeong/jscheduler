package schedule.unit;

import job.Job;
import schedule.ScheduleManager;

import java.util.TimerTask;

public class FutureScheduler extends TimerTask {

    private final Job job;

    public FutureScheduler(Job job) {
        this.job = job;
    }

    @Override
    public void run() {
        if (job.getScheduleUnitKey() == null) {
            return;
        }

        ScheduleUnit scheduleUnit = ScheduleManager.getInstance().getScheduleUnit(job.getScheduleUnitKey());
        if (scheduleUnit.getJobScheduler().isJobFinished(job)) {
            return;
        }

        scheduleUnit.getJobScheduler().addJobToExecutor(job);

        if (!job.getIsInitialFinished() && job.getInitialDelay() > 0) {
            job.setIsInitialFinished(true);
            scheduleUnit.getJobScheduler().schedule(job);
        }
    }

}