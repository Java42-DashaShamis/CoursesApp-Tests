package telran.courses.controller;

import java.util.Base64;

import javax.validation.Valid;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import telran.courses.dto.LoginData;
import telran.courses.dto.LoginResponse;
import telran.courses.exceptions.BadRequestException;
import telran.courses.security.Account;
import telran.courses.security.AccountingManagement;
import telran.courses.security.JWTUtils;

@RestController
@RequestMapping("/login")
@CrossOrigin
@Validated
public class AuthController {
	
	static Logger LOG = LoggerFactory.getLogger(AuthController.class);
	
	AccountingManagement accounting;
	PasswordEncoder passwordEncoder;
	public AuthController(AccountingManagement accounting, PasswordEncoder passwordEncoder) {
		this.accounting = accounting;
		this.passwordEncoder = passwordEncoder;
	}
	@Value("${app.security.enable: true}")
	private boolean securityEnable;
	@Autowired
	JWTUtils jwtUtils;
	
	@PostMapping
	LoginResponse login(@RequestBody @Valid LoginData loginData) {
		if(!securityEnable) {
			LoginResponse response = new LoginResponse("", "ADMIN");
			return response;
		}
		LOG.debug("login data are email {}, password {}", loginData.email, loginData.password);
		Account account = accounting.getAccount(loginData.email);
		if(account == null || !passwordEncoder.matches(loginData.password, account.getPasswordHash())) {
			throw new BadRequestException("Wrong credentials");
		}
		LoginResponse response = new LoginResponse(getToken(loginData), account.getRole());
		LOG.debug("accessToken: {}, role: {}", response.accessToken, response.role);
		return response;
	}

	private String getToken(LoginData loginData) {
		//"Basic <username:password> in Base64 code
		
		return "Bearer" + jwtUtils.create(loginData.email); 
	}
}
