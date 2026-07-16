package net.number33.warchest.api;

import net.number33.warchest.service.CsvImportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// TODO: endpoint has no auth, shared secret header the controller checks against an env var (?)

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final CsvImportService csvImportService;

    public AdminController(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @PostMapping("/import")
    public Map<String, Object> importSeason(@RequestParam int season) {
        int imported = csvImportService.importBattingSeason(season);
        return Map.of("season", season, "imported", imported);
    }
}
