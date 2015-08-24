package vic.common.services.forkservice.envs.windows.shell;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import vic.common.services.forkservice.shellservice.IShellService;

@Component
@Profile("windows")
public class ShellServiceForWin implements IShellService {

	@Override
	public String[] getCallingParams(final String... params) {
		final String[] customStart = { "CMD.exe", "/C", "start", "/i" };
		final int size = params.length + customStart.length;
		final String[] toReturnArr = new String[size];
		int i = 0;
		for (; i < customStart.length; i++) {
			toReturnArr[i] = customStart[i];
		}
		for (int j = 0; j < params.length; j++) {
			toReturnArr[i + j] = params[j];
		}
		return toReturnArr;
	}
}
