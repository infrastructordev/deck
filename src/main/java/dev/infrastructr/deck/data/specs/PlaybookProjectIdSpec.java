package dev.infrastructr.deck.data.specs;

import dev.infrastructr.deck.data.entities.Playbook;

import java.util.UUID;

public class PlaybookProjectIdSpec extends IdSpec<Playbook> {

    public PlaybookProjectIdSpec(UUID projectId){
        super(projectId, "project");
    }
}
