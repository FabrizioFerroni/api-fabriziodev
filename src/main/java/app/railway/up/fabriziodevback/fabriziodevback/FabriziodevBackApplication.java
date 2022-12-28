package app.railway.up.fabriziodevback.fabriziodevback;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class FabriziodevBackApplication {

	@Value("${cloudinary.cloud_name}")
	private String cloudName;

	@Value("${cloudinary.api_key}")
	private String apiKey;

	@Value("${cloudinary.api_secret}")
	private String apiSecret;

	@Value("${hostfront.dns}")
	private String front;

	@Value("${hostadmin.dns}")
	private String admin;

	private static final Logger logger = LoggerFactory.getLogger(FabriziodevBackApplication.class);



	public static void main(String[] args) {
		SpringApplication.run(FabriziodevBackApplication.class, args);
	}



	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**").allowedOrigins(front, admin).allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*");
				registry.addMapping("/file/**").allowedOrigins(front, admin).allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*");
				registry.addMapping("/auth/**").allowedOrigins(front, admin).allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*");
			}
		};
	}

	@Bean
	public Cloudinary cloudinaryConfig() {
		Cloudinary cloudinary = null;
		Map config = new HashMap();
		config.put("cloud_name", cloudName);
		config.put("api_key", apiKey);
		config.put("api_secret", apiSecret);
		cloudinary = new Cloudinary(config);
		return cloudinary;
	}


}
