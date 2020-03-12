package twitter.opinion.mining;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import twitter.opinion.mining.domain.service.upload.StorageProperties;
import twitter.opinion.mining.domain.service.upload.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class TwitterOpinionMiningApplication {
	public static void main(String[] args) {
		SpringApplication.run(TwitterOpinionMiningApplication.class, args);
	}
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
	//		storageService.deleteAll();
			storageService.init();
		};
	}
}
