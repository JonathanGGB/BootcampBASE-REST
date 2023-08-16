package com.bancobase.bootcamp.services;

import com.bancobase.bootcamp.dto.CurrencyDTO;
import com.bancobase.bootcamp.dto.response.*;
import com.bancobase.bootcamp.http.APIExchangeRateClient;
import com.bancobase.bootcamp.repositories.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {

    private final APIExchangeRateClient apiExchangeRateClient;
    private final CurrencyRepository currencyRepository;
    public CurrencyService(APIExchangeRateClient apiExchangeRateClient, CurrencyRepository currencyRepository) {
        this.apiExchangeRateClient = apiExchangeRateClient;
        this.currencyRepository = currencyRepository;
    }

    public List<CurrencyDTO> getCurrencies() {
        ExchangeRateResponse exchangeRate = apiExchangeRateClient.getExchangeRate();
        SymbolsNameResponse symbolsName = apiExchangeRateClient.getSymbolsName();

        return CurrencyDTO
                .mergeExchangeRateAndSymbolsNameResponses(exchangeRate, symbolsName);
    }

    public List<CurrencyDTO> getCurrenciesBySearch(String search) {
        ExchangeRateResponse exchangeRate = apiExchangeRateClient.getExchangeRate();
        SymbolsNameResponse symbolsName = apiExchangeRateClient.getSymbolsName();

        return CurrencyDTO
                .mergeExchangeRateAndSymbolsNameResponses(exchangeRate, symbolsName);
    }
}