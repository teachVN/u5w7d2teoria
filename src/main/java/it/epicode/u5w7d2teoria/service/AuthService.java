package it.epicode.u5w7d2teoria.service;

import it.epicode.u5w7d2teoria.dto.UserLoginDto;
import it.epicode.u5w7d2teoria.entity.User;
import it.epicode.u5w7d2teoria.exception.UnauthorizedException;
import it.epicode.u5w7d2teoria.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTool jwtTool;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public String authenticateUserAndCreateToken(UserLoginDto userLoginDto){
        User user = userService.getUserByEmail(userLoginDto.getEmail());

        if(passwordEncoder.matches(userLoginDto.getPassword(),user.getPassword())){
            return jwtTool.createToken(user);
        }
        else{
            throw  new UnauthorizedException("Error in authorization, relogin!");
        }
    }
}
