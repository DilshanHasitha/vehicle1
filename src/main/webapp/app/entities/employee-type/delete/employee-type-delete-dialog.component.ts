import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployeeType } from '../employee-type.model';
import { EmployeeTypeService } from '../service/employee-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './employee-type-delete-dialog.component.html',
})
export class EmployeeTypeDeleteDialogComponent {
  employeeType?: IEmployeeType;

  constructor(protected employeeTypeService: EmployeeTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
