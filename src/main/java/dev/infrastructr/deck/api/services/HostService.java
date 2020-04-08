package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.entities.Host;
import dev.infrastructr.deck.api.exceptions.NotFoundException;
import dev.infrastructr.deck.api.mappers.HostMapper;
import dev.infrastructr.deck.api.requests.CreateHostRequest;
import dev.infrastructr.deck.data.repositories.HostRepository;
import dev.infrastructr.deck.data.repositories.ProjectRepository;
import dev.infrastructr.deck.security.authorizers.HostAuthorizer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HostService {

    private final HostRepository hostRepository;

    private final ProjectRepository projectRepository;

    private final HostMapper hostMapper;

    private final HostAuthorizer hostAuthorizer;

    public HostService(
        HostRepository hostRepository,
        HostMapper hostMapper,
        HostAuthorizer hostAuthorizer,
        ProjectRepository projectRepository
    ){
        this.hostRepository = hostRepository;
        this.hostMapper = hostMapper;
        this.hostAuthorizer = hostAuthorizer;
        this.projectRepository = projectRepository;
    }

    public Host create(CreateHostRequest createHostRequest){
        hostAuthorizer.authorizeByProjectId(createHostRequest.getProjectId());

        dev.infrastructr.deck.data.entities.Project project = projectRepository.findById(createHostRequest.getProjectId()).orElseThrow(NotFoundException::new);
        dev.infrastructr.deck.data.entities.Host host = new dev.infrastructr.deck.data.entities.Host();
        host.setProject(project);
        host.setName(createHostRequest.getName());

        return hostMapper.map(hostRepository.save(host));
    }

    public Host getById(UUID id){
        hostAuthorizer.authorizeByHostId(id);

        dev.infrastructr.deck.data.entities.Host host = hostRepository.findById(id).orElseThrow(NotFoundException::new);

        return hostMapper.map(host);
    }

    public Page<Host> getByProjectId(Pageable pageable, UUID projectId){
        hostAuthorizer.authorizeByProjectId(projectId);

        return hostRepository
            .findByProjectId(pageable, projectId)
            .map(hostMapper::map);
    }
}
