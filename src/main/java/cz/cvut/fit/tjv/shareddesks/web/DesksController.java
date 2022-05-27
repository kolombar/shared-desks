package cz.cvut.fit.tjv.shareddesks.web;

import cz.cvut.fit.tjv.shareddesks.persistence.entity.Equipment;
import cz.cvut.fit.tjv.shareddesks.persistence.entity.SharedDesk;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.BranchRepository;
import cz.cvut.fit.tjv.shareddesks.persistence.repository.SharedDeskRepository;
import cz.cvut.fit.tjv.shareddesks.service.SharedDeskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("/desks")
public class DesksController {

    @Autowired private SharedDeskRepository sharedDeskRepository;

    @Autowired private BranchRepository branchRepository;

    @Autowired private SharedDeskService sharedDeskService;

    @GetMapping
    public String allDesks(Model model) {
        model.addAttribute("desks", sharedDeskRepository.findAll());
        model.addAttribute("equipments", Equipment.values());
        model.addAttribute("desk", new SharedDesk());
        model.addAttribute("branches", branchRepository.findAll());
        return "DesksTemplate";
    }

    @PostMapping("/add")
    public String addDesk(@ModelAttribute SharedDesk desk) {
        log.info("Saving desk. {}", desk);
        sharedDeskRepository.save(desk);
        return "redirect:/desks";
    }

    @GetMapping("/delete")
    public String deleteDesk(@RequestParam Long id) {
        log.info("Deleting desk with ID {}.", id);
        sharedDeskRepository.deleteById(id);
        return "redirect:/desks";
    }

    @RequestMapping(value = "/download", produces = "application/zip")
    public ResponseEntity<StreamingResponseBody> zipAnnotations(
            HttpServletResponse response) {

        response.addHeader("Content-Disposition", "attachment; filename=\"desks.zip\"");

        StreamingResponseBody stream =
                out -> sharedDeskService.getDesksZip(out);

        return new ResponseEntity<>(stream, HttpStatus.OK);
    }
}
