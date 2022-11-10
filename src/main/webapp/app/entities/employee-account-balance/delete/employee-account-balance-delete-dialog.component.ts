import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployeeAccountBalance } from '../employee-account-balance.model';
import { EmployeeAccountBalanceService } from '../service/employee-account-balance.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './employee-account-balance-delete-dialog.component.html',
})
export class EmployeeAccountBalanceDeleteDialogComponent {
  employeeAccountBalance?: IEmployeeAccountBalance;

  constructor(protected employeeAccountBalanceService: EmployeeAccountBalanceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeAccountBalanceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
