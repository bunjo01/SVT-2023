package uns.ftn.projekat.svt2023.controller;

import org.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;
import uns.ftn.projekat.svt2023.model.entity.User;
import uns.ftn.projekat.svt2023.repository.*;
import uns.ftn.projekat.svt2023.security.*;
import uns.ftn.projekat.svt2023.service.*;
import uns.ftn.projekat.svt2023.service.implementation.*;

import javax.persistence.criteria.*;
import javax.servlet.http.*;
import javax.transaction.*;
import java.security.*;
import java.time.*;
import java.util.*;

@Transactional
@RestController
@RequestMapping("api/users")
public class UserController
{
    ReactionService reactionService;
    UserService userService;
    FriendRequestService friendRequestService;
    UserDetailsService userDetailsService;

    @Autowired
    public UserController(UserServiceImpl userService, UserDetailsService userDetailsService,
                          ReactionService reactionService, FriendRequestService friendRequestService){
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.reactionService = reactionService;
        this.friendRequestService = friendRequestService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> create(@RequestBody @Validated UserDTO newUser) {
        User createdUser = userService.create(newUser);

        if(createdUser == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        UserDTO userDTO = new UserDTO(createdUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/changeProfile")
    public ResponseEntity<UserProfileDTO> changeProfile(@RequestBody @Validated UserProfileDTO newUserData) {
        User changedUser = userService.changeProfile(newUserData);

        if(changedUser == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        UserProfileDTO userDTO = new UserProfileDTO(changedUser);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{userUsername}/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable String userUsername) {

        User user = userService.findByUsername(userUsername);

        if(user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        UserDTO userDTO = new UserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable Integer userId) {

        User user = userService.findOne(userId);

        if(user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        UserDTO userDTO = new UserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response){
        return userService.createAuthenticationToken(authenticationRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        User user = userService.findOne(id);

        if(user != null) {
            userService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> edit(@RequestBody UserDTO editUserDTO) {
        User user = userService.edit(editUserDTO);

        if(user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        UserDTO userDTO = new UserDTO(user);

        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PutMapping("/changePassword")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDTO> changePassword (@RequestBody @Validated PasswordDTO passwords) {
        return userService.changePassword(passwords);
    }

    @PutMapping("/changeImage")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String>  changeImage (@RequestBody Map<String, String> requestBody) {
        try {
            String filePath = requestBody.get("filePath");
            Image imageDTO = new Image();
            imageDTO.setPath("../../../assets/images/" + filePath);

            userService.changeImage(imageDTO);

            return ResponseEntity.ok("File path received successfully: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the file path.");
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.userService.getAll();
    }

    @GetMapping("/whoami")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public User user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }

    @GetMapping("/profileImage")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Image getUserImage() {
        return this.userService.getUserImage();
    }

    @GetMapping("/{userId}/posts")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Post> getUserPosts(@PathVariable Integer userId) {
        return userService.getUserPosts(userId);
    }

    @GetMapping("/posts")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Post> getNonGoupPosts() {
        return userService.getNonGroupPosts();
    }

    @GetMapping("/{userId}/groups")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<Group> getUserGroups(@PathVariable Integer userId) {
        return userService.getUserGroups(userId);
    }

    @GetMapping("/{userId}/friends")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<User> getAllFriends(@PathVariable Integer userId) {
        return userService.getAllFriends(userId);
    }

    @GetMapping("/{userId}/requests")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<FriendRequest> getUserRequests(@PathVariable Integer userId) {return userService.getAllUserFriendRequests(userId);}

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Set<User> searchUser(@RequestParam String name) {
        return userService.searchUsersByName(name);
    }
}
