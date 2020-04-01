package dev.infrastructr.deck.security.authorizers;

import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.data.entities.Host;
import dev.infrastructr.deck.data.entities.Project;
import dev.infrastructr.deck.data.repositories.HostRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HostAuthorizer {

    private final ProjectAuthorizer projectAuthorizer;

    private final HostRepository hostRepository;

    public HostAuthorizer(
        ProjectAuthorizer projectAuthorizer,
        HostRepository hostRepository
    ){
        this.projectAuthorizer = projectAuthorizer;
        this.hostRepository = hostRepository;
    }

    public void authorizeByHostId(UUID hostId){
        Host host = hostRepository.findById(hostId).orElseThrow(NotFoundException::new);
        Project project = host.getProject();

        projectAuthorizer.authorizeByProject(project);
    }

    public void authorizeByProjectId(UUID projectId){
        projectAuthorizer.authorizeByProjectId(projectId);
    }
}
