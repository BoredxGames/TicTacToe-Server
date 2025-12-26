package com.mycompany.tictactoeserver.domain.authentication;

import com.mycompany.tictactoeserver.domain.entity.AuthenticationDto;

public interface AuthenticationManager {
    void register(AuthenticationDto credentials);
    void login(AuthenticationDto credentials);
    void logout();

}
