package ru.netology.sendmoney.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.sendmoney.logger.LoggerTransaction;
import ru.netology.sendmoney.model.operation.ConfirmOperation;
import ru.netology.sendmoney.model.operation.OperationId;
import ru.netology.sendmoney.model.transaction.Transaction;
import ru.netology.sendmoney.service.ConfirmService;
import ru.netology.sendmoney.service.CheckTransferService;

@RestController()
@RequestMapping("/")
public class GetDataFromClientController {
    private CheckTransferService checkTransferService;
    private ConfirmService confirmService;
    private static LoggerTransaction logger = LoggerTransaction.getLogger();

    public GetDataFromClientController(CheckTransferService checkTransferService, ConfirmService confirmService) {
        this.checkTransferService = checkTransferService;
        this.confirmService = confirmService;
    }

    @CrossOrigin
    @PostMapping("transfer")
    private ResponseEntity<OperationId> checkAndDoTransfer(@RequestBody Transaction transaction) {
        logger.log(">>> Begin transaction!");
        return ResponseEntity.ok(checkTransferService.checkCardBalance(transaction));
    }

    @CrossOrigin
    @PostMapping("confirmOperation")
    private ResponseEntity<ConfirmOperation> checkConfirmOperation(@RequestBody ConfirmOperation confirmOperation) {
        logger.log("Begin confirm operation!");
        return ResponseEntity.ok(confirmService.checkConfirmOperation(confirmOperation));
    }
}
