package com.kameleoon.kameleoontrialtask.service;

import com.kameleoon.kameleoontrialtask.dao.QuoteDAO;
import com.kameleoon.kameleoontrialtask.dao.UserDAO;
import com.kameleoon.kameleoontrialtask.dto.QuoteDTO;
import com.kameleoon.kameleoontrialtask.entity.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class QuoteService {
    private final QuoteDAO quoteDAO;
    private final UserDAO userDAO;

    @Autowired
    public QuoteService(
        final QuoteDAO quoteDAO,
        final UserDAO userDAO
    ) {
        this.quoteDAO = quoteDAO;
        this.userDAO = userDAO;
    }

    public QuoteDTO findQuoteById(final int id) {
        return QuoteDTO.of(quoteDAO.findById(id).get());
    }

    public List<QuoteDTO> getAllQuotes() {
        return quoteDAO.findAll().stream().map(QuoteDTO::of).collect(Collectors.toList());
    }

    public List<QuoteDTO> getTop10Quotes() {
        final List<QuoteDTO> quotes = quoteDAO.findAll().stream()
            .map(QuoteDTO::of)
            .collect(Collectors.toList());

        Collections.sort(quotes, Collections.reverseOrder(QuoteDTO.COMPARE_BY_VOTE));

        return getOnly10Quotes(quotes);
    }

    public List<QuoteDTO> getFlop10Quotes() {
        final List<QuoteDTO> quotes = quoteDAO.findAll().stream()
            .map(QuoteDTO::of)
            .collect(Collectors.toList());

        Collections.sort(quotes, QuoteDTO.COMPARE_BY_VOTE);

        return getOnly10Quotes(quotes);
    }

    private List<QuoteDTO> getOnly10Quotes(final List<QuoteDTO> quotes) {
        final List<QuoteDTO> resultQuotes;

        if (quotes.size() >= 10) {
            resultQuotes = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                resultQuotes.add(quotes.get(i));
            }
        } else {
            resultQuotes = new ArrayList<>(quotes);
        }

        return resultQuotes;
    }

    public QuoteDTO getLastQuote() {
        final List<QuoteDTO> quotes = quoteDAO.findAll().stream()
            .map(QuoteDTO::of)
            .collect(Collectors.toList());

        Collections.sort(quotes, QuoteDTO.COMPARE_BY_DATE);

        return quotes.get(quotes.size() - 1);
    }

    public String getRandomQuote() {
        final int randomIndex = getRandomIndex();

        return quoteDAO.findById(randomIndex).get().getQuote();
    }

    public QuoteDTO getFullRandomQuote() {
        final int randomIndex = getRandomIndex();

        return QuoteDTO.of(quoteDAO.findById(randomIndex).get());
    }

    public boolean save(final QuoteDTO quoteDTO) {
        final Quote quote = Quote.builder()
            .id(quoteDTO.getId())
            .quote(quoteDTO.getQuote())
            .creator(userDAO.findByUsername(quoteDTO.getCreator()))
            .dateOfCreation(quoteDTO.getDateOfCreation())
            .votes(quoteDTO.getVotes())
            .build();

        quoteDAO.save(quote);

        return true;
    }

    public boolean delete(final int quoteId) {
        quoteDAO.delete(quoteDAO.findById(quoteId).get());

        return true;
    }

    private int getRandomIndex() {
        final Random random = new Random();
        final List<Integer> quoteIds =
            quoteDAO.findAll().stream().map(QuoteDTO::of).map(QuoteDTO::getId).collect(Collectors.toList());

        int randomIndex = random.nextInt(quoteDAO.findAll().size()) + 1;

        while (!quoteIds.contains(randomIndex)) {
            randomIndex = random.nextInt(quoteDAO.findAll().size()) + 1;
        }

        return randomIndex;
    }
}
