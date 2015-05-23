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

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A programmatically configured filter
 */
public class CorsOriginFilter implements Filter {

    private final CorsFilterBuilder builder;

    public CorsOriginFilter(final CorsFilterBuilder builder) {
        this.builder = builder;
    }

    private static void addHeaders(final ServletResponse res, final CorsOriginBuilder builder) {
        final HttpServletResponse response = (HttpServletResponse) res;
        if (null != builder.origin) {
            response.setHeader("Access-Control-Allow-Origin", builder.origin);
        }
        if (null != builder.exposeHeaders) {
            response.setHeader("Access-Control-Expose-Headers", String.join(", ", builder.exposeHeaders));
        }
        if (null != builder.maxAge) {
            response.setHeader("Access-Control-Max-Age", builder.maxAge.toString());
        }
        if (null != builder.allowCredentials) {
            response.setHeader("Access-Control-Allow-Credentials", builder.allowCredentials.toString());
        }
        if (null != builder.allowHeaders) {
            response.setHeader("Access-Control-Allow-Headers", String.join(", ", builder.allowHeaders));
        }
        if (null != builder.allowMethods) {
            response.setHeader("Access-Control-Allow-Methods", String.join(", ", builder.allowMethods));
        }
    }

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) req;
        final String origin = httpRequest.getHeader("Origin");
        for (final CorsOriginBuilder o : builder.originBuilders) {
            if (origin.startsWith(o.origin)) {
                addHeaders(res, o);
                chain.doFilter(req, res);
                return;
            }
        }
        if (null != builder.anyOriginBuilder) {
            addHeaders(res, builder.anyOriginBuilder);
            chain.doFilter(req, res);
        }
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }
}
