package it.epicode.u5w7d2teoria.security;

import it.epicode.u5w7d2teoria.entity.User;
import it.epicode.u5w7d2teoria.exception.UnauthorizedException;
import it.epicode.u5w7d2teoria.exception.NotFoundException;
import it.epicode.u5w7d2teoria.service.UserService;
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


        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            throw  new UnauthorizedException("Error in authorization, token missing!");
        }

        String token = authHeader.substring(7);

        jwtTool.verifyToken(token);
        //aggiunta di questa sezione per recuperare lo user che ha l'id che si trova nel token. Serve
        //per creare un oggetto di tipo authentication che contiene i ruoli dell'utente e inserirli nel contesto della sicurezza
        int userId = jwtTool.getIdFromToken(token);

        Optional<User> userOptional = userService.getUserById(userId);

        if(userOptional.isPresent()){
            User user = userOptional.get();

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        else{
            throw new NotFoundException("User with id=" + userId + " not found");
        }

        filterChain.doFilter(request, response);
    }

    @Override //permette di non effettuare l'autenticazione per usare i servizi di autenticazione
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
