/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2014-2021 The author and/or original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.griffon.runtime.quartz;

import griffon.core.addon.GriffonAddon;
import griffon.core.injection.Module;
import org.codehaus.griffon.runtime.core.injection.AbstractModule;
import org.kordamp.jipsy.annotations.ServiceProviderFor;
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
            .to(QuartzAddon.class)
            .asSingleton();
        // end::bindings[]
    }
}
