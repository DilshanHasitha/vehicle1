import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployeeAccount } from '../employee-account.model';
import { EmployeeAccountService } from '../service/employee-account.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './employee-account-delete-dialog.component.html',
})
export class EmployeeAccountDeleteDialogComponent {
  employeeAccount?: IEmployeeAccount;

  constructor(protected employeeAccountService: EmployeeAccountService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeAccountService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
