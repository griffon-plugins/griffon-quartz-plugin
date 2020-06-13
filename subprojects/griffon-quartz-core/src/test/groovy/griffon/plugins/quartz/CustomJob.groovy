/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2014-2020 The author and/or original authors.
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
