import com.pealipala.bean.User;
import com.pealipala.manager.service.UserService;
import com.pealipala.utils.MD5Util;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public class Test4 {

	public static void main(String[] args) throws Exception {
		ApplicationContext ioc = new ClassPathXmlApplicationContext("spring/spring*.xml");
		
		UserService userService = ioc.getBean(UserService.class);
		
		
		for (int i = 1; i <= 100; i++) {
			User user = new User();
			user.setLoginacct("test"+i);
			user.setUserpswd(MD5Util.digest("123"));
			user.setUsername("test"+i);
			user.setEmail("test"+i+"@atguigu.com");
			user.setCreatetime("2019-08-08 14:17:00");
			userService.saveUser(user);
		}
		
	}

}
