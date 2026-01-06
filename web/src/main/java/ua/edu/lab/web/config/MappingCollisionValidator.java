package ua.edu.lab.web.config;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validator that runs on application startup to ensure no duplicate mappings
 * exist for critical Lab 6 HTML endpoints.
 * Throws IllegalStateException if any collision is detected.
 */
@Component
public class MappingCollisionValidator {

    private final RequestMappingHandlerMapping handlerMapping;

    public MappingCollisionValidator(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @PostConstruct
    public void validateMappings() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        
        // Check critical paths
        checkMapping(handlerMethods, RequestMethod.GET, "/books", "GET /books");
        checkMapping(handlerMethods, RequestMethod.GET, "/books/add", "GET /books/add");
        checkMapping(handlerMethods, RequestMethod.POST, "/books/add", "POST /books/add");
    }

    private void checkMapping(Map<RequestMappingInfo, HandlerMethod> handlerMethods,
                              RequestMethod method, String path, String description) {
        List<HandlerMethod> matchingHandlers = handlerMethods.entrySet().stream()
                .filter(entry -> {
                    RequestMappingInfo info = entry.getKey();
                    Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
                    Set<String> patterns = info.getPatternValues();
                    
                    boolean methodMatches = methods.isEmpty() || methods.contains(method);
                    boolean pathMatches = patterns.contains(path);
                    
                    return methodMatches && pathMatches;
                })
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        if (matchingHandlers.isEmpty()) {
            throw new IllegalStateException(
                    "No handler found for " + description + ". This endpoint is required for Lab 6."
            );
        }
        
        if (matchingHandlers.size() > 1) {
            String handlerInfo = matchingHandlers.stream()
                    .map(hm -> hm.getBeanType().getSimpleName() + "." + hm.getMethod().getName())
                    .collect(Collectors.joining(", "));
            throw new IllegalStateException(
                    "Ambiguous mapping detected for " + description + ". Found " + matchingHandlers.size() +
                            " handlers: " + handlerInfo + ". Only ONE handler is allowed for Lab 6 HTML endpoints."
            );
        }
    }
}

