package telran.courses.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class CoursesSecurityConfigurer extends WebSecurityConfigurerAdapter {
	@Bean //the returned object is bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Value("${app.security.enable: true}")
	private boolean securityEnable;
	@Autowired
	JwtAuthFilter jwtAuthFilter;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.cors().and().csrf().disable();
		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		if(securityEnable) {
			http.authorizeHttpRequests().antMatchers("/login").permitAll();	
			http.authorizeHttpRequests().antMatchers(HttpMethod.GET).hasAnyRole("USER", "ADMIN"); 
			http.authorizeHttpRequests().anyRequest().hasRole("ADMIN");
		}else {
			http.authorizeHttpRequests().anyRequest().permitAll();
		}
	}
}
