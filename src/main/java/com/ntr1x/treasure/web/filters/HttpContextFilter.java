//package com.ntr1x.treasure.web.filters;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.Produces;
//import java.io.IOException;
//
//public class HttpContextFilter implements Filter {
//
//	@Override
//	public void init(FilterConfig config) throws ServletException {
//		// do nothing
//	}
//
//	@Override
//	public void destroy() {
//		// do nothing
//	}
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		
//		try {
//			HttpContext.build((HttpServletRequest) request, (HttpServletResponse) response);
//			chain.doFilter(request, response);
//		} finally {
//			HttpContext.destroy();
//		}
//	}
//	
//	@Produces
//	public HttpContext produce() {
//		return HttpContext.get();
//	}
//}