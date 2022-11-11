package com.mobitel.jobscheduler.controller;

import com.mobitel.jobscheduler.CommonUrl;
import com.mobitel.jobscheduler.dto.JobDTO;
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
//    public ResponseHandler<JobDTO> getAllMainJobs(){
//        try{
//
//        }
//        catch (){
//
//        }
//    }

    @PostMapping(CommonUrl.deleteJob)
    public ResponseHandler<String> deleteJob(@RequestBody RequestHandler<JobDTO> jobDTORequestHandler){
        return serviceRequestService.deleteJob(jobDTORequestHandler);
    }

    @PostMapping(CommonUrl.deleteAllJobs)
    public ResponseHandler<String> deleteAllJobs(){
        return serviceRequestService.deleteAllJobs();
    }

    @PostMapping(CommonUrl.pauseTrigger)
    public ResponseHandler<String> pauseTrigger(@RequestBody RequestHandler<JobDTO> jobDTORequestHandler){
        return serviceRequestService.pauseTrigger(jobDTORequestHandler);
    }

    @PostMapping(CommonUrl.resumeTrigger)
    public ResponseHandler<String> resumeTrigger(@RequestBody RequestHandler<JobDTO> jobDTORequestHandler){
        return serviceRequestService.resumeTrigger(jobDTORequestHandler);

    }

    @PostMapping(CommonUrl.addRequst)
    public ResponseHandler<String> addRequst(@RequestBody RequestHandler<JobDTO> jobDTORequestHandler){
        return mainJobsService.mainScheduler(jobDTORequestHandler);
    }

    @GetMapping (CommonUrl.getFiredJobs)
    public ResponseEntity<List<JobDetails>> getFiredJobs(@RequestParam int count){
        return ResponseEntity.ok().body(srService.getFiredJobs(count));
    }

    @GetMapping("/get")
    public ResponseEntity<String> getApiTest(){
        return ResponseEntity.ok("test pass 1234");
    }
}
