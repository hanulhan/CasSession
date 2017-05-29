package com.acentic.cloudservices.session.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExpiresFilter implements Filter {

    private Integer secs = -1;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        if (secs > -1) {
            HttpServletRequest req = (HttpServletRequest) request;
            String requestURI = req.getRequestURI();

            if ((requestURI.toLowerCase().endsWith(".js"))
                    || (requestURI.toLowerCase().endsWith(".css"))
                    || (requestURI.toLowerCase().endsWith(".json"))) {

                //System.out.println("req: " + requestURI);
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.SECOND, secs);

                //  HTTP header date format: Thu, 01 Dec 1994 16:00:00 GMT
                String o = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz").format(c.getTime());
                ((HttpServletResponse) response).setHeader("Expires", o);
                // ((HttpServletResponse) response).setHeader("expiresFiilter", "SET");
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        String expiresAfter = filterConfig.getInitParameter("secs");
        if (expiresAfter != null) {
            try {
                secs = Integer.parseInt(expiresAfter);
            } catch (NumberFormatException nfe) {
                // badly configured
            }
        }
    }

    @Override
    public void destroy() {
    }
}
