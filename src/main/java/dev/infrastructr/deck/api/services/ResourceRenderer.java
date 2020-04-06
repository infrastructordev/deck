package dev.infrastructr.deck.api.services;

import dev.infrastructr.deck.api.exceptions.ResourceRenderingException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.Resource;
import org.springframework.security.util.InMemoryResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class ResourceRenderer {

    private static final String TEMPLATE_PREFIX = "{{";

    private static final String TEMPLATE_SUFFIX = "}}";

    public Resource render(Resource resource, Map<String, Object> context){
        try {
            String content = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
            String renderedContent = StringSubstitutor.replace(content, context, TEMPLATE_PREFIX, TEMPLATE_SUFFIX);
            return new InMemoryResource(renderedContent);
        } catch(IOException e){
            throw new ResourceRenderingException("Resource rendering has failed.",  e);
        }
    }
}
