import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICashBookBalance } from '../cash-book-balance.model';
import { CashBookBalanceService } from '../service/cash-book-balance.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './cash-book-balance-delete-dialog.component.html',
})
export class CashBookBalanceDeleteDialogComponent {
  cashBookBalance?: ICashBookBalance;

  constructor(protected cashBookBalanceService: CashBookBalanceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cashBookBalanceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
