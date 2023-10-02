package uns.ftn.projekat.svt2023.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import uns.ftn.projekat.svt2023.model.dto.*;
import uns.ftn.projekat.svt2023.model.entity.*;
import uns.ftn.projekat.svt2023.service.*;

import javax.transaction.*;
import java.util.*;

@Transactional
@RestController
@RequestMapping("api/group_requests")
public class GroupRequestController {
    GroupRequestService groupRequestService;
    @Autowired
    public GroupRequestController(GroupRequestService groupRequestService) {
        this.groupRequestService = groupRequestService;
    }
    @PostMapping("/{groupId}/send")
    public ResponseEntity<GroupRequestDTO> createGroupRequest(@PathVariable Integer groupId) {
        GroupRequest groupRequest = groupRequestService.create(groupId);

        if(groupRequest == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }

        GroupRequestDTO createdGroupRequest = new GroupRequestDTO(groupRequest);

        return new ResponseEntity<>(createdGroupRequest, HttpStatus.CREATED);
    }

    @PostMapping("/{requestId}/approve")
    public void approveGroupRequest(@PathVariable Integer requestId) {groupRequestService.approveRequest(requestId);}

    @PostMapping("/{requestId}/decline")
    public void declineGroupRequest(@PathVariable Integer requestId) {groupRequestService.declineRequest(requestId);}

}
