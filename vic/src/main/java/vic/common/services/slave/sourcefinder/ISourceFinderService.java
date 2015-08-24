package vic.common.services.slave.sourcefinder;

public interface ISourceFinderService {

	public final String Mediator = "WWW.ankmdb.rhcloud.com";

	public String getSourceIp();

	public boolean checkIsAliveSource(String ip);
}
