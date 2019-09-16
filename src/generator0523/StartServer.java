package generator0523;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StartServer {
	
	public void generator() throws Exception{
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;
		File configFile = new File("genarator.xml"); 
		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
				callback, warnings);
		myBatisGenerator.generate(null);
	}
	
	public static void main(String[] args) throws Exception {
		try {
			StartServer startServer = new StartServer();
			//startServer.generator();
			args = new String[] { "-configfile", "src\\genarator.xml", "-overwrite" };
			ShellRunner.main(args);

		} catch (Exception e) {
			e.printStackTrace();
		}
}

}
