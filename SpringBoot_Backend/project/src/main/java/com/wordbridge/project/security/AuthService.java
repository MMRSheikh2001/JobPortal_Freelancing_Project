package com.wordbridge.project.security;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.exception.InvalidCredentialException;
import com.wordbridge.project.repository.CompanyProfileRepository;
import com.wordbridge.project.repository.UserProfileRepository;
import com.wordbridge.project.repository.UserRepository;
import com.wordbridge.project.util.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final CompanyProfileRepository companyProfileRepository;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final PasswordEncoder encoder;

    public LoginResponseDTO login(LoginRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(),
                            dto.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new InvalidCredentialException("Invalid email or password.");
        }

        User user = userRepository.findByEmailIgnoreCase(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("No User found"));

        Long profileId = null;
        String displayName = "";
        String image = "";
        if (user.getRole() == UserRole.USER) {
            profileId = userProfileRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("No user profile found")).getId();
            displayName = userProfileRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("No user profile found")).getName();
            image = userProfileRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("No user profile found")).getImage();
        }
        if (user.getRole() == UserRole.COMPANY) {
            profileId = companyProfileRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("No user profile found")).getId();
            displayName = companyProfileRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("No user profile found")).getName();
            image = companyProfileRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("No user profile found")).getImage();
        }


        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        LoginResponseDTO response = new LoginResponseDTO();

        response.setToken(token);
        response.setTokenType("Bearer");

        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setProfileId(profileId);
        response.setDisplayName(displayName);
        response.setImage(image);

        return response;

    }

    //Send/Resend Verification Mail
    public void sendVerificationEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found with" + email));

        if (user.getIsActive()) {
            throw new RuntimeException("Account is already Verified");
        }
        String token = jwtUtil.generateVerificationToken(user.getEmail());
        try {
            String name;

            if (user.getRole() == UserRole.USER) {
                name = user.getUserProfile().getName();
            } else if (user.getRole() == UserRole.COMPANY) {
                name = user.getCompanyProfile().getName();
            } else {
                name = "Admin";
            }

            emailService.sendVerificationEmail(user.getEmail(), name, token);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send Verification Email   " + e.getMessage());
        }


    }

    //Check and confirm Verification Link
    public void verifyEmail(String token) {
        if (!jwtUtil.isValidForPurpose(token, "EMAIL_VERIFICATION")) {
            throw new RuntimeException("Invalid token");


        }
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("No user found"));
        if (user.getIsActive()) {
            throw new RuntimeException("Account is already Verified");
        }
        user.setIsActive(true);
        user.setIsVerified(true);
        userRepository.save(user);
    }

    //Forgot Password----Send Reset Link
    public void forgotPassword(ForgotPasswordRequestDTO dto) {

        User user = userRepository.findByEmailIgnoreCase(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not found by" + dto.getEmail()));
        String token = jwtUtil.generatePasswordResetToken(user.getEmail());
        try {
            emailService.sendPasswordResetEmail(user.getEmail(), "User", token);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send Reset email    " + e.getMessage());
        }


    }

    //Reset Password Using Token
    public void resetPasswordUsingToken(ResetPasswordRequestDTO dto) {
        if (!jwtUtil.isValidForPurpose(dto.getToken(), "PASSWORD_RESET")) {
            throw new RuntimeException("Invali/Expired Link");
        }
        if (dto.getNewPassword() == null || dto.getNewPassword().length() < 4) {
            throw new RuntimeException("Give a more Complex Password of atleast 4 characters ");
        }

        String email = jwtUtil.extractEmail(dto.getToken());
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User Not found"));
        user.setPassword(encoder.encode(dto.getNewPassword()));
        userRepository.save(user);

    }


}
