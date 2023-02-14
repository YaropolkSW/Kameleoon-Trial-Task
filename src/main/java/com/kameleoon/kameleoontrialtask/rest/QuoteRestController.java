package com.kameleoon.kameleoontrialtask.rest;

import com.kameleoon.kameleoontrialtask.dto.DTO;
import com.kameleoon.kameleoontrialtask.dto.QuoteDTO;
import com.kameleoon.kameleoontrialtask.response.Response;
import com.kameleoon.kameleoontrialtask.service.QuoteService;
import com.kameleoon.kameleoontrialtask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quotes")
public class QuoteRestController {
    private final static String NO_SUCH_QUOTE = "No such quote!";
    private final static String QUOTE_WAS_DELETED = "Quote with id = %s was deleted.";
    private final static String CANT_MODIFY_QUOTE = "You can only modify quotes that you added yourself!";
    private final static String CANT_DELETE_QUOTE = "You can only delete quotes that you added yourself!";

    private final QuoteService quoteService;
    private final UserService userService;

    @Autowired
    public QuoteRestController(
        final QuoteService quoteService,
        final UserService userService
    ) {
        this.quoteService = quoteService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<QuoteDTO> getAllQuotes() {
        return quoteService.getAllQuotes();
    }

    @GetMapping("/{quoteId}")
    public DTO getQuote(@PathVariable final int quoteId) {
        final List<Integer> quoteIds =
            quoteService.getAllQuotes().stream().map(QuoteDTO::getId).collect(Collectors.toList());

        if (quoteIds.contains(quoteId)) {
            return quoteService.findQuoteById(quoteId);
        } else {
            return new Response(NO_SUCH_QUOTE);
        }
    }

    @GetMapping("/random")
    public QuoteDTO getRandomQuote() {
        return quoteService.getFullRandomQuote();
    }

    @GetMapping("/top10")
    public List<QuoteDTO> getTop10Quotes() {
        return quoteService.getTop10Quotes();
    }

    @GetMapping("/flop10")
    public List<QuoteDTO> getFlop10Quotes() {
        return quoteService.getFlop10Quotes();
    }

    @GetMapping("/last")
    public QuoteDTO getLastQuote() {
        return quoteService.getLastQuote();
    }

    @PostMapping("/add-quote")
    public QuoteDTO addNewQuote(@RequestBody final QuoteDTO quote) {
        quote.setDateOfCreation(LocalDateTime.now());
        quote.setCreator(userService.getUsernameFromSecurityContextHolder());
        quote.setVotes(0);

        quoteService.save(quote);

        return quote;
    }

    @PostMapping("/modify-quote")
    public DTO modifyQuote(@RequestBody final QuoteDTO quoteDTO) {
        if (userService.getUsernameFromSecurityContextHolder().equals(quoteDTO.getCreator())) {
            quoteDTO.setDateOfCreation(LocalDateTime.now());
            quoteDTO.setVotes(0);

            quoteService.save(quoteDTO);

            return quoteDTO;
        } else {
            return new Response(CANT_MODIFY_QUOTE);
        }
    }

    @DeleteMapping("/delete/{quoteId}")
    public DTO deleteQuote(@PathVariable final int quoteId) {
        final List<Integer> quoteIds =
            quoteService.getAllQuotes().stream().map(QuoteDTO::getId).collect(Collectors.toList());

        if (quoteIds.contains(quoteId)) {
            final QuoteDTO quoteDTO = quoteService.findQuoteById(quoteId);

            if (quoteDTO.getCreator().equals(userService.getUsernameFromSecurityContextHolder())) {
                quoteService.delete(quoteId);

                return new Response(String.format(QUOTE_WAS_DELETED, quoteId));
            } else {
                return new Response(CANT_DELETE_QUOTE);
            }
        } else {
            return new Response(NO_SUCH_QUOTE);
        }
    }

    @GetMapping("/upvote/{quoteId}")
    public DTO upvote(@PathVariable final int quoteId) {
        final List<Integer> quoteIds =
            quoteService.getAllQuotes().stream().map(QuoteDTO::getId).collect(Collectors.toList());

        if (quoteIds.contains(quoteId)) {
            final QuoteDTO quoteDTO = quoteService.findQuoteById(quoteId);

            quoteDTO.setDateOfCreation(LocalDateTime.now());
            quoteDTO.setVotes(quoteDTO.getVotes() + 1);

            quoteService.save(quoteDTO);

            return quoteDTO;
        } else {
            return new Response(NO_SUCH_QUOTE);
        }
    }

    @GetMapping("/downvote/{quoteId}")
    public DTO downvote(@PathVariable final int quoteId) {
        final List<Integer> quoteIds =
            quoteService.getAllQuotes().stream().map(QuoteDTO::getId).collect(Collectors.toList());

        if (quoteIds.contains(quoteId)) {
            final QuoteDTO quoteDTO = quoteService.findQuoteById(quoteId);

            quoteDTO.setDateOfCreation(LocalDateTime.now());
            quoteDTO.setVotes(quoteDTO.getVotes() - 1);

            quoteService.save(quoteDTO);

            return quoteDTO;
        } else {
            return new Response(NO_SUCH_QUOTE);
        }
    }
}
