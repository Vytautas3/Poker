package poker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poker.dto.CardHandDTO;
import poker.service.PokerService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/poker")
public class PokerController {

    private final PokerService pokerService;

    @Autowired
    public PokerController(PokerService pokerService) {
        this.pokerService = pokerService;
    }

    @PostMapping("/results")
    public ResponseEntity<String> results(@RequestBody List<CardHandDTO> cardHands) {
        return ResponseEntity.ok(pokerService.getResults(cardHands));
    }
}
