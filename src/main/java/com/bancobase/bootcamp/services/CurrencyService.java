package com.bancobase.bootcamp.services;

import com.bancobase.bootcamp.dto.response.ExchangeRateResponse;
import com.bancobase.bootcamp.dto.response.Symbol;
import com.bancobase.bootcamp.dto.response.SymbolsNameResponse;
import com.bancobase.bootcamp.http.APIExchangeRateClient;
import com.bancobase.bootcamp.repositories.CurrencyRepository;
import com.bancobase.bootcamp.schemas.CurrencySchema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bancobase.bootcamp.dto.*;

import java.util.*;

@RestController
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
        if (this.currencyRepository.count() > 0L) {
            this.currencyRepository.deleteAll();
        }
        setSymbolsAndExchangeRateCurrencies();
    }

    @GetMapping("/currencies")
    public List<CurrencyDTO> getCurrencies() {
        return currencyRepository
                .findAll()
                .stream()
                .map(CurrencyDTO::getFromSchema)
                .toList();
    }

    private void setSymbolsAndExchangeRateCurrencies() {
        APIExchangeRateClient api = new APIExchangeRateClient();
        ExchangeRateResponse exchangeRateResponse = api.getExchangeRate();
        SymbolsNameResponse symbolsNameResponse = api.getSymbolsName();
        Map<String, Double> rates = exchangeRateResponse.getRates();
        for (String key : symbolsNameResponse.getSymbols().keySet()) {
            Symbol symbol = symbolsNameResponse.getSymbols().get(key);
            String name = symbol.getDescription();
            String code = symbol.getCode();
            Double value = rates.get(code);

            if (name != null && code != null && value != null) {
                CurrencySchema currencySchema = new CurrencySchema();
                currencySchema.setName(name);
                currencySchema.setSymbol(code);
                currencySchema.setValue(value);
                currencyRepository.save(currencySchema);
            }
        }
    }
}