import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpenseType } from '../expense-type.model';
import { ExpenseTypeService } from '../service/expense-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './expense-type-delete-dialog.component.html',
})
export class ExpenseTypeDeleteDialogComponent {
  expenseType?: IExpenseType;

  constructor(protected expenseTypeService: ExpenseTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expenseTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
