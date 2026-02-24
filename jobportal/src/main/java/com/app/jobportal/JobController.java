package com.app.jobportal;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.app.jobportal.model.Job;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
public class JobController {

    private ArrayList<Job> jobsList;

    @PostConstruct
    public void init() {
        Job job1 = new Job("1","Integration Support Engineer","Integrated 3rd party application and solutioning", LocalDate.now());
        Job job2 = new Job("2", "Data Scientist", "Analyze data and develop models", LocalDate.now());
        jobsList = new ArrayList<Job>();
        jobsList.add(job1);
        jobsList.add(job2);
        jobsList.add(new Job(
                "3",
                "Backend Java Developer",
                "Develop REST APIs using Spring Boot and Hibernate",
                LocalDate.of(2026, 2, 1)
        ));

        jobsList.add(new Job(
                "4",
                "Frontend Developer",
                "Build responsive UI using React and Tailwind CSS",
                LocalDate.of(2026, 2, 5)
        ));

        jobsList.add(new Job(
                "5",
                "DevOps Engineer",
                "Manage CI/CD pipelines and deploy applications on AWS",
                LocalDate.of(2026, 2, 10)
        ));

        jobsList.add(new Job(
                "6",
                "Product Manager",
                "Define product roadmap and coordinate between business and tech teams",
                LocalDate.of(2026, 2, 12)
        ));

        jobsList.add(new Job(
                "7",
                "QA Automation Engineer",
                "Write automation test scripts using Selenium and JUnit",
                LocalDate.of(2026, 2, 14)
        ));
    }
    @GetMapping("/jobs")
    public ArrayList<Job> listJobs() {
        for(Job job: jobsList) {
            job.setLastAccessDate(LocalDateTime.now());
            job.addNumView();
        }
        return jobsList;
    }
    @GetMapping("/jobs/search/{query}")
    public ArrayList<Job> searchJobs(@PathVariable String query) {
        ArrayList<Job> matchingJob = new ArrayList<>();
        for(Job job : jobsList) {
            if(job.getTitle().toLowerCase().contains(query.toLowerCase())
                || job.getDescription().toLowerCase().contains(query.toLowerCase())){
                matchingJob.add(job);
            }
        }
        return matchingJob;
    }
}
