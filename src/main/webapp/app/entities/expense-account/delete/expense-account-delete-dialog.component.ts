import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpenseAccount } from '../expense-account.model';
import { ExpenseAccountService } from '../service/expense-account.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './expense-account-delete-dialog.component.html',
})
export class ExpenseAccountDeleteDialogComponent {
  expenseAccount?: IExpenseAccount;

  constructor(protected expenseAccountService: ExpenseAccountService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expenseAccountService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
