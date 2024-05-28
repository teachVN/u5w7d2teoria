package it.epicode.u5w7d2teoria.security;

import it.epicode.u5w7d1teoria.entity.User;
import it.epicode.u5w7d1teoria.exception.UnauthorizedException;
import it.epicode.u5w7d1teoria.exception.UserNotFoundException;
import it.epicode.u5w7d1teoria.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTool jwtTool;
    @Autowired
    private UserService userService;
    @Override //metodo per verificare che nella richiesta ci sia il token, altrimenti non si è autorizzati
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        System.out.println("sono qui");

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            throw  new UnauthorizedException("Error in authorization, token missing!");
        }

        String token = authHeader.substring(7);

        jwtTool.verifyToken(token);

        int userId = Integer.parseInt(jwtTool.getUserIdFromToken(token));

        Optional<User> userOptional = userService.getUserById(userId);

        if(userOptional.isPresent()){
            User user = userOptional.get();
            System.out.println(user);

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            System.out.println(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        else{
            throw new UserNotFoundException("User with id=" + userId + " not found");
        }

        filterChain.doFilter(request, response);
    }

    @Override //permette di non effettuare l'autenticazione per usare i servizi di autenticazione
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        System.out.println("should");
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
