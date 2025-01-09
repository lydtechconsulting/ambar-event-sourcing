package eventsourcing.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class DockerHealthcheckEndpoint {
    @GetMapping("/docker_healthcheck")
    @ResponseStatus(HttpStatus.OK)
    public String dockerHealthcheck() {
        return "OK";
    }
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String rootHealthcheck() {
        return "OK";
    }
}

