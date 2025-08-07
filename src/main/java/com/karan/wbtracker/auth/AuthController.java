package com.karan.wbtracker.auth;


import com.karan.wbtracker.user.User;
import com.karan.wbtracker.user.UserRepository;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.karan.wbtracker.user.UserDto;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest authRequest) {
        // This part correctly validates the user's credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        // 1. Load the UserDetails object, which is what generateToken now expects
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());

        // 2. Generate the token using the UserDetails object
        final String token = jwtUtil.generateToken(userDetails);

        // 3. Fetch the User entity to build the DTO for the response body
        final User user = userRepository.findByEmail(authRequest.getEmail()).get();
        UserDto userDto = new UserDto(user);

        // 4. Return the token and the UserDto
        return ResponseEntity.ok(new AuthResponse(token, userDto));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest authRequest) {
        if (userRepository.findByEmail(authRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() { // <-- Change return type
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Convert the User entity to a UserDto before returning
        UserDto userDto = new UserDto(user);
        return ResponseEntity.ok(userDto);
    }
}


// You'll also need a simple request object. Create this class in the `auth` package.
@Setter
@Getter
@Data
class AuthRequest {
    // Getters and Setters
    private String username;
    private String email;
    private String password;

}