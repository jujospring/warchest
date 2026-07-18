package net.number33.warchest.api;

import net.number33.warchest.service.ValueService;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ValueController {

    private final ValueService valueService;

    public ValueController(ValueService valueService) {
        this.valueService = valueService;
    }

    @GetMapping("/players/value")
    public List<PlayerValue> playerValues(@RequestParam int season) {
        return valueService.rankBySeason(season);
    }

    @GetMapping("/players/albatrosses")
    public List<PlayerValue> albatrosses(@RequestParam int season) {
        return valueService.albatrosses(season);
    }

    @GetMapping("/teams/{abbrev}/efficiency")
    public TeamEfficiency teamEfficiency(@PathVariable String abbrev, @RequestParam int season) {
        return valueService.teamEfficiency(abbrev, season);
    }

    @GetMapping("/players/search")
    public List<PlayerValue> searchByNameAndSeason(@RequestParam String name, @RequestParam int season) {
        return valueService.playerValueBySeason(name, season);
    }

    @GetMapping("/players/career")
    public List<PlayerValue> searchByName(@RequestParam String name) {
        return valueService.playerValues(name);
    }

    @GetMapping("/players/{mlbPlayerId}")
    public List<PlayerValue> searchById(@PathVariable int mlbPlayerId) {
        return valueService.playerData(mlbPlayerId);
    }

}
