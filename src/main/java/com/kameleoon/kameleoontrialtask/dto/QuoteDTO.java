package com.kameleoon.kameleoontrialtask.dto;

import com.kameleoon.kameleoontrialtask.entity.Quote;
import lombok.*;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Comparator;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class QuoteDTO implements DTO {
    private int id;
    @Size(min = 1, message = "Quote must be at least 1 character long")
    private String quote;
    private String creator;
    private LocalDateTime dateOfCreation;
    private int votes;

    public static QuoteDTO of(final Quote quote) {
        return QuoteDTO.builder()
            .id(quote.getId())
            .quote(quote.getQuote())
            .creator(quote.getCreator().getUsername())
            .dateOfCreation(quote.getDateOfCreation())
            .votes(quote.getVotes())
            .build();
    }

    public static final Comparator<QuoteDTO> COMPARE_BY_VOTE = Comparator.comparing(QuoteDTO::getVotes);

    public static final Comparator<QuoteDTO> COMPARE_BY_DATE = Comparator.comparing(QuoteDTO::getDateOfCreation);
}
