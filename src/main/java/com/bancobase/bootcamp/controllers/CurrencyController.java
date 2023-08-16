package com.bancobase.bootcamp.controllers;

import com.bancobase.bootcamp.dto.CurrencyDTO;
import com.bancobase.bootcamp.services.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
@Tag(name = "Currencies controller")
@CrossOrigin(origins = {"*"}, methods = {RequestMethod.GET, RequestMethod.POST})
public class CurrencyController {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    @Operation(summary = "Get all currencies")
    public ResponseEntity<List<CurrencyDTO>> getCurrencies() {
        List<CurrencyDTO> currencies = this.currencyService.getCurrencies();

        return new ResponseEntity<>(currencies, HttpStatus.OK);
    }

}
