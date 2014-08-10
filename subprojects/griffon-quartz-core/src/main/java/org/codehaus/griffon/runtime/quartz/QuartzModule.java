package org.codehaus.griffon.runtime.quartz;

import griffon.core.addon.GriffonAddon;
import griffon.core.injection.Module;
import org.codehaus.griffon.runtime.core.injection.AbstractModule;
import org.kordamp.jipsy.ServiceProviderFor;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;

import javax.inject.Named;

@Named("quartz")
@ServiceProviderFor(Module.class)
public class QuartzModule extends AbstractModule {
    @Override
    protected void doConfigure() {
        // tag::bindings[]
        bind(Scheduler.class)
            .toProvider(SchedulerProvider.class)
            .asSingleton();

        bind(JobFactory.class)
            .to(InjectorAwareJobFactory.class)
            .asSingleton();

        bind(GriffonAddon.class)
            .to(QuartzAdon.class)
            .asSingleton();
        // end::bindings[]
    }
}
