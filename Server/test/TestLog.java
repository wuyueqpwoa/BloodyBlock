import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试日志
 * Created by wuy on 2017/5/13.
 */
public class TestLog {
	public static void main(String[] args) throws Exception {
		Logger logger = LoggerFactory.getLogger(TestLog.class);
		logger.debug("Hello Logger!");
	}
}
