import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMerchant } from '../merchant.model';
import { MerchantService } from '../service/merchant.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './merchant-delete-dialog.component.html',
})
export class MerchantDeleteDialogComponent {
  merchant?: IMerchant;

  constructor(protected merchantService: MerchantService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.merchantService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
