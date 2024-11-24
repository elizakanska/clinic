package ek.vetms.clinic.service;

import ek.vetms.clinic.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void saveUser(User user);
}

