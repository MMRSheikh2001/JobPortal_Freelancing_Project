package com.wordbridge.project.serviceimpl;


import com.wordbridge.project.admin.UserSearchRequestDTO;
import com.wordbridge.project.admin.UserSpecification;
import com.wordbridge.project.dto.mapper.UserMapper;
import com.wordbridge.project.dto.requestdto.UserRequestDTO;
import com.wordbridge.project.dto.responsedto.UserResponseDTO;
import com.wordbridge.project.entity.CompanyProfile;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.entity.UserProfile;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.repository.CompanyProfileRepository;
import com.wordbridge.project.repository.UserProfileRepository;
import com.wordbridge.project.repository.UserRepository;
import com.wordbridge.project.security.AuthService;
import com.wordbridge.project.service.UserService;
import com.wordbridge.project.util.EmailService;
import com.wordbridge.project.wallet.WalletService;
import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final CompanyProfileRepository companyProfileRepository;
    private final WalletService walletService;
    private final AuthService authService;
    private final PasswordEncoder encoder;


    private final EmailService emailService;


    private final UserMapper userMapper;


    @Override
    @Transactional
    public UserResponseDTO register(UserRequestDTO dto) {


        if (userRepository.existsByEmail(dto.getEmail().trim().toLowerCase())) {
            throw new RuntimeException("Email already exists");

        }

        User user = userMapper.toEntity(dto);
        user.setPassword(encoder.encode(dto.getPassword()));//Encoding Password
        user.setIsVerified(false);

        user.setIsActive(false);

        user.setIsSuspended(false);

        User savedUser = userRepository.save(user);
        if (savedUser.getRole() == UserRole.USER) {

            UserProfile profile = new UserProfile();

            profile.setUser(savedUser);
            profile.setName(dto.getFullName());

            profile.setProfileCompleted(false);

            userProfileRepository.save(profile);
            user.setUserProfile(profile);
        }

        if (savedUser.getRole() == UserRole.COMPANY) {

            CompanyProfile company = new CompanyProfile();

            company.setUser(savedUser);

            company.setName(dto.getFullName());


            companyProfileRepository.save(company);
            user.setCompanyProfile(company);
        }


        authService.sendVerificationEmail(savedUser.getEmail());

        walletService.createWallet(savedUser.getId());

        return userMapper.toDTO(savedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found by this Id"));
        return userMapper.toDTO(user);
    }


    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user found"));

        if (user.getRole() == UserRole.ADMIN) {
            throw new RuntimeException("Admin can't be deleted");
        }

        userRepository.delete(user);
    }

    @Override
    public Long countAllUsers() {
        return userRepository.count();
    }

    @Override
    public UserResponseDTO toggleSuspendedStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No user found"));

        if (user.getRole() == UserRole.ADMIN) {
            throw new RuntimeException("Admin can't be suspened");
        }

        if (user.getIsSuspended()) {
            user.setIsSuspended(false);
        } else {
            user.setIsSuspended(true);
        }
        User savedUser = userRepository.save(user);
        UserResponseDTO userResponseDTO = userMapper.toDTO(savedUser);

        sendMailToUser(userResponseDTO);
        return userResponseDTO;

    }


    @Override
    public List<UserResponseDTO> search(
            UserSearchRequestDTO request
    ) {

        Specification<User> specification =
                UserSpecification.search(request);

        return userRepository
                .findAll(specification)
                .stream()
                .map(userMapper::toDTO)
                .toList();

    }


    //Email Sending to User After suspending them


    public void sendMailToUser(UserResponseDTO u) {
        String subject = "Suspension from Workbridge";

        String mailText = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "  body { font-family: Arial, sans-serif; line-height: 1.6; }"
                + "  .container { max-width: 600px; margin: auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 10px; }"
                + "  .header { background-color: #4CAF50; color: white; padding: 10px; text-align: center; border-radius: 10px 10px 0 0; }"
                + "  .content { padding: 20px; }"
                + "  .footer { font-size: 0.9em; color: #777; margin-top: 20px; text-align: center; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "  <div class='container'>"
                + "    <div class='header'>"
                + "      <h2>SuspensionNotice</h2>"
                + "    </div>"
                + "    <div class='content'>"
                + "      <p>Dear " + u.getName() + ",</p>"
                + "      <p>You have been suspend from our service</p>"
                + "      <p>Please contact admin</p>"
                + "      <p>If you have any questions or need help, feel free to reach out to our support team.</p>"
                + "      <br>"
                + "      <p>Best regards,<br>The Support Team</p>"


                + "    </div>"
                + "    <div class='footer'>"
                + "      &copy; " + java.time.Year.now() + " WorkBridge. All rights reserved."
                + "    </div>"
                + "  </div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendSimpleMail(u.getEmail(), subject, mailText);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }
}
