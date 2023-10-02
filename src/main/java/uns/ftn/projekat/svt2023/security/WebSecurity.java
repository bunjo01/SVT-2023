package uns.ftn.projekat.svt2023.security;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import uns.ftn.projekat.svt2023.model.entity.User;
import uns.ftn.projekat.svt2023.service.*;

import javax.servlet.http.*;

@Component
public class WebSecurity {

    @Autowired
    private UserService userService;

    public boolean checkUserId(Authentication authentication, HttpServletRequest request, int id) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(userDetails.getUsername());
        if(id == user.getId()) {
            return true;
        }
        return false;
    }

}
