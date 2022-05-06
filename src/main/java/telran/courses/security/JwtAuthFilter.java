package telran.courses.security;

import java.io.IOException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	static Logger LOG = LoggerFactory.getLogger(JwtAuthFilter.class);
	private static final String BEARER = "Bearer";
	@Autowired
	JWTUtils jwtUtils;
	@Autowired
	UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		LOG.trace("filter with header {}", request.getHeader("Authorization"));
		String jwt = parseJwt(request);
		SecurityContextHolder.getContext().setAuthentication(null);
		if(jwt!=null) {
			String username = jwtUtils.validate(jwt);
			if(username!=null) {
				UserDetails user = userDetailsService.loadUserByUsername(username); //=>we can build security context
				
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); //=> default token for this user
				//authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);//=>security context
			}
		}
		filterChain.doFilter(request, response);

	}

	private String parseJwt(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		String res = null;
		if(authHeader!=null && authHeader.startsWith(BEARER)) {
			res = authHeader.substring(BEARER.length());
		}
		return res;
	}

}
