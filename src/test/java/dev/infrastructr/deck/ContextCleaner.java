package dev.infrastructr.deck;

import dev.infrastructr.deck.api.models.TestContext;
import dev.infrastructr.deck.git.GitProjectRemover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContextCleaner {

    @Autowired
    private GitProjectRemover gitProjectRemover;

    public void clean(TestContext context){
        gitProjectRemover.delete(context);
    }
}
