package job;

public abstract class JobContainer {

    private Job job = null;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

}
