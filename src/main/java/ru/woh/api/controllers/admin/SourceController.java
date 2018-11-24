package ru.woh.api.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.woh.api.exceptions.NotFoundException;
import ru.woh.api.models.Role;
import ru.woh.api.models.Source;
import ru.woh.api.models.repositories.SourceRepository;
import ru.woh.api.services.ParserService;
import ru.woh.api.views.admin.SourceView;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SourceController {
    private final SourceRepository sourceRepository;
    private final ParserService parserService;

    @Autowired
    public SourceController(SourceRepository sourceRepository, ParserService parserService) {
        this.sourceRepository = sourceRepository;
        this.parserService = parserService;
    }

    @GetMapping("/source/")
    @RolesAllowed({Role.ROLE_ADMIN})
    public List<SourceView> list() {
        return this.sourceRepository.findAll()
            .stream()
            .map(Source::view)
            .collect(Collectors.toList());
    }

    @GetMapping("/source/{id:[0-9]*}/")
    @RolesAllowed({Role.ROLE_ADMIN})
    public SourceView byId(@PathVariable("id") Long id) {
        return this.sourceRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(
            "source #%d not found",
            id
        ))).view();
    }

    @GetMapping("/source/run/{id:[0-9]*}/")
    @RolesAllowed({Role.ROLE_ADMIN})
    public ResponseEntity run(@PathVariable("id") Long id) {
        Source source = this.sourceRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format(
            "source #%d not found",
            id
        )));

        if (this.parserService.parseSource()) {
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/source/add/")
    @RolesAllowed({Role.ROLE_ADMIN})
    public SourceView add(@RequestBody SourceView sourceView) {
        Source source = sourceView.model();
        source.setId(null);

        return this.sourceRepository.save(source).view();
    }

    @PostMapping("/source/edit/")
    @RolesAllowed({Role.ROLE_ADMIN})
    public SourceView edit(@RequestBody SourceView sourceView) {
        if (sourceView.getId() == null || !this.sourceRepository.existsById(sourceView.getId())) {
            throw new NotFoundException("source not found");
        }

        return this.sourceRepository.save(sourceView.model()).view();
    }

    @PostMapping("/source/delete/{id:[0-9]*}/")
    @RolesAllowed({Role.ROLE_ADMIN})
    public ResponseEntity delete(@PathVariable("id") Long id) {
        if (!this.sourceRepository.existsById(id)) {
            throw new NotFoundException("source not found");
        }

        this.sourceRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
