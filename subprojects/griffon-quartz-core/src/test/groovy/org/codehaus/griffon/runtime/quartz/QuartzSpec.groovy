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
package org.codehaus.griffon.runtime.quartz

import griffon.annotations.inject.BindTo
import griffon.core.GriffonApplication
import griffon.plugins.quartz.StateBean
import griffon.test.core.GriffonUnitRule
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Inject
import java.util.concurrent.TimeUnit

import static org.awaitility.Awaitility.await

class QuartzSpec extends Specification {
    static {
        System.setProperty('org.slf4j.simpleLogger.defaultLogLevel', 'trace')
    }

    @Rule
    final GriffonUnitRule griffon = new GriffonUnitRule()

    @Inject
    private GriffonApplication application

    @Unroll
    def 'CustomJob changes the state of a bean upon scheduled execution'() {
        given:
        assert !stateBean.changed

        when:
        application.startup()
        await().atMost(30, TimeUnit.SECONDS).until { stateBean.changed }

        then:
        stateBean.changed
    }

    @BindTo(StateBean)
    private StateBean stateBean = new StateBean()
}
