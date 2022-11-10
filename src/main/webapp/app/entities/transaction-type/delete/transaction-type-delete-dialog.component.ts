import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITransactionType } from '../transaction-type.model';
import { TransactionTypeService } from '../service/transaction-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './transaction-type-delete-dialog.component.html',
})
export class TransactionTypeDeleteDialogComponent {
  transactionType?: ITransactionType;

  constructor(protected transactionTypeService: TransactionTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transactionTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
