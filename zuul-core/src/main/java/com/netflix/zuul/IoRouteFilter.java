/**
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.zuul;

import com.netflix.zuul.metrics.ZuulMetrics;
import rx.Observable;

public abstract class IoRouteFilter<Request> implements RouteFilter<Request> {
    public abstract Observable<IngressResponse> routeToOrigin(EgressRequest<Request> egressReq);

    @Override
    public Observable<IngressResponse> execute(EgressRequest<Request> egressReq) {
        final long startTime = System.currentTimeMillis();
        return routeToOrigin(egressReq).doOnError(ex ->
                ZuulMetrics.markFilterFailure(getClass(), System.currentTimeMillis() - startTime)).doOnCompleted(() ->
                ZuulMetrics.markFilterSuccess(getClass(), System.currentTimeMillis() - startTime));
    }
}