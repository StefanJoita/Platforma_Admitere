package aplicatie_admitere.servicies;

import aplicatie_admitere.models.MyUserDetails;
import aplicatie_admitere.models.User;
import aplicatie_admitere.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    // Gets user by user email
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        user.orElseThrow(() -> new UsernameNotFoundException("NOT found: " + email));

        return user.map(MyUserDetails::new).get();

    }

    public UserDetails getUserById(Long userId) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(userId);

        user.orElseThrow(() -> new UsernameNotFoundException("NOT found: " + userId));

        return user.map(MyUserDetails::new).get();

    }
    public UserDetails getUserByRoles(String role) throws UsernameNotFoundException
    {
        Optional<User> user = userRepository.findUserByRoles(role);

        user.orElseThrow(() -> new UsernameNotFoundException("NOT found: " + role));

        return user.map(MyUserDetails::new).get();
    }
    public boolean exist(String email){
        return userRepository.existsByEmail(email);
    }

}
