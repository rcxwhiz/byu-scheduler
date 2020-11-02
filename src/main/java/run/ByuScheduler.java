package run;

import api.ByuApi;
import model.Semester;
import model.SemesterYear;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ByuScheduler
{
	public static void main(String[] args)
	{
		SpringApplication.run(ByuScheduler.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx)
	{
		return args ->
		{
			System.out.println("hello");
			Semester semester = ByuApi.getSemester(new SemesterYear(1, 2020));
			System.out.println("bye");
		};
	}
}
