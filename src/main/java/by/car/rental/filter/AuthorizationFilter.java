package by.car.rental.filter;

import by.car.rental.domain.UsersRole;
import by.car.rental.dto.UserDto;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static by.car.rental.utils.UrlPath.*;

public class AuthorizationFilter implements Filter {
    private static final Set<String> PUBLIC_PATHS = Set.of(LOGIN, REGISTRATION, LOGOUT, LOCALE);
    private static final Set<String> CLIENT_PATHS = Set.of(CLIENT_DATA, ORDER, CARS, CAR_CATEGORY, IMAGES,
            CLIENT_ORDERS, SEE_ORDER);
    private static final Set<String> ADMIN_PATHS = Set.of(CARS, ADD_CAR, CAR_CATEGORY, IMAGES, ORDERS,
            CHECK_ORDER, DOWNLOAD, SAVE, CHECK);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String uri = ((HttpServletRequest) servletRequest).getRequestURI();

        if ((isAdminPath(uri) && isAdminLogged(servletRequest))) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if ((isClientPath(uri) && isClientLogged(servletRequest))) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (isPublicPath(uri) || isUserLoggedIn(servletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String prevPage = ((HttpServletRequest) servletRequest).getHeader("referer");
            ((HttpServletResponse) servletResponse).sendRedirect(prevPage != null ? prevPage : LOGIN);
        }
    }

    private boolean isUserLoggedIn(ServletRequest servletRequest) {
        UserDto user = (UserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        return user != null;
    }

    private boolean isAdminLogged(ServletRequest servletRequest) {
        UserDto user = (UserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        return user.getRole() == UsersRole.ADMIN;
    }

    private boolean isClientLogged(ServletRequest servletRequest) {
        UserDto user = (UserDto) ((HttpServletRequest) servletRequest).getSession().getAttribute("user");
        return user.getRole() == UsersRole.CLIENT;
    }

    private boolean isPublicPath(String uri) {
        return PUBLIC_PATHS.stream().anyMatch(uri::startsWith) || isAdminPath(uri) || isClientPath(uri);
    }

    private boolean isAdminPath(String uri) {
        return ADMIN_PATHS.stream().anyMatch(uri::startsWith);
    }

    private boolean isClientPath(String uri) {
        return CLIENT_PATHS.stream().anyMatch(uri::startsWith);
    }
}
