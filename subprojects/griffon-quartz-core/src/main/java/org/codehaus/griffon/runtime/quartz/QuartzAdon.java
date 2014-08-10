/*
 * Copyright $today.year the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.griffon.runtime.quartz;

import griffon.core.GriffonApplication;
import griffon.plugins.quartz.Scheduled;
import griffon.util.ServiceLoaderUtils;
import org.codehaus.griffon.runtime.core.addon.AbstractGriffonAddon;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.TimeZone;

import static griffon.core.GriffonExceptionHandler.sanitize;
import static griffon.plugins.quartz.Scheduled.DEFAULT;
import static java.util.TimeZone.getTimeZone;

/**
 * @author Andres Almiray
 */
@Named("quartz")
public class QuartzAdon extends AbstractGriffonAddon {
    @Inject
    private Scheduler scheduler;

    public void onBootstrapEnd(@Nonnull GriffonApplication application) {
        ServiceLoaderUtils.load(resolveClassLoader(application), "META-INF/services/", Job.class, new ServiceLoaderUtils.LineProcessor() {
            @Override
            public void process(@Nonnull ClassLoader classLoader, @Nonnull Class<?> type, @Nonnull String line) {
                try {
                    String jobClassName = line.trim();
                    Class<? extends Job> jobClass = (Class<? extends Job>) loadClass(jobClassName, classLoader);
                    schedule(jobClass);
                } catch (Exception e) {
                    if (getLog().isWarnEnabled()) {
                        getLog().warn("Could not load/schedule " + type.getName() + " with " + line, sanitize(e));
                    }
                }
            }
        });
    }

    private void schedule(Class<? extends Job> jobClass) throws SchedulerException {
        JobSchedulerBuilder builder = new JobSchedulerBuilder(jobClass);

        if (jobClass.isAnnotationPresent(Scheduled.class)) {
            Scheduled scheduled = jobClass.getAnnotation(Scheduled.class);

            builder
                // job
                .withJobName(scheduled.jobName())
                .withJobGroup(scheduled.jobGroup())
                .withRequestRecovery(scheduled.requestRecovery())
                .withStoreDurably(scheduled.storeDurably())
                    // trigger
                .withCronExpression(scheduled.cronExpression())
                .withTriggerName(scheduled.triggerName());

            if (!DEFAULT.equals(scheduled.timeZoneId())) {
                TimeZone timeZone = getTimeZone(scheduled.timeZoneId());
                if (timeZone != null) {
                    builder.withTimeZone(timeZone);
                }
            }

            builder.schedule(scheduler);
        }
    }

    protected Class<?> loadClass(@Nonnull String className, @Nonnull ClassLoader classLoader) throws ClassNotFoundException {
        ClassNotFoundException cnfe;

        ClassLoader cl = QuartzAdon.class.getClassLoader();
        try {
            return cl.loadClass(className);
        } catch (ClassNotFoundException e) {
            cnfe = e;
        }

        cl = classLoader;
        try {
            return cl.loadClass(className);
        } catch (ClassNotFoundException e) {
            cnfe = e;
        }

        throw cnfe;
    }

    @Nonnull
    private ClassLoader resolveClassLoader(@Nonnull GriffonApplication application) {
        return application.getApplicationClassLoader().get();
    }
}
