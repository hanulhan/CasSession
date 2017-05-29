package com.acentic.cloudservices.session.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.interceptor.I18nInterceptor;

public class LocaleFilter implements Filter {

    private List<String> availableLocale;
    private String defaultLocale = "EN-EN";
    private Locale newlocale = Locale.ENGLISH;

    protected static final Logger LOGGER = Logger.getLogger(LocaleFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            try {
                String loc = request.getLocale().toString().toUpperCase();
                if ((availableLocale != null) && (isInvalidLocale(loc))) {
                    HttpServletRequest req = (HttpServletRequest) request;
                    try {
                        Locale used = (Locale) req.getSession().getAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE);
                        if (used != newlocale) {
                            LOGGER.log(Level.DEBUG, "invalid locale used " + loc + " setting to " + newlocale.toString());
                            req.getSession().setAttribute(I18nInterceptor.DEFAULT_SESSION_ATTRIBUTE, newlocale);
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.ERROR, "error setting locale ", e);
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.ERROR, "error setting locale ", e);
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isInvalidLocale(String alocale) {
        for (String localeItem : availableLocale) {
            if (alocale.startsWith(localeItem)) {
                return false;
            }
        }
        return true;

    }

    private List<String> builLocaleList(String locale) {
        if (null != locale && locale.trim().length() != 0) {
            List<String> list = new ArrayList<String>();
            String[] tokens = locale.split(",");
            for (String token : tokens) {
                list.add(token.trim().toUpperCase());
            }
            return Collections.unmodifiableList(list);
        } else {
            return null;
        }
    }

    public void init(FilterConfig filterConfig) {
        availableLocale = builLocaleList(filterConfig.getInitParameter("availableLocale"));
        String s = filterConfig.getInitParameter("defaultLocale");
        if ((s != null) && (!s.isEmpty())) {
            defaultLocale = s;
            newlocale = fromString(defaultLocale);
        }

    }

    public void destroy() {
    }

    private static Locale fromString(String locale) {
        Locale l = null;
        String parts[] = locale.split("_", -1);
        if (parts.length == 1) {
            l = new Locale(parts[0]);
        } else if (parts.length == 2 || (parts.length == 3 && parts[2].startsWith("#"))) {
            l = new Locale(parts[0], parts[1]);
        } else {
            l = new Locale(parts[0], parts[1], parts[2]);
        }

        LOGGER.log(Level.DEBUG, "setting default locale to " + l.toString());
        return l;
    }
}
