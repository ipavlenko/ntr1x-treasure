//package com.ntr1x.treasure.web.swagger;
//
//import javax.servlet.ServletConfig;
//import javax.servlet.http.HttpServlet;
//
//import io.swagger.jaxrs.config.DefaultJaxrsScanner;
//import io.swagger.jaxrs.config.ReaderConfigUtils;
//import io.swagger.jaxrs.config.WebXMLReader;
//
//public class SwaggerJersey2JaxrsConfig extends HttpServlet {
//
//	private static final long serialVersionUID = 1222031144374494077L;
//
//	@Override
//    public void init(ServletConfig servletConfig) throws javax.servlet.ServletException {
//        super.init(servletConfig);
//
//        servletConfig.getServletContext().setAttribute("reader", new WebXMLReader(servletConfig));
//        servletConfig.getServletContext().setAttribute("scanner", new DefaultJaxrsScanner());
//        ReaderConfigUtils.initReaderConfig(servletConfig);
//    }
//}
