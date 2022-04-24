package telran.courses.security;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountingManagement {
	private Map<String, Account> accounts = new ConcurrentHashMap<>();
	public AccountingManagement() {
		accounts.put("admin@tel-ran.co.il", new Account("admin@tel-ran.co.il", "$2a$10$0d.gqun7BTHuD1lNHDNWAujVXkHwcpZIXGiXb8oJvA/JbJjfKcrpm", "ADMIN"));
		accounts.put("user@tel-ran.co.il", new Account("user@tel-ran.co.il", "$2a$10$rSdI0lSvHmwhzOxLQ1olOujYO4gIGgRhst03Si3vKxtpASI/4W3Ni", "USER"));
	}
	public Account getAccount(String username) {
		return accounts.get(username);
	}
}
