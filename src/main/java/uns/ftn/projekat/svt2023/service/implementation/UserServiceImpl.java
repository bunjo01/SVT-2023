package uns.ftn.projekat.svt2023.service.implementation;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;
import uns.ftn.projekat.svt2023.model.entity.User;
import uns.ftn.projekat.svt2023.model.enums.*;
import uns.ftn.projekat.svt2023.repository.*;
import uns.ftn.projekat.svt2023.security.*;
import uns.ftn.projekat.svt2023.service.*;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    @Lazy
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    @Lazy
    private GroupService groupService;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;
    @Autowired
    @Lazy
    private TokenUtils tokenUtils;
    @Autowired
    @Lazy
    private PasswordEncoder encoder;
    @Autowired
    private ImageRepository imageRepository;

    @Override
    public User findByUsername(String username) {

        Optional<User> user = userRepository.findFirstByUsername(username);

        if(!user.isEmpty()){
            return user.get();
        }
        return null;
    }

    @Override
    public Set<User> searchUsersByName(String name) {
        return userRepository.searchUsersByName(name);
    }

    @Override
    public User returnLoggedUser() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return this.findByUsername(a.getName());
    }

    @Override
    public Set<User> getAllFriends(Integer userId) {
        User user = this.findOne(userId);

        Set<User> firstSet = userRepository.getAllFriends(user.getId());
        Set<User> secondSet = userRepository.getAllFriends1(user.getId());

        Set<User> mergedSet = new HashSet<>();
        mergedSet.addAll(firstSet);
        mergedSet.addAll(secondSet);
        mergedSet.remove(user);

        return mergedSet;
    }

    @Override
    public ResponseEntity<UserTokenState> createAuthenticationToken(JwtAuthenticationRequest authenticationRequest) {
        User loginUser = findByUsername(authenticationRequest.getUsername());
        if(!loginUser.getSuspended()) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails user = (UserDetails) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user);
            int expiresIm = tokenUtils.getExpiredIn();

            return ResponseEntity.ok(new UserTokenState(jwt, expiresIm));
        }
        return null;
    }

    @Override
    public ResponseEntity<UserDTO> changePassword(PasswordDTO passwords) {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        User foundUser = this.findByUsername(a.getName());

        if(encoder.matches(passwords.getCurrent(), foundUser.getPassword()) && passwords.getConfirm().equals(passwords.getPassword())){
            foundUser.setPassword(encoder.encode(passwords.getPassword()));
            this.save(foundUser);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        UserDTO userDTO = new UserDTO(foundUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @Override
    public Set<Post> getUserPosts(Integer userId) {
        User user = this.findOne(userId);
        return postRepository.findByUser(user);
    }

    @Override
    public Set<Post> getNonGroupPosts() {
        return userRepository.getNonGroupPosts();
    }

    @Override
    public Set<Group> getUserGroups(Integer userId) {
        return groupService.findUserGroups(userId);
    }

    @Override
    public Set<FriendRequest> getAllUserFriendRequests(Integer userId) {
        return friendRequestRepository.getAllUserFriendRequests(userId);
    }

    @Override
    public void suspendUser(String username) {
        userRepository.suspendUser(username);
    }

    @Override
    public void changeImage(Image image) {
        User user = returnLoggedUser();

        image.setUser(user);
        imageRepository.save(image);

        user.setImage(image);
        save(user);
    }

    @Override
    public Image getUserImage() {
        User user = returnLoggedUser();
        Image image = imageRepository.getUserImage(user.getId());
        return image;
    }

    @Override
    public User create(UserDTO userDTO) {

        Optional<User> user = userRepository.findFirstByUsername(userDTO.getUsername());

        if(user.isPresent()){
            return null;
        }

        User newUser = new User();
        newUser.setSuspended(false);
        newUser.setUsername(userDTO.getUsername());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setRole(Roles.USER);
        newUser.setDescription("");
        newUser = userRepository.save(newUser);

        return newUser;
    }

    @Override
    public User edit(UserDTO userDTO) {
        return null;
    }

    @Override
    public User changeProfile(UserProfileDTO userProfileDTO) {
        User user = this.returnLoggedUser();

        user.setLastName(userProfileDTO.getLastName());
        user.setFirstName(userProfileDTO.getFirstName());
        user.setDescription(userProfileDTO.getDescription());

        user = userRepository.save(user);

        return user;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User findOne(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NullPointerException("User not found with id: " + id));
    }

    @Override
    public void delete(Integer id) {userRepository.deleteById(id);}

}
