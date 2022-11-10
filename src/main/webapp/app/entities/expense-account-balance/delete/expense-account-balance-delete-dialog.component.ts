import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpenseAccountBalance } from '../expense-account-balance.model';
import { ExpenseAccountBalanceService } from '../service/expense-account-balance.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './expense-account-balance-delete-dialog.component.html',
})
export class ExpenseAccountBalanceDeleteDialogComponent {
  expenseAccountBalance?: IExpenseAccountBalance;

  constructor(protected expenseAccountBalanceService: ExpenseAccountBalanceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expenseAccountBalanceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
