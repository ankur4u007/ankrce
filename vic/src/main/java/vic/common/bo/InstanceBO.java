package vic.common.bo;

public class InstanceBO {

	private final String name;
	private final Integer port;
	private final String status;

	public InstanceBO(final String name, final Integer port, final String status) {
		super();
		this.name = name;
		this.port = port;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public Integer getPort() {
		return port;
	}

	public String getStatus() {
		return status;
	}

}
