package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.api.services.HostHeartbeatService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/hosts")
public class HostHeartbeatController {

    private final HostHeartbeatService hostHeartbeatService;

    public HostHeartbeatController(HostHeartbeatService hostHeartbeatService){
        this.hostHeartbeatService = hostHeartbeatService;
    }

    @GetMapping("/{hostId}/heartbeat")
    public @ResponseBody Resource getByHostId(
        @PathVariable("hostId") UUID hostId,
        @RequestParam("hostToken") String hostToken
    ){
        return hostHeartbeatService.getByHostIdAndHostToken(hostId, hostToken);
    }

    @PostMapping("/{hostId}/heartbeat")
    public @ResponseBody void updateByHostId(
        @PathVariable("hostId") UUID hostId,
        @RequestParam("hostToken") String hostToken,
        @RequestBody Map<String, Object> receiveHeartbeatRequest
    ){
        hostHeartbeatService.updateByHostIdAndHostToken(hostId, hostToken, receiveHeartbeatRequest);
    }
}
