package ek.vetms.clinic.service.impl;
import ek.vetms.clinic.entity.Authority;
import ek.vetms.clinic.entity.User;
import ek.vetms.clinic.repository.AuthorityRepository;
import ek.vetms.clinic.repository.UserRepository;
import ek.vetms.clinic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(User user) {
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        // Save the user in the `users` table
        userRepository.save(user);

        // Assign default role (e.g., ROLE_USER)
        Authority authority = new Authority();
        authority.setUsername(user.getUsername());
        authority.setAuthority("ROLE_USER");
        authorityRepository.save(authority);
    }
}

