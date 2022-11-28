package ru.netology.sendmoney.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.sendmoney.model.ConfirmOperation;
import ru.netology.sendmoney.model.Transaction;
import ru.netology.sendmoney.service.ConfirmService;
import ru.netology.sendmoney.service.TransferService;


@RestController()
@RequestMapping("/")
public class GetDataFromClientController {
    private TransferService transferService;
    private ConfirmService confirmService;
    private static Logger logger = LoggerFactory.getLogger(GetDataFromClientController.class);

    public GetDataFromClientController(TransferService transferService, ConfirmService confirmService) {
        this.transferService = transferService;
        this.confirmService = confirmService;
    }

    @CrossOrigin
    @PostMapping("transfer")
    private ResponseEntity<ConfirmOperation> checkAndDoTransfer(@RequestBody Transaction transaction) {
        logger.info("Start to check & do operation between cards: " + transaction);
        return ResponseEntity.ok(transferService.doTransaction(transaction));
    }

    @CrossOrigin
    @PostMapping("confirmOperation")
    private ResponseEntity<ConfirmOperation> checkConfirmOperation(@RequestBody ConfirmOperation confirmOperation) {
        logger.info("Rabotaet confirm " + confirmOperation);
        return ResponseEntity.ok(confirmService.checkConfirmOperation(confirmOperation));
    }
}
