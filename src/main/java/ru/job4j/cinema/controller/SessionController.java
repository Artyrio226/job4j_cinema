package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.service.session.SessionService;

import javax.servlet.http.HttpServletRequest;

@ThreadSafe
@RequestMapping("/sessions")
@Controller
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("filmSessions", sessionService.findAll());
        return "sessions/list";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable int id, HttpServletRequest request) {
        var sessionOptional = sessionService.findById(id);
        if (sessionOptional.isEmpty()) {
            model.addAttribute("message", "Сеанс с указанным идентификатором не найден");
            return "errors/404";
        }
        model.addAttribute("filmSession", sessionOptional.get());
        request.getSession().setAttribute("sessionDto", sessionOptional.get());
        return "sessions/one";
    }
}
