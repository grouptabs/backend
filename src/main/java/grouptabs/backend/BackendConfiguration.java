package grouptabs.backend;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BackendConfiguration extends Configuration {

	@JsonProperty
	private DataSourceFactory database = new DataSourceFactory();
	
	public DataSourceFactory getDataSourceFactory() {
		return database;
	}
}