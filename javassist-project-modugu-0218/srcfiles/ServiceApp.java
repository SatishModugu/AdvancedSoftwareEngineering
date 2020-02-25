package target;

@Table(name = "Service", id = 101)
@Author(name = "SA", year = 1991)
public class ServiceApp {
	@Row(name = "Service_NAME", id = 102)
	@Author(name = "SB", year = 1992)
	String componentName;
	
	@Row(name = "Service_PROVIDER", id = 104)
	@Author(name = "SC", year = 1993)
	String componentProvider;

	@Column(name = "Service_LOCATION", id = 104)
	@Author(name = "SD", year = 1994)
	String componentLocation;

}