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

/**
 * An intermediate builder to gather header values for a specific origin.
 */
public class CorsOriginBuilder {

    protected final CorsFilterBuilder parent;
    protected final String origin;

    protected String allowOrigin;
    protected Boolean allowCredentials;
    protected String[] exposeHeaders;
    protected Long maxAge;
    protected String[] allowMethods;
    protected String[] allowHeaders;

    public CorsOriginBuilder(final CorsFilterBuilder parent, final String origin) {
        this.parent = parent;
        this.origin = origin;
    }

    public CorsOriginBuilder allowOrigin(final String origin) {
        allowOrigin = origin;
        return this;
    }

    public CorsOriginBuilder allowCredentials(final Boolean allow) {
        allowCredentials = allow;
        return this;
    }

    public CorsOriginBuilder exposeHeaders(final String... headers) {
        exposeHeaders = headers;
        return this;
    }

    public CorsOriginBuilder maxAge(final Long seconds) {
        maxAge = seconds;
        return this;
    }

    public CorsOriginBuilder allowMethods(final String... methods) {
        allowMethods = methods;
        return this;
    }

    public CorsOriginBuilder allowHeaders(final String... headers) {
        allowHeaders = headers;
        return this;
    }

    public CorsFilterBuilder done() {
        return parent;
    }

}
