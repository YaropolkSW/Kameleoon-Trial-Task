package com.kameleoon.kameleoontrialtask.controller;

import com.kameleoon.kameleoontrialtask.dto.QuoteDTO;
import com.kameleoon.kameleoontrialtask.service.QuoteService;
import com.kameleoon.kameleoontrialtask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
public class QuoteController {
    private final QuoteService quoteService;
    private final UserService userService;

    @Autowired
    public QuoteController(
        final QuoteService quoteService,
        final UserService userService
    ) {
        this.quoteService = quoteService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String firstPage(final Model model) {
        final String username = userService.getUsernameFromSecurityContextHolder();

        if (username != null) {
            model.addAttribute("user", userService.findByUsername(username));
        }


        model.addAttribute("allQuotes", quoteService.getAllQuotes());
        model.addAttribute("randomQuote", quoteService.getRandomQuote());
        model.addAttribute("username", username);

        return "index";
    }

    @GetMapping("/top_10_quotes")
    public String getTop10Quotes(final Model model) {
        final String username = userService.getUsernameFromSecurityContextHolder();

        if (username != null) {
            model.addAttribute("logged", userService.findByUsername(username));
        }

        model.addAttribute("top10", quoteService.getTop10Quotes());

        return "top-10-quotes";
    }

    @GetMapping("/flop_10_quotes")
    public String getFlop10Quotes(final Model model) {
        final String username = userService.getUsernameFromSecurityContextHolder();

        if (username != null) {
            model.addAttribute("logged", userService.findByUsername(username));
        }

        model.addAttribute("flop10", quoteService.getFlop10Quotes());

        return "flop-10-quotes";
    }

    @GetMapping("/last_quote")
    public String getLastQuote(final Model model) {
        final String username = userService.getUsernameFromSecurityContextHolder();

        if (username != null) {
            model.addAttribute("logged", userService.findByUsername(username));
        }

        model.addAttribute("last", quoteService.getLastQuote());

        return "last-quote";
    }

    @GetMapping("/{from}/upvote/{quoteId}")
    public String upvote(
        @PathVariable final String from,
        @PathVariable final int quoteId
    ) {
        final QuoteDTO quoteDTO = quoteService.findQuoteById(quoteId);

        quoteDTO.setDateOfCreation(LocalDateTime.now());
        quoteDTO.setVotes(quoteDTO.getVotes() + 1);

        quoteService.save(quoteDTO);

        if (from.equals("last")) {
            return "redirect:/last_quote";
        } else if (from.equals("top")) {
            return "redirect:/top_10_quotes";
        } else if (from.equals("flop")) {
            return "redirect:/flop_10_quotes";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/{from}/downvote/{quoteId}")
    public String downvote(
        @PathVariable final String from,
        @PathVariable final int quoteId
    ) {
        final QuoteDTO quoteDTO = quoteService.findQuoteById(quoteId);

        quoteDTO.setDateOfCreation(LocalDateTime.now());
        quoteDTO.setVotes(quoteDTO.getVotes() - 1);

        quoteService.save(quoteDTO);

        if (from.equals("last")) {
            return "redirect:/last_quote";
        } else if (from.equals("top")) {
            return "redirect:/top_10_quotes";
        } else if (from.equals("flop")) {
            return "redirect:/flop_10_quotes";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/modify/{quoteId}")
    public String modifyQuote(@PathVariable final int quoteId, final Model model) {
        final String username = userService.getUsernameFromSecurityContextHolder();

        if (username != null) {
            model.addAttribute("logged", userService.findByUsername(username));
        }

        model.addAttribute("quote", quoteService.findQuoteById(quoteId));

        return "modify-quote";
    }

    @PostMapping("/update-quote/{quoteId}")
    public String updateQuote(
        @ModelAttribute("quote") final QuoteDTO quote,
        final BindingResult result,
        @PathVariable final int quoteId
    ) {
        if (result.hasErrors()) {
            return "modify-quote";
        }

        final QuoteDTO quoteDTO = quoteService.findQuoteById(quoteId);

        quoteDTO.setQuote(quote.getQuote());
        quoteDTO.setDateOfCreation(LocalDateTime.now());

        quoteService.save(quoteDTO);

        return "redirect:/";
    }

    @GetMapping("/add-quote")
    public String addNewQuote(final Model model) {
        final String username = userService.getUsernameFromSecurityContextHolder();

        if (username != null) {
            model.addAttribute("logged", userService.findByUsername(username));
        }

        model.addAttribute("quote", new QuoteDTO());

        return "add-new-quote";
    }

    @PostMapping("/save-quote")
    public String saveNewQuote(
        @ModelAttribute @Valid final QuoteDTO quoteDTO,
        final BindingResult result
    ) {
        if (result.hasErrors()) {
            return "add-new-quote";
        }

        quoteDTO.setCreator(userService.getUsernameFromSecurityContextHolder());
        quoteDTO.setDateOfCreation(LocalDateTime.now());
        quoteDTO.setVotes(0);

        quoteService.save(quoteDTO);

        return "redirect:/";
    }

    @DeleteMapping("/delete/{quoteId}")
    public String deleteQuote(@PathVariable final int quoteId) {
        quoteService.delete(quoteId);

        return "redirect:/";
    }
}
