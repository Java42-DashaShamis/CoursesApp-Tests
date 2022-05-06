package telran.courses.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWTUtils {
	static Logger LOG = LoggerFactory.getLogger(JWTUtils.class);
	@Value("${app.expiration.minutes: 60}")
	long expPeriodMinutes;
	@Value("{app.jwt.secret: x}")
	String secret;
	public String create(String username) { //username->token
		Date currentDate = new Date();
		return Jwts.builder()
					.setExpiration(getExpDate(currentDate))
					.setIssuedAt(currentDate)
					.setSubject(username)
					.signWith(SignatureAlgorithm.HS512, secret)
					.compact();
	}
	
	String validate(String jwt) {//token->username
		try {
			return Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody().getSubject();
		} catch (ExpiredJwtException e) {//time of token is over
			LOG.warn("JWT expired in period {}", expPeriodMinutes);
		} catch (UnsupportedJwtException e) {
			LOG.error("signing algorithm is unsupported");
		} catch (MalformedJwtException e) {
			LOG.error("malformed JWT exception");
		} catch (SignatureException e) {
			LOG.error("corrupted JWT");
		} catch (IllegalArgumentException e) {
			LOG.error("empty token");
		}
		return null;
	}
	private Date getExpDate(Date currentDate) {
		return new Date(currentDate.getTime() + expPeriodMinutes*60000);
	}
	
}
