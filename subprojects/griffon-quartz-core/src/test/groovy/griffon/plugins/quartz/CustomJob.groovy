package griffon.plugins.quartz

import org.kordamp.jipsy.ServiceProviderFor
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.quartz.JobExecutionException

import javax.inject.Inject

@javax.inject.Singleton
@ServiceProviderFor(Job)
@Scheduled(cronExpression = '0/2 * * * * ?')
class CustomJob implements Job {
    @Inject
    private StateBean stateBean

    @Override
    void execute(JobExecutionContext context) throws JobExecutionException {
        stateBean.changed = true
    }
}
