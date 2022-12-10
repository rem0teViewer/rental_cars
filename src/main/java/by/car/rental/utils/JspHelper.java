package by.car.rental.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class JspHelper {

    private static final String JSP_FORMAT = "\\WEB-INF\\templates\\%s.jsp";

    public static String getPath(String jspName) {
        return String.format(JSP_FORMAT, jspName);
    }
}