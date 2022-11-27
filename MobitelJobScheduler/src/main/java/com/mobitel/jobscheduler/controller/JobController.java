package com.mobitel.jobscheduler.controller;

import com.mobitel.jobscheduler.CommonUrl;
import com.mobitel.jobscheduler.dto.FiredJobsDTO;
import com.mobitel.jobscheduler.dto.MainJobsDTO;
import com.mobitel.jobscheduler.dto.ServiceRequestsDTO;
import com.mobitel.jobscheduler.service.MainJobsService;
import com.mobitel.jobscheduler.service.ServiceRequestService;
import com.mobitel.jobscheduler.util.generic.RequestHandler;
import com.mobitel.jobscheduler.util.generic.ResponseHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class JobController {

    private final MainJobsService mainJobsService;
    private final ServiceRequestService serviceRequestService;

    public JobController(MainJobsService mainJobsService, ServiceRequestService serviceRequestService) {
        this.mainJobsService = mainJobsService;
        this.serviceRequestService = serviceRequestService;
    }

    @GetMapping(CommonUrl.getAllMainJobs)
    public ResponseHandler<MainJobsDTO> getAllMainJobs(){
        return serviceRequestService.getJobs();
    }

    @PostMapping(CommonUrl.deleteJob)
    public ResponseHandler<String> deleteJob(@RequestBody RequestHandler<MainJobsDTO> jobDTORequestHandler){
        return serviceRequestService.deleteJob(jobDTORequestHandler);
    }

    @PostMapping(CommonUrl.deleteAllJobs)
    public ResponseHandler<String> deleteAllJobs(){
        return serviceRequestService.deleteAllJobs();
    }

    @PostMapping(CommonUrl.pauseTrigger)
    public ResponseHandler<String> pauseTrigger(@RequestBody RequestHandler<MainJobsDTO> jobDTORequestHandler){
        return serviceRequestService.pauseTrigger(jobDTORequestHandler);
    }

    @PostMapping(CommonUrl.resumeTrigger)
    public ResponseHandler<String> resumeTrigger(@RequestBody RequestHandler<MainJobsDTO> jobDTORequestHandler){
        return serviceRequestService.resumeTrigger(jobDTORequestHandler);

    }

    @PostMapping(CommonUrl.addJob)
    public ResponseHandler<String> addJob(@RequestBody RequestHandler<MainJobsDTO> jobDTORequestHandler){
        return mainJobsService.mainScheduler(jobDTORequestHandler);
    }

    @PostMapping(CommonUrl.addRequst)
    public ResponseHandler<ServiceRequestsDTO> addRequest(@RequestBody RequestHandler<ServiceRequestsDTO> serviceRequestsDTORequestHandler){
        return serviceRequestService.addRequst(serviceRequestsDTORequestHandler);
    }

    @GetMapping (CommonUrl.getFiredJobs)
    public ResponseHandler<FiredJobsDTO> getFiredJobs(@RequestParam int count){
        return serviceRequestService.getFiredJobs(count);
    }

    @GetMapping("/get")
    public ResponseEntity<String> getApiTest(){
        return ResponseEntity.ok("test pass hooo!");
    }
}
