package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.dto.SessionDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/buy")
    public String register(Model model, HttpSession session,
                           @ModelAttribute Ticket ticket) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            return "/users/login";
        }
        var sessionDto = (SessionDto) session.getAttribute("sessionDto");
        var foundTicket = ticketService.findUniqueTicket(sessionDto.getId(), ticket.getRowNumber(), ticket.getPlaceNumber());
        if (foundTicket.isEmpty()) {
            model.addAttribute("message",
                    "Не удалось приобрести билет на заданное место. "
                            + "Вероятно оно уже занято. Перейдите на страницу бронирования билетов "
                            + "и попробуйте снова.");
            return "tickets/failure";
        }
        var savedTicket = ticketService.save(ticket, sessionDto.getId(), user.getId());
        model.addAttribute("message",
                String.format("Вы успешно приобрели билет. Ряд: %s, место: %s",
                        savedTicket.getRowNumber(),
                        savedTicket.getPlaceNumber()));
        return "tickets/success";
    }
}
