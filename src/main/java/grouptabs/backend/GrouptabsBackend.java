package grouptabs.backend;

import grouptabs.backend.resource.TabResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.skife.jdbi.v2.DBI;

public class GrouptabsBackend extends Application<BackendConfiguration> {

	public static void main(String[] args) throws Exception {
		new GrouptabsBackend().run(args);
	}

	@Override
	public void initialize(Bootstrap<BackendConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle());
		bootstrap.addBundle(new AssetsBundle("/assets/favicon.ico", "/favicon.ico", null, "favicon"));
	}

	@Override
	public void run(BackendConfiguration configuration, Environment environment) throws Exception {
		
		// Support CORS
	    FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
	    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
	    filter.setInitParameter("allowedOrigins", "*"); // allowed origins comma separated
	    filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
	    filter.setInitParameter("allowedMethods", "GET,PUT,POST,DELETE,OPTIONS,HEAD");
	    filter.setInitParameter("preflightMaxAge", "5184000"); // 2 months
	    filter.setInitParameter("allowCredentials", "true");
	    
		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "h2");

		// TODO introduce caching authenticator
		// environment.jersey().register(new BasicAuthProvider<User>(new BackendAuthenticator(jdbi), "Web Service Realm"));

		environment.jersey().register(new TabResource(jdbi));
	}

}
