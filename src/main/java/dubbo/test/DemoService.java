package dubbo.test;

public interface DemoService {
	long ipToLong(String strIp);
	String ip2location(String strIp) throws Exception;
}
