package ru.woh.api.controllers.site;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorsController {
    @RequestMapping(method = RequestMethod.OPTIONS, value = "/**")
    public ResponseEntity options() {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
