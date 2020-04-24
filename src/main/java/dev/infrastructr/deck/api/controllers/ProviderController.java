package dev.infrastructr.deck.api.controllers;

import dev.infrastructr.deck.api.entities.Provider;
import dev.infrastructr.deck.api.requests.CreateProviderRequest;
import dev.infrastructr.deck.api.services.ProviderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    private final ProviderService providerService;

    public ProviderController(ProviderService providerService){
        this.providerService = providerService;
    }

    @PostMapping
    public @ResponseBody Provider create(@RequestBody CreateProviderRequest request) {
        return providerService.create(request);
    }

    @GetMapping
    public @ResponseBody Page<Provider> getAll(
        Pageable pageable,
        @RequestParam(value = "filter", defaultValue = "") String filter
    ){
        return providerService.getAll(pageable, filter);
    }

    @GetMapping("/{providerId}")
    public @ResponseBody Provider getById(@PathVariable("providerId") UUID providerId){
        return providerService.getById(providerId);
    }
}
