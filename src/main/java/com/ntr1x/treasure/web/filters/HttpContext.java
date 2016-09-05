//package com.ntr1x.treasure.web.filters;
//
//import javax.enterprise.context.RequestScoped;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Locale;
//
//@RequestScoped
//public class HttpContext {
//	
//	private final static ThreadLocal<HttpContext> contextThreadLocal = new ThreadLocal<HttpContext>();
//
//	static void build(HttpServletRequest request, HttpServletResponse response) {
//		contextThreadLocal.set(new HttpContext(request, response));
//	}
//
//	static void destroy() {
//		contextThreadLocal.remove();
//	}
//
//	public static HttpContext get() {
//		return contextThreadLocal.get();
//	}
//
//	private HttpServletRequest request;
//	private HttpServletResponse response;
//
//	private HttpContext(HttpServletRequest request, HttpServletResponse response) {
//		this.request = request;
//		this.response = response;
//	}
//
//	public HttpServletRequest getRequest() {
//		return request;
//	}
//
//	public HttpServletResponse getResponse() {
//		return response;
//	}
//
//	public Locale getLocale() {
//		return request.getLocale();
//	}
//}