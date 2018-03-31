package org.marketplace.service;

import lombok.extern.slf4j.Slf4j;
import org.marketplace.domain.Project;
import org.marketplace.domain.User;
import org.marketplace.dto.BidDetail;
import org.marketplace.dto.ProjectDetail;
import org.marketplace.exception.NotFoundException;
import org.marketplace.repository.ProjectRepository;
import org.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public ProjectService() {
    }

    public List<ProjectDetail> getProjects() {
        Iterable<Project> projectIterable = projectRepository.findAll();
        return StreamSupport.stream(projectIterable.spliterator(), false)
                .map(ProjectDetail::toProjectDto)
                .collect(Collectors.toList());
    }

    public ProjectDetail postNewProject(ProjectDetail projectDetail) {
        log.debug("updateProject() - projectDetail="+projectDetail);
        Project projectModel = ProjectDetail.fromProjectDto(projectDetail);
        Optional<User> user = userRepository.findById(projectDetail.getUserId());
        if(!user.isPresent())
            throw new NotFoundException("Invalid user id "+ projectDetail.getUserId());

        User userModel = user.get();
        projectModel.setUser(userModel);
        Project persistentProject = projectRepository.save(projectModel);
        return ProjectDetail.toProjectDto(persistentProject);
    }


    public ProjectDetail updateProject(Long projectid, ProjectDetail projectDetail) {
        Project updtProject = ProjectDetail.fromProjectDto(projectDetail);
        Optional<Project> project = projectRepository.findById(projectid);
        if(project.isPresent()) {
            Project projectModel = project.get();
            projectModel.setName(projectDetail.getName());
            projectModel.setDescription(projectDetail.getDescription());
            projectModel.setMaxAmount(projectDetail.getMaxAmount());
            projectModel.setStartDate(projectDetail.getStartDate());
            projectModel.setDeadline(projectDetail.getDeadline());
            projectModel.setStatus(projectDetail.getStatus());
            Project persistentProject = projectRepository.save(projectModel);
            return ProjectDetail.toProjectDto(persistentProject);
        } else
            throw new NotFoundException(String.format("Project with id %s doesn't exist", projectid));
    }

    public ProjectDetail getProject(Long projectid) {
        log.debug("getProject() - projectid="+projectid);
        Optional<Project> optProject = projectRepository.findById(projectid);
        if(optProject.isPresent())
            return ProjectDetail.toProjectDto(optProject.get());
        else
            throw new NotFoundException(String.format("Project with id %s doesn't exist", projectid));
    }

    public void deleteProject(Long projectid) {
        log.debug("deleteProject() - projectid="+projectid);
        Optional<Project> optProject = projectRepository.findById(projectid);
        if(!optProject.isPresent())
            throw new NotFoundException(String.format("Project with id %s doesn't exist", projectid));
        projectRepository.deleteById(projectid);
    }

}