import com.pealipala.utils.DesUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

public class Spring_email {
    public static void main(String[] args) throws Exception {
        // 使用JAVA程序发送邮件

        ApplicationContext application = new ClassPathXmlApplicationContext("spring/spring-*.xml");

        // 邮件发送器，由Spring框架提供的
        JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl)application.getBean("sendMail");

        javaMailSender.setDefaultEncoding("UTF-8");
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail);
        helper.setSubject("标题");
        StringBuilder content = new StringBuilder();

        String param = "userid123";
        param = DesUtil.encrypt(param, "abcdefghijklmnopquvwxyz");

        content.append("<a href='http://www.pealipala.cn/test/act.do?p="+param+"'>激活链接</a>");
        helper.setText(content.toString(), true);
        helper.setFrom("admin@pealipala.cn");
        helper.setTo("test@pealipala.cn");
        javaMailSender.send(mail);

    }
}
