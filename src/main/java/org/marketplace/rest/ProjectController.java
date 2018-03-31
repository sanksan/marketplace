package org.marketplace.rest;

import lombok.extern.slf4j.Slf4j;
import org.marketplace.dto.ProjectDetail;
import org.marketplace.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired private ProjectService projectService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<ProjectDetail> getProjects() {
        return projectService.getProjects();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ProjectDetail postNewProject(@Valid @RequestBody ProjectDetail projectDetail) {
        log.debug("postNewProject() - projectDetail="+ projectDetail);
        return projectService.postNewProject(projectDetail);
    }

    @RequestMapping(value = "/{projectid}", method = RequestMethod.PUT)
    public ProjectDetail updateProject(@PathVariable(value = "projectid") Long projectid,
                                        @Valid @RequestBody ProjectDetail projectDetail) {
        log.info("updateProject() - updating project with "+ projectid + "\tprojectDetail="+ projectDetail);
        return projectService.updateProject(projectid, projectDetail);
    }

    @RequestMapping(value = "/{projectid}", method = RequestMethod.GET)
    public ProjectDetail getProject(@PathVariable(value = "projectid") Long projectid) {
        log.debug("getProject() - projectid="+projectid);
        return projectService.getProject(projectid);
    }

    @RequestMapping(value = "/{projectid}", method = RequestMethod.DELETE)
    public void deleteProject(@PathVariable(value = "projectid") Long projectid) {
        log.debug("deleteProject() - projectid="+projectid);
        projectService.deleteProject(projectid);
    }

}