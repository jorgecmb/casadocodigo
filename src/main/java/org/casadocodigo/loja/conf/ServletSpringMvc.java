package org.casadocodigo.loja.conf;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ServletSpringMvc extends AbstractAnnotationConfigDispatcherServletInitializer{

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {SecurityConfiguration.class, AppWebConfiguration.class, JPAConfiguration.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
    
    @Override
    protected Filter[] getServletFilters() {
    	CharacterEncodingFilter filter = new CharacterEncodingFilter();
    	filter.setEncoding("UTF-8");
    	
    	return new Filter[] {filter, new OpenEntityManagerInViewFilter()}; //Deixar o entity manager aberto até a view para evitar LazyInitializationException
    }

    @Override
    protected void customizeRegistration(Dynamic registration) {
    	registration.setMultipartConfig(new MultipartConfigElement(""));
    }
    
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	super.onStartup(servletContext);
    	servletContext.addListener(RequestContextListener.class); //Registrando um context listener
    	servletContext.setInitParameter("spring.profiles.active", "dev"); //Iniciando o profile dev
    }
}
