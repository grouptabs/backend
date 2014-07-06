package grouptabs.backend;

import grouptabs.backend.resources.TabResource;
import grouptabs.backend.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.core.MultivaluedMap;

import org.skife.jdbi.v2.DBI;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class GrouptabsBackend extends Application<BackendConfiguration> {

	public static void main(String[] args) throws Exception {
		new GrouptabsBackend().run(args);
	}

	@Override
	public void initialize(Bootstrap<BackendConfiguration> bootstrap) {
		bootstrap.addBundle(new AssetsBundle());
		bootstrap.addBundle(new AssetsBundle("/assets/favicon.ico", "/favicon.ico", null, "favicon"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(BackendConfiguration configuration, Environment environment) throws Exception {
		
		// Support CORS
		environment.jersey().getResourceConfig().getContainerResponseFilters().add(new ContainerResponseFilter() {
			public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
				MultivaluedMap<String, Object> headers = response.getHttpHeaders();
				headers.add("Access-Control-Allow-Origin", "*");
				headers.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,HEAD,OPTIONS");
				headers.add("Access-Control-Allow-Credentials", "true");

				String reqHead = request.getHeaderValue("Access-Control-Request-Headers");
				if (null != reqHead && !reqHead.equals("")) {
					headers.add("Access-Control-Allow-Headers", reqHead);
				}

				return response;
			}
		});

		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "h2");

		// TODO introduce caching authenticator
		// environment.jersey().register(new BasicAuthProvider<User>(new BackendAuthenticator(jdbi), "Web Service Realm"));

		environment.jersey().register(new TabResource(jdbi));
		environment.jersey().register(new UserResource(jdbi));
	}

}
