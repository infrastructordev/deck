package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.api.entities.HostInit;
import dev.infrastructr.deck.api.services.HostInitService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/hosts")
public class HostInitController {

    private final HostInitService hostInitService;

    public HostInitController(HostInitService hostInitService){
        this.hostInitService = hostInitService;
    }

    @PostMapping("/{hostId}/init")
    public @ResponseBody HostInit createByHostId(@PathVariable("hostId") UUID hostId){
        return hostInitService.createByHostId(hostId);
    }

    @GetMapping(
        value = "/{hostId}/init",
        produces = "application/x-sh"
    )
    public @ResponseBody Resource getByHostIdAndHostToken(
        @PathVariable("hostId") UUID hostId,
        @RequestParam("hostToken") String hostToken) {
        return hostInitService.getInitByIdAndHostToken(hostId, hostToken);
    }
}
