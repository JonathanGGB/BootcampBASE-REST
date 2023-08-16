package com.bancobase.bootcamp.services;

import com.bancobase.bootcamp.dto.CustomerDTO;
import com.bancobase.bootcamp.dto.CustomerInfoDTO;
import com.bancobase.bootcamp.dto.request.PreCustomerInfo;
import com.bancobase.bootcamp.exceptions.BusinessException;
import com.bancobase.bootcamp.repositories.CustomerRepository;
import com.bancobase.bootcamp.schemas.AccountSchema;
import com.bancobase.bootcamp.schemas.CustomerSchema;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final AccountService accountService;
    private final CustomerRepository customerRepository;

    public CustomerService(AccountService accountService, CustomerRepository customerRepository) {
        this.accountService = accountService;
        this.customerRepository = customerRepository;
    }

    public CustomerDTO getCustomerById(@PathVariable Long customerId) {
        Optional<CustomerSchema> customerOptional = customerRepository.findById(customerId);

        if (customerOptional.isPresent()) {
            CustomerSchema customerSchema = customerOptional.get();
            return CustomerDTO.getFromSchema(customerSchema, customerSchema.getAccounts());
        } else {
            return null;
        }
    }

    public List<CustomerInfoDTO> filterCustomersByName(String name) {
        return customerRepository
                .findByNameContaining(name)
                .stream()
                .map(CustomerInfoDTO::getFromSchema)
                .toList();
    }

    public CustomerDTO createCustomer(PreCustomerInfo information) {
        if (customerRepository.findByCurp(information.getCurp()).isPresent()) {
            throw BusinessException
                    .builder()
                    .message("A customer with the same CURP " + information.getCurp() + " is already registered")
                    .build();
        }

        CustomerSchema customer = new CustomerSchema();

        customer.setBirthdate(information.getBirthdate());
        customer.setCurp(information.getCurp());
        customer.setGender(information.getGender());
        customer.setName(information.getName());

        CustomerSchema savedCustomer = customerRepository.save(customer);
        List<AccountSchema> accounts = accountService.createAccount(savedCustomer);

        return CustomerDTO.getFromSchema(customer, accounts);
    }
}
