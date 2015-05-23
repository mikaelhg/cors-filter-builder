/*
Copyright 2015 Mikael Gueck

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package io.mikael.cors;

import java.util.ArrayList;
import java.util.List;

/**
 * Easily build a CORS filter for your specific need.
 */
public class CorsFilterBuilder {

    protected volatile List<CorsOriginBuilder> originBuilders = new ArrayList<CorsOriginBuilder>();

    protected volatile CorsOriginBuilder anyOriginBuilder;

    public CorsOriginBuilder forAnyOrigin() {
        final CorsOriginBuilder ret = new CorsOriginBuilder(this, "*");
        this.anyOriginBuilder = ret;
        return ret;
    }

    public CorsOriginBuilder forOrigin(final String origin) {
        final CorsOriginBuilder ret = new CorsOriginBuilder(this, origin);
        this.originBuilders.add(ret);
        return ret;
    }

    public CorsOriginFilter build() {
        return new CorsOriginFilter(this);
    }

}

