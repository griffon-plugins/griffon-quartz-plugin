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

import griffon.core.Configuration;
import griffon.exceptions.GriffonException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;
import java.util.Properties;

/**
 * @author Andres Almiray
 */
public class SchedulerProvider implements Provider<Scheduler> {
    private static final String KEY_SCHEDULER_PROPERTIES = "scheduler.properties";
    private static final String KEY_SCHEDULER_AUTO_START = "scheduler.auto-start";

    @Inject
    private JobFactory jobFactory;

    @Inject
    private Configuration configuration;

    @Override
    public Scheduler get() {
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = null;

        try {
            if (configuration.containsKey(KEY_SCHEDULER_PROPERTIES)) {
                Object properties = configuration.get(KEY_SCHEDULER_PROPERTIES);
                if (properties instanceof Properties) {
                    schedulerFactory.initialize((Properties) properties);
                } else if (properties instanceof Map) {
                    schedulerFactory.initialize(toProperties((Map) properties));
                }
            }

            scheduler = schedulerFactory.getScheduler();
            scheduler.setJobFactory(jobFactory);

            if (configuration.containsKey(KEY_SCHEDULER_AUTO_START)) {
                if (configuration.getAsBoolean(KEY_SCHEDULER_AUTO_START)) {
                    scheduler.start();
                }
            } else {
                scheduler.start();
            }
        } catch (SchedulerException e) {
            throw new GriffonException(e);
        }

        return scheduler;
    }

    @Nonnull
    private Properties toProperties(@Nonnull Map map) {
        Properties props = new Properties();
        for (Object o : map.entrySet()) {
            Map.Entry e = (Map.Entry) o;
            props.put(e.getKey(), e.getValue());
        }

        return props;
    }
}
