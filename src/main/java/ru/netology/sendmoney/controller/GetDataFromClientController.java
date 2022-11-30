package ru.netology.sendmoney.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.sendmoney.logger.LoggerTransaction;
import ru.netology.sendmoney.model.operation.ConfirmOperation;
import ru.netology.sendmoney.model.operation.OperationId;
import ru.netology.sendmoney.model.transaction.Transaction;
import ru.netology.sendmoney.service.ConfirmService;
import ru.netology.sendmoney.service.TransferService;

@RestController()
@RequestMapping("/")
public class GetDataFromClientController {
    private TransferService transferService;
    private ConfirmService confirmService;
    private static LoggerTransaction logger = LoggerTransaction.getLogger();

    public GetDataFromClientController(TransferService transferService, ConfirmService confirmService) {
        this.transferService = transferService;
        this.confirmService = confirmService;
    }

    @CrossOrigin
    @PostMapping("transfer")
    private ResponseEntity<OperationId> checkAndDoTransfer(@RequestBody Transaction transaction) {
        logger.log("> Begin transaction!");
        return ResponseEntity.ok(transferService.checkTransfer(transaction));
    }

    @CrossOrigin
    @PostMapping("confirmOperation")
    private ResponseEntity<ConfirmOperation> checkConfirmOperation(@RequestBody ConfirmOperation confirmOperation) {
        logger.log("> Begin confirm operation!");
        return ResponseEntity.ok(confirmService.checkConfirmOperation(confirmOperation));
    }
}
