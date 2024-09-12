package com.msara.servicio.services.impl;

import com.msara.servicio.controllers.dto.AuthLoginRequest;
import com.msara.servicio.controllers.dto.AuthRegisterRequest;
import com.msara.servicio.controllers.dto.AuthResponse;
import com.msara.servicio.domain.entities.CartEntity;
import com.msara.servicio.domain.entities.RoleEntity;
import com.msara.servicio.domain.entities.UserEntity;
import com.msara.servicio.domain.repositories.CartRepository;
import com.msara.servicio.domain.repositories.RoleRepository;
import com.msara.servicio.domain.repositories.UserRepository;
import com.msara.servicio.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " does not exist"));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userEntity.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList);
    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        String email = authLoginRequest.email();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(email, "User loged successfuly", accessToken, true);
    }

    public AuthResponse registerUser(AuthRegisterRequest authRegisterRequest) {
        String name = authRegisterRequest.name();
        String email = authRegisterRequest.email();
        String password = authRegisterRequest.password();
        String confirmPassword = authRegisterRequest.confirmPassword();
        String address = authRegisterRequest.address();
        List<String> roles = authRegisterRequest.roleRequest().roleListName();

        Set<RoleEntity> roleEntitySet = new HashSet<>(roleRepository.findRoleEntitiesByRoleEnumIn(roles));
        if(roleEntitySet.isEmpty()) {
            throw new IllegalArgumentException("The roles specified does not exist");
        }

        if (!password.equals(confirmPassword)) {
            throw new BadCredentialsException("Passwords are not the same");
        }

        UserEntity newUser = new UserEntity().builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .address(address)
                .isEnabled(true)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .roles(roleEntitySet)
                .build();
        UserEntity userCreated = userRepository.save(newUser);

        // Creamos el carrito luego de crear el usuario
        CartEntity cartCreated = new CartEntity().builder()
                .user(userCreated)
                .build();
        cartRepository.save(cartCreated);

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        //Setteamos los roles como un SimpleGratedAuthority para que SpringSecurity lo reconozca
        userCreated.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));

        //Setteamos las autorizaciones que tenemos para cada uno de los orles
        userCreated.getRoles()
                .stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getEmail(), userCreated.getPassword(), authorityList);
        String accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(userCreated.getEmail(), "User has been created successfuly", accessToken, true);
    }

    /**
     * Validamos que la el usuario y la contraseña existan en la BD
     * @param username en esta caso es el email
     * @param password es la contraseña
     * @return
     */
    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null)
            throw new BadCredentialsException("Invalid username or password");
        if(!passwordEncoder.matches(password, userDetails.getPassword()))
            throw new BadCredentialsException("Invalid password");

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

}
