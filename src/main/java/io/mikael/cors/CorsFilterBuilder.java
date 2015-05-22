package io.mikael.cors;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

class CorsOriginBuilder {

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

class CorsOriginFilter implements Filter {

    private final CorsFilterBuilder corsFilterBuilder;

    public CorsOriginFilter(final CorsFilterBuilder corsFilterBuilder) {
        this.corsFilterBuilder = corsFilterBuilder;
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
        for (final CorsOriginBuilder o : corsFilterBuilder.originBuilders) {
            if (origin.startsWith(o.origin)) {
                addHeaders(res, o);
                chain.doFilter(req, res);
                return;
            }
        }
        if (null != corsFilterBuilder.anyOriginBuilder) {
            addHeaders(res, corsFilterBuilder.anyOriginBuilder);
            chain.doFilter(req, res);
        }
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {

    }
}
