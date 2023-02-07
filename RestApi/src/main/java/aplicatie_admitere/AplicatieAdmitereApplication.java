package aplicatie_admitere;

import aplicatie_admitere.config.RsaKeyProperties;
import aplicatie_admitere.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@EnableConfigurationProperties({RsaKeyProperties.class})
@SpringBootApplication
public class AplicatieAdmitereApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(AplicatieAdmitereApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{

	}

}
